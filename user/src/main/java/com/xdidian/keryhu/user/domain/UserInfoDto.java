package com.xdidian.keryhu.user.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;



import lombok.Data;
import org.springframework.web.servlet.tags.form.SelectTag;

/**
 * 
 * @ClassName: UserInfoDto
 * @Description: 用户登录后，查看本人的个人资料，或公司管理员，新地点的客服或管理人员，
 * 查看某个人的 个人信息资料，这些资料只属于存储在user 数据库中的信息
 * @author keryhu keryhu@hotmail.com
 * @date 2016年8月15日 上午11:04:38
 */


@Data
@NoArgsConstructor
public class UserInfoDto implements Serializable {

  private static final long serialVersionUID = 4973534920168786326L;

  private String id; // userId

  private String email;

  private String phone;
  
  private byte[] header;  // 头像png base64。
  

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime registerTime; // 用户注册时间

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime lastLoginTime; // 记录上次登录时间

  //user 对应的company 的id，如果这个id为null或 "",那么就证明他目前没有公司。如果companyId为uuid，那么就证明现在有公司
  // 一个人可以  有 多个公司。
  private Set<String> companyIds=new HashSet<>();


  private String name; // 对外公布的名字。


  // 用户的生日，只需要月份－日期，其中年份统一为0000
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDate birthday;


}
