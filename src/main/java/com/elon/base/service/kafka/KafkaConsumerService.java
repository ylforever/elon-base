package com.elon.base.service.kafka;

import com.alibaba.fastjson.JSON;
import com.elon.base.model.TaskBase;
import com.elon.base.util.StringUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Kafka消费者服务类。订阅接收消息, 再转给具体的业务处理类处理
 *
 * @author neo
 * @since 2024-5-14
 */
@Component
public class KafkaConsumerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Value("${neo.application_name:}")
    private String applicationName;

    // Kafka分区器连接
    @Value("${neo.kafka.bootstrap.servers:}")
    private String kafkaServer;

    // Kafka组ID
    @Value("${neo.kafka.group.id:}")
    private String kafkaGroupId;

    // 最大一次拉取的消息数量
    @Value("${neo.kafka.max.poll.records:1}")
    private int maxPollRecords;

    @Value("${neo.kafka.topics}")
    private List<String> topics;

    @Value("${neo.redis.ip:}")
    private String redisIp;

    @Value("${neo.redis.port:}")
    private int redisPort;

    // 消费者
    private KafkaConsumer consumer = null;

    // 任务处理器. Map<任务编码, 任务处理器>
    private Map<String, TaskHandler> handlerMap = new HashMap<>();

    /**
     * 注册任务处理器
     *
     * @param handler 任务处理器
     */
    public void registerHandler(TaskHandler handler) {
        handlerMap.put(handler.getTaskCode(), handler);
    }

    /**
     * 初始化消费者实例. 订阅消息
     */
    public void initKafkaConsumer() {
        LOGGER.info("Subscribe message. kafkaServer:{}|kafkaGroupId:{}|maxPollRecords:{}|topics:{}",
                kafkaServer, kafkaGroupId, maxPollRecords, topics);

        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaServer);  // 指定 Broker
        properties.put("group.id", kafkaGroupId);              // 指定消费组群 ID
        properties.put("max.poll.records", maxPollRecords);
        properties.put("enable.auto.commit", "false");
        properties.put("key.deserializer", StringDeserializer.class); // 将 key 的字节数组转成 Java 对象
        properties.put("value.deserializer", StringDeserializer.class);  // 将 value 的字节数组转成 Java 对象

        consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(topics);  // 订阅主题

        new Thread(this::handleMessage).start();
    }

    /**
     * 从Kafka获取消息，传给相应的处理器处理.
     */
    public void handleMessage() {
        Jedis jedisClient = getJedisClient();
        while (true) {
            synchronized (this) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
                LOGGER.info("Fetch record num:{}", records.count());
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        handleSingleMessage(jedisClient, record);
                    } catch (Exception e) {
                        LOGGER.error("Handle message fail. Topic:{}|Partition:{}|Offset:{}|Key:{}|Message:{}",
                                record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    }
                }

                // 提交任务，更新offset
                consumer.commitSync();
            }
        }
    }

    private void handleSingleMessage(Jedis jedisClient, ConsumerRecord<String, String> record) {
        TaskBase taskBase = JSON.parseObject(record.value(), TaskBase.class) ;
        if (!handlerMap.containsKey(taskBase.getTaskCode())) {
            return;
        }

        // 判断同一个任务是否已经有其它实例在处理
        String taskKey = "Task_" + taskBase.getTaskId();
        String handleAppName = jedisClient.getSet(taskKey, applicationName);

        // 设置过期时间只是为了方便自动清除redis中的数据。在实际项目中，任务数据是非常重要的，往往需要持久化到数据库
        jedisClient.expire(taskKey, 60 * 60);
        if (!StringUtil.isEmpty(handleAppName)) {
            jedisClient.set(taskKey, handleAppName, new SetParams().px(1000 * 60 * 60));
            LOGGER.info("Task:{} completed. Handle app name:{}", taskBase.getTaskId(), handleAppName);
            return;
        }

        // 将消息分发给具体的handler类处理
        LOGGER.info("Handle message. Topic:{}|Partition:{}|Offset:{}|Key:{}|Message:{}",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
        TaskHandler handler = handlerMap.get(taskBase.getTaskCode());
        handler.handle(record.value());
    }

    public Jedis getJedisClient() {
        Jedis jedis = new Jedis(redisIp, redisPort);
        return jedis;
    }
}
