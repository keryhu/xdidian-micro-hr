package com.xdidian.keryhu.signup.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;



/**
 * @Description : 激活信息的配置属性
 * @date : 2016年6月18日 下午9:14:20
 * @author : keryHu keryhu@hotmail.com
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "emailActivate")
public class ActivatedProperties implements Serializable {


  private static final long serialVersionUID = -917850265000066502L;

  private int expiredTime;// 默认单位是小时


}
