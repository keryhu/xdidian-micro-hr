package com.xdidian.keryhu.company.domain.address;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

/**
 * 
* @ClassName: Address
* @Description: 地址信息 clss)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月15日 下午2:33:46
 */

@Data
public class Address implements Serializable{

  
  private static final long serialVersionUID = 2514623687464311448L;
  
  private String province;         // 省，直辖市
  
  private String city;          //地级市
  
  private String county;         //  县的信息



  

}
