package com.xdidian.keryhu.menu.domain.target;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @ClassName: TargetType
 * 在员工进行绩效考核，目标管理时，需要提交的目标任务的几种类型， 分为：日/周/月/季/年)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年8月9日 下午5:05:44
 */
public enum TargetType {
  DAY, //  日报
  WEEK, //  周报
  MONTH, // 月报
  SEASON, // 季报
  YEAR; //  年报

  /**
   * 返回中文名字，方便前台显示
   */

  public String getName() {
    TargetType type = this;
    String result = "";
    switch (type) {
      case DAY:
        result = "日报";
        break;
      case WEEK:
        result = "周报";
        break;
      case MONTH:
        result = "月报";
        break;
      case SEASON:
        result = "季报";
        break;
      case YEAR:
        result = "年报";
        break;
      default:
        break;
    }

    return result;
  }


}
