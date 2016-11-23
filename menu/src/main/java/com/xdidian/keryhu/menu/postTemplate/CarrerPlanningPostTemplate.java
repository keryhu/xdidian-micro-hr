package com.xdidian.keryhu.menu.postTemplate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.xdidian.keryhu.menu.domain.CarrerPlanningResource;

import lombok.Data;

/**
 * 
* @ClassName: CarrerPlanningPostTemplate
* @Description: TODO(职业规划，用户提交的内容模版
* 内容模版是： 在xx时间内，希望得到xx（例如：职位，薪资，旅游，培训，技能，福利，保险，工作环境或其他
* 
* 如果想晋升xx职位，一般情况下，这个职位是目前公司存在的。)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月10日 上午10:02:04
 */


@Data
public class CarrerPlanningPostTemplate implements Serializable{
 
  private static final long serialVersionUID = -8507852349614698101L;
  
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDate expired;  //在xx个时间内
  
  //模版中，用户希望获得的资源，例如职位，薪资，旅游，培训，技能，福利，保险，工作环境或其他
  private List<CarrerPlanningResource> carrerPlanningResources=new ArrayList<CarrerPlanningResource>();
  
  //用户首先选择上面的enum 类型，然后填写上具体的 类型的 细节信息，
  private String resourceInfo;
  
  public CarrerPlanningPostTemplate(){
    
  }
  //用户选择，增加某个职业规划
  public void  addCarrerPlanningResource(CarrerPlanningResource carrerPlanningResource){
    this.carrerPlanningResources.add(carrerPlanningResource);
  }
  
  
  //修改职业规划，删除某个resource
  public void removeCarrerPlanningResource(CarrerPlanningResource carrerPlanningResource){
    this.carrerPlanningResources=this.carrerPlanningResources.stream()
        .filter(e->!e.equals(carrerPlanningResource)).collect(Collectors.toList());
  }
  
  

}
