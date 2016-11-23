package com.xdidian.keryhu.account_activate.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.RecoverMethod;

import lombok.Data;

/**
 * 
 * @ClassName: CommonActivatedTokn
 * 适用于 这是一个使用下面3种情景的通用的 数据库保存的class
 * 1 用户注册完，email激活 2 忘记密码，通过手机号或邮箱找回密码 3
 *               用户资料修改时候，发送新的邮箱或者手机号的 token验证， )
 * @author keryhu keryhu@hotmail.com
 * @date 2016年9月9日 下午2:39:09
 */



@Data
public class CommonActivatedLocalToken implements Serializable {

  private static final long serialVersionUID = -8929039068651339288L;

  @Id
  private String id;

  // 用户的userId，可选，当编辑个人资料的时候，提交新的email或phone的时候，需要当前的userId
  private String userId;

  private String account; // token，对应的账号，email的值或phone的值

  private String token; // 具体的验证码token

  private String resendToken; // 用于用户点击“重新发送验证邮件”时，需要被验证的token

  private String resignupToken; // 可选，当注册时候，才会出现这个。

  // 哪种应用在调用此方法，，注册／密码重设，还是 个人资料修改
  private ApplySituation applySituation;

  // 可选，重设密码的时候，取得验证码的方法  为email 或phone
  private RecoverMethod recoverMethod;

  private boolean locked; // 重新发送的url是否被冻结。

  private LocalDateTime lockedTime; // 被锁定的时间点

  private LocalDateTime firstTime; // 第一次点击的时间点。

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime expiryDate; // token 过期时间

  // 邮件激活，已经重发的次数（包含当前userId下更换email的次数）
  private int sentTimes;

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime sendExpiryDate; // 点击重新发送过期时间


  public CommonActivatedLocalToken() {
    this.id = UUID.randomUUID().toString();
    this.expiryDate = null;
    this.account = null;
    this.token = null;
    this.resendToken = null;
    this.resignupToken=null;
    this.sentTimes = 0;
    this.sendExpiryDate = null;
    this.locked = false;
    this.lockedTime = null;
    this.firstTime = null;
  }



}
