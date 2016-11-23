package com.xdidian.keryhu.company.domain.company.check;

import com.xdidian.keryhu.company.domain.company.common.CheckCompanyByteItem;
import com.xdidian.keryhu.company.domain.company.common.CheckCompanyStringItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by hushuming on 2016/11/6.
 *
 * 此class ，是为了方便，前台会员注册完公司，查看已经注册了的公司信息，
 * 新地点的工作人员，审核公司资料的时候，查看公司信息。
 * 还有申请人事后，查看被拒绝的提交材料
 * 这些属性对象，有3种，
 * CheckCompanyStringItem value is string
 * CheckCompanyByteItem  value is byte【】
 * 还有就是string
 */

@Data
@NoArgsConstructor
public class CheckCompanyInfoForRead implements Serializable {

    private CheckCompanyStringItem name;       //公司名字
    private CheckCompanyStringItem address;   //包含省份,地级市，县的address
    private CheckCompanyStringItem fullAddress;   // 自定义的address 全地址。

    private CheckCompanyStringItem companyIndustry;    //公司行业
    private CheckCompanyStringItem enterpriseNature;   // 企业性质
    private CheckCompanyByteItem businessLicense;  //营业执照的图片
    private CheckCompanyByteItem intruduction;     //介绍信的图片

    private String businessLicenseType;      // 营业执照的图片格式
    private String intruductionType;    //介绍信的图片格式


}
