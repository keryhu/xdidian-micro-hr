package com.xdidian.keryhu.user.config;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * 
* @ClassName: CreateDir
* @Description: (创建目录用来，保存user 的 头像 图片。这个目录是在项目工程之外的，)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月30日 下午3:39:29
 */


@Component
public class CreateDir {
  
  @Getter
  @Setter
  private  String userHeader;

  // 文件保存在本地的 目录地址，正式上行，这个需要改
  private final String homePath="/xdidian/static/upload/userHeader";
  
  
  @PostConstruct
  public void init(){
    String usrHome =System.getProperty("user.home");
    
    String dir=new StringBuffer(usrHome).append(homePath).toString();
    File f=new File(dir);
    if(!f.exists()){
      f.mkdirs();  //创建多层目录，包含子目录
    }
    
    setUserHeader(dir);   
  }
 
}
