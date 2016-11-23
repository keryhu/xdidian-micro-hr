package com.xdidian.keryhu.auth_server.domain;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;


/**
 * 
 * @Description : 将yml配置文件中，自定义的jwt.clientId,clientsecret等属性配置起了
 * @date : 2016年6月18日 下午7:54:50
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@ConfigurationProperties(prefix = "jwtOfReadAndWrite")
@Data
public class JwtOfReadAndWrite implements Serializable {


  private static final long serialVersionUID = 1L;
  private String clientId;
  private String clientSecret;
  private boolean autoApproval;
  private String[] scopes;
  private String[] grantTypes;
  private String[] resourceIds;
  private int accessTokenValiditySeconds;
  private int refreshTokenValiditySeconds;



}
