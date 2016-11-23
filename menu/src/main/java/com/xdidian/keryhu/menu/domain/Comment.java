package com.xdidian.keryhu.menu.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


import lombok.Data;

/**
 * 
* @ClassName: Comment
* @Description: TODO(对于用户提交的内容，上级或主管进行评论，点评的class 模型)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月12日 上午9:38:21
 */

@Data
public class Comment implements Serializable{

  
  private static final long serialVersionUID = 3436423987071262106L;
  
  private String userId;    // 提交点评的人的userID
  
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime commentTime;     //点评的时间
  
  private String content;  // 点评的内容。

}
