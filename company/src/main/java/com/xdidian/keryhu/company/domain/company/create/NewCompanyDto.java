package com.xdidian.keryhu.company.domain.company.create;

import java.io.Serializable;

import com.xdidian.keryhu.company.domain.address.Address;
import com.xdidian.keryhu.company.domain.company.component.CompanyIndustry;
import com.xdidian.keryhu.company.domain.company.component.EnterpriseNature;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
* @ClassName: NewCompanyDto
* @Description: 公司第一步注册的时候，需要提交的信息。
* 公司名字，公司地址，公司管理员的userId，默认是当前user，公司营业执照图片)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月16日 下午12:06:11
 */

@Data
public class NewCompanyDto implements Serializable{

  
  private static final long serialVersionUID = 3763838704029508919L;
  
  private String name;   //公司名字
  // 使用 string 而不是 address对象，因为在新建的时候，@RequestPart不能json deserializer address
  // 这里的string 格式是 ： "省份，地级市，县"
  private String address;   //包含省份,地级市，县的address  省份，地级市，县的 string
  private String fullAddress;   // 自定义的address 全地址。
  private String adminId; // 平台，客户管理员 的 id

  private CompanyIndustry companyIndustry;    //公司行业

  private EnterpriseNature enterpriseNature;   // 企业性质
  
  private MultipartFile businessLicense;  //营业执照的图片
  
  private MultipartFile intruduction;     //介绍信的图片



}
