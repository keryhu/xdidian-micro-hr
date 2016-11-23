package com.xdidian.keryhu.menu.postTemplate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xdidian.keryhu.menu.domain.ReleaseType;

import lombok.Data;

/**
 * 
* @ClassName: ReleasePostTemplate
* @Description: TODO(发布管理的内容模板
* 通常的模版是：
* 
* 选择发送 类型（会议／公告／体育活动   然后是具体的内容。)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月10日 下午3:30:28
 */


@Data
public class ReleasePostTemplate implements Serializable {

  private static final long serialVersionUID = 5440784906982980015L;

  private ReleaseType releaseType;
  
  //如果选择的是会议，那么还必须设置，会议的地点，参加的人员。
  
  private String address ;// 地址。
  
  //参加人员的userId；
  private List<String>  participantUserId=new ArrayList<String>();
  
  //参加的时间点
  @DateTimeFormat(iso = ISO.DATE_TIME)


  private LocalDateTime participantTime;  // 
  
  //发布的主要内容，（例如发布的公告，定义的内容。
  private String content;
  
  
}
