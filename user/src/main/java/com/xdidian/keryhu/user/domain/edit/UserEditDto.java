package com.xdidian.keryhu.user.domain.edit;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 
 * 用户 修改个人资料，头像，姓名，email，手机号，生日 等，首先需要下载这些信息到页面。的class。)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年8月15日 上午11:04:38
 */


@Data
public class UserEditDto implements Serializable {


  private String id; // userId

  private String email;

  private String phone;

  private byte[] header;  // 头像png base64。

  private boolean useDefaultHeaderImg;  //是否使用的是 默认的头像。

  private String name; // 对外公布的名字。

  private boolean nameCanModify;  //用户的姓名是否可以修改。

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime nameModifyTime; // 记录姓名修改时间

  // 用户的生日，只需要月份－日期，其中年份统一为0000
  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate birthday;


  public UserEditDto(){
    
  }


}
