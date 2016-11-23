package com.xdidian.keryhu.company.domain.company.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import com.querydsl.core.annotations.QueryEntity;
import com.xdidian.keryhu.company.domain.address.Address;
import com.xdidian.keryhu.company.domain.company.check.Reject;
import com.xdidian.keryhu.company.domain.company.component.CompanyIndustry;
import com.xdidian.keryhu.company.domain.company.component.EnterpriseNature;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


import lombok.Data;

/**
 * 
 * @ClassName: CompanyInfo
 * @Description: 员工所在公司的基本信息，包含公司名字，地址，客户平台管理员，组织机构图，营业执照图片 y因为使用的是自定义的company，没有相应的转换配置，所以不需要
 *               spring-data-rest 依赖)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年7月28日 下午1:02:50
 */

@Data
@Document
@QueryEntity
public class Company implements Serializable {

  private static final long serialVersionUID = -1554578596908142213L;

  @Id
  private String id; // 本身的id，用来区分不同的company,数据库中id，也是companyId
  private String name;    //公司名字
  private Address address;      // 省份，地级市，县的组合
  private String fullAddress;   //详细地址
  private String adminId; // 平台，客户管理员 的 id   yi个用户最多能主动注册5家公司。

  //此公司 有没有审核通过，在公司刚刚注册完成的时候，需要 新地点工作人员 对 公司资质
  // （提供的营业执照，公司名字，介绍信 等是否 工商存在 默认是false，只有 新地点审核后，通过了，才为true
  private boolean checked;

  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime registerTime; // 公司注册时间


  @DateTimeFormat(iso = ISO.DATE_TIME)
  private LocalDateTime checkedTime; // 资料被审核的时间


  //@JsonSerialize(using = TreeDepartmentSerializer.class)
 // @JsonDeserialize(using = TreeDepartmentDeserializer.class)
  //private TreeNode<Department> department; // 公司下面的部门

  //private boolean defaultDepartment; // 是否使用默认的部门。

  private String businessLicensePath; // 营业执照的图片,存储的路径（本地）

  private String intruductionPath; // 介绍信的图片 存储的路径（本地）

  private CompanyIndustry companyIndustry; //公司行业

  private EnterpriseNature enterpriseNature; // 公司性质。

  // 审核被拒绝的理由。如果有新的拒绝理由，应该是add，而不是覆盖原来的，这样还可以查看到 以前这个公司的拒绝的理由。
  private Set<Reject> rejects=new HashSet<>();

  public Company() {
    this.id = UUID.randomUUID().toString();
    this.name = null;
    this.address = null;
    this.adminId = null;
    this.checked=false;
    this.registerTime = null;
    //this.department = null;
   // this.defaultDepartment = true;
    this.businessLicensePath=null;
    this.intruductionPath=null;
    this.companyIndustry=null;
    this.enterpriseNature=null;
  }





}
