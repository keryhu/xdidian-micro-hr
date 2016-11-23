package com.xdidian.keryhu.menu.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
* @ClassName: DraftAuthor
* @Description: 内容的制订者，是属于谁。
* 一般情况下，分为： 自己制定，直接上级制定，人力部制定。)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月10日 上午11:14:21
 */
public enum DraftAuthor {
  SELF,    // 自己制定
  LEADER,  // 直接领导
  HR;      //  人力资源部


  
  public String getName(){
    DraftAuthor au=this;
    String result="";
    switch(au){
      case SELF:
        result="自身制定";
        break;
      case LEADER:
        result="上级制定";
        break;
      case HR:
        result="人力部制定";
        break;
      default:
        break;
      
    }
    return result;
  }

}
