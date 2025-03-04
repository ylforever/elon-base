package com.elon.base.rest;

import com.elon.base.util.JWTUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 基础控制类
 *
 * @author neo
 * @since 2025-02-17
 */
public class BaseController {
    private static final Logger LOGGER = LogManager.getLogger(BaseController.class);

    /**
     * 从请求头Token中获取用户账号信息
     *
     * @return 用户账号
     */
    protected String getUserAccount() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "";
        }

        try {
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader("jwt-token");

            Map<String, String> parameterMap  = JWTUtil.instance().decodeToken(token);
            if (parameterMap.containsKey("account")) {
                return parameterMap.get("account");
            } else {
                LOGGER.error("Can not find account in token.");
                return "";
            }
        } catch (Exception e) {
            LOGGER.error("Parse token fail.", e);
            return "";
        }
    }
}
