package com.xdidian.keryhu.company.domain;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * 
* @ClassName: Office
* 公司职位，官职的基础类，是一个公司部门的组成单位
* )
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月4日 上午11:19:40
 */


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Office implements Serializable{

  private static final long serialVersionUID = 3436698318736828295L;
  
  private String id;    //职位的id，用来区分不同的职位。
  
  private String name;  //职位对外的名字
  
  private String duty; // 岗位的责任
  
  private String right;  //岗位的权利
  
  private String salary;   //薪资
  
  private String basePay; //基本工资
  
  private String remarks; //备注
  
    
  public Office(String name){
    this.name=name;
    this.id=UUID.randomUUID().toString();
  }
  
  public Office(){
    this.id=UUID.randomUUID().toString();
  }

}
