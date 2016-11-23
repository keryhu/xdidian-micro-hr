package com.xdidian.keryhu.mail.domain;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 * @author : keryHu keryhu@hotmail.com
 * @Description :获取当前 的 host，
 * @date : 2016年6月18日 下午9:10:25
 */


@Component("host")
@ConfigurationProperties(prefix = "hostProperty")
@Getter
@Setter
public class Host implements Serializable {

    private String hostName;
}