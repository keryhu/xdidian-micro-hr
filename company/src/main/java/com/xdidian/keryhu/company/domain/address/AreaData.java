package com.xdidian.keryhu.company.domain.address;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * 
* @ClassName: AreaData
* @Description: 从本地文件   static/area.txt  读取 统计局的  全国县以上行政区划分数据。
* 按照 code 和 name 的格式 读取 ，所以建立了此 class domain 模型)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月15日 下午7:55:28
 */
@Data
@AllArgsConstructor
public class AreaData  implements Serializable{

  private static final long serialVersionUID = 1645868516102309860L;
  
  private String code;
  private String name;

}
