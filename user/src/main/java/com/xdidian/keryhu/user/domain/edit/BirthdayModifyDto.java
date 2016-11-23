package com.xdidian.keryhu.user.domain.edit;

import java.io.Serializable;
import java.time.LocalDate;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
* @ClassName: NameModifyDto
* @Description: 当生日修改的时候的dto
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月5日 下午7:45:23
 */


@Data
@NoArgsConstructor
public class BirthdayModifyDto implements Serializable{

  private static final long serialVersionUID = -2280886868429275060L;

  private String userId; // userId

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDate birthday;
  
  private String password;  //此密码是用户输入的 密码，并不是加密密码。
  
  

}
