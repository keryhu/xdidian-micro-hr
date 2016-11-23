package com.xdidian.keryhu.auth_server.service;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component("utils")
public class UtilsImpl implements Utils {

    /**
     * 获取用户本地当前ip地址
     */
    @Override
    public String getIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        return (xfHeader == null || xfHeader.isEmpty()) ? request.getRemoteAddr()
                : xfHeader.split(",")[0];
    }

    // 当email 未激活的时候，从account-activated 发送过来的，包含resendtoken的string，转为map

    @Override
    public Map<String, String> emailUnActivatedStringToMap(String value) {
        Map<String, String> map = new HashMap();
        try {
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON string to Map
            map = mapper.readValue(value, new TypeReference<Map<String, String>>() {});

            System.out.println(map);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

}
