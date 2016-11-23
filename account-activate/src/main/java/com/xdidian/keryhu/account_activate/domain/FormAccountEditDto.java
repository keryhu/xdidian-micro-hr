package com.xdidian.keryhu.account_activate.domain;

import java.io.Serializable;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
* @ClassName: FormAccountEditDto
* 在前台修改 个人资料，email或者phone的时候，需要前台提交  userId，账号，和可选的密码。)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月10日 下午5:33:19
 */


@Data
@NoArgsConstructor
public class FormAccountEditDto implements Serializable {
  
  
  
  private static final long serialVersionUID = -1338761425548722838L;

  private String userId; // userId
  
  private String account; //可以代表email或者phone
  
  private String password;  //此密码是用户输入的 密码，并不是加密密码。

}
