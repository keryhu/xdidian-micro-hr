package com.xdidian.keryhu.menu.postTemplate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.xdidian.keryhu.menu.domain.target.TargetType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


import com.xdidian.keryhu.menu.domain.DraftAuthor;

import lombok.Data;

/**
 * 
* @ClassName: PerformacePostTemplate
* @Description: TODO(绩效考核时，自己提交的 日／周／月／季／年 报
* 这个class 可以用在3个地方
* 1  用户自身提交自己的 绩效考核的目标
* 2  上级人员对于下属管理时，，给下属制定的绩效考核的（在制订者里面选择 author 为leader 即可
* 3  人力部门进行  人才管理的时候，对不同的员工，设定的 绩效考核的目标。当然人力部门也可以不设定这个，可有可无)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月10日 上午11:31:26
 */

@Data
public class PerformacePostTemplate implements Serializable{

  /**
  * @Fields serialVersionUID :
  */ 
  private static final long serialVersionUID = -8795998879137689373L;
  
  private DraftAuthor author;     //制作者 时自己提交的，还是领导提交的，还是人力资源部提交的。
  
  private TargetType targetType;  // 用户制作的日报／周报的，具体是哪种类型。
  
  
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDate startDate;    // 日报／周报／月报  开始时间
  
  
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDate expireDate;  //   日报／周报／月报  结束时间
  
  private float completionRate;    //完成比例    1-100的数字，可以有小数  到时候前台将这个浮点类型专为 百分比即可
  
  //在日报／周报／年报  等中用户填写的具体内容。
  private List<String> content=new ArrayList<String>();
  
  //用户对自己上一个周报，日报，年报，等完成情况进行 描述。
  private String completionDescription; 

}
