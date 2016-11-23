package com.xdidian.keryhu.menu.postTemplate;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


import lombok.Data;


/**
 * 
* @ClassName: RecruitPostTemplate
*用户发布招聘需求的时候，内容模版。
* 
* xx时间内，希望 招聘 xx岗位（可能是自定义的岗位），岗位职责权利 xx个人)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月10日 下午8:26:30
 */

@Data
public class RecruitPostTemplate implements Serializable {

  private static final long serialVersionUID = 3162201417422965540L;
  
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDate expireDate;  //   截止时间内
  
  private int num;     // 希望招聘的此岗位的人数。
  
  // 暂时用  自定义的 string 来使用，因为如果去  套用目前公司存在的岗位，有可能客户提交的是目前没有的岗位，所以暂时这么使用。
  
  private String position ; //  岗位名称。 不使用数组，如果有个岗位，那么就多提交几次。
  
  private String jobResponsibilities;   // 岗位职责
  
  private String jobRequirements ;   // 岗位要求
  
  private String jobRight;  //岗位权利
  
  private String jobSalary; //岗位薪资。
  
 

}
