package com.xdidian.keryhu.company.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * 
* @ClassName: CreateDir
* @Description: 创建目录用来，保存 公司营业执照 和 公司管理人员的介绍信 图片。
 *   这个目录是在项目工程之外的，)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月30日 下午3:39:29
 *
 *
 * 这里只负责创建 一个  总的公用文件夹，companyInfo，用来 保存营业执照和 公司介绍信。
 *
 * 接下来，具体保存的时候，先 根据companyId，创建子目录。然后将 营业执照和 介绍信保存在 这个companyId下。
 * 营业执照的取名  英文－营业执照＋当前时间        介绍信 adminId，＋当前时间
 */


@Component
public class CreateDir {

  @Getter
  @Setter
  private String companyInfo;

  private final String defaultDir="/xdidian/static/company";
  
  
  @PostConstruct
  public void init(){
    String usrHome =System.getProperty("user.home");
    
    String companyInfoDir=new StringBuffer(usrHome)
            .append(defaultDir).toString();


    File cf=new File(companyInfoDir);
    if(!cf.exists()){
      cf.mkdirs();  //创建多层目录，包含子目录
    }

    setCompanyInfo(companyInfoDir);

  }
 
}
