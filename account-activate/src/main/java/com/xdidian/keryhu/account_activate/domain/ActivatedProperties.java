/**
 * @Title: HostProperty.java
 * @Package com.xdidian.keryhu.mailServer.mail
 * @Description: TODO(用一句话描述该文件做什么)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年5月7日 下午7:34:59
 * @version V1.0
 */
package com.xdidian.keryhu.account_activate.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;


/**
 * 
 * 自定义host，通过application取得docker 的ip地址作为host，主要用在email激活时， 需要填写的url地址
 * @date : 2016年6月18日 下午8:09:53
 * @author : keryHu keryhu@hotmail.com
 */
@Component
@ConfigurationProperties(prefix = "activated")
@Getter
@Setter
public class ActivatedProperties implements Serializable {

  private static final long serialVersionUID = 6219341996080208763L;
  
  //设置密码重设，验证码的最大有效期是0.5个小时，如果超时了，那么验证码就失效了，需要重新获取验证码
  //最多允许重复发送 6次 
  private int expiredTime;   // 默认单位是小时 

  private int maxSendTimes;      // 允许最多的重复发送的次数
  private int minutesOfSendCycle;  // 点击“再次发送激活邮件的 间隔时间 单位分钟数
  private int hoursOfResetSendTimes;         //隔多久，sentTimes自动为0，单位为小时。
  private int hoursOfLocked;        //一旦被冻结，冻结多少个小时，单位为小时。
 

  
}
