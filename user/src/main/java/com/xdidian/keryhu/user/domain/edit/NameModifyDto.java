package com.xdidian.keryhu.user.domain.edit;

import java.io.Serializable;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
* @ClassName: NameModifyDto
* @Description: TODO(当name修改的时候的dto)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月5日 下午7:45:23
 */


@Data
@NoArgsConstructor
public class NameModifyDto implements Serializable{

  private static final long serialVersionUID = -4350442317185968620L;
  
  private String userId; // userId

  private String name;
  
  private String password;  //此密码是用户输入的 密码，并不是加密密码。
  
  

}
