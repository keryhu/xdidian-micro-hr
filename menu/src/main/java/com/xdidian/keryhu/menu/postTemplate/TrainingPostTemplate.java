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
import com.xdidian.keryhu.menu.domain.DraftAuthor;

import lombok.Data;

/**
 * 
 * @ClassName: TrainingPostTemplate
 * @Description: TODO(发起培训，，内容模版 人力部或上级发布： xx时间 xx培训，参加人员——关键字：持续多久 时间，培训内容
 *               参加xx人员，参加地点，培训所需的时间，培训的目的，)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年8月10日 下午3:58:52
 */

@Data
public class TrainingPostTemplate implements Serializable {

  private static final long serialVersionUID = 7204116248087311269L;

  @DateTimeFormat(iso = ISO.DATE_TIME)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)

  private LocalDateTime TrainingTime; // 培训时间


  private float hoursOfLast; // 培训持续多久，单位：小时

  // 参加培训的内容
  private String content; // 培训的内容。

  // 参加培训的人员。
  private List<String> participantUserId = new ArrayList<String>();

  // 参加培训地址
  private String address;// 地址。
  
  //培训的目的
  private String trainingTarget ; 
  
  //发起人
  private DraftAuthor draftAuthor; 



}
