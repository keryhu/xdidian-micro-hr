package com.xdidian.keryhu.company.domain.company.check;

/**
 * Created by hushuming on 2016/11/1.
 *
 * 公司注册时候，具体需要的条目，这些条目是有限的，所以现在定义为enum
 *
 * 还有一个目的，就是方便新地点的工作人员审核公司的时候，一一比对这些已有的条目，然后为这些
 * 条目增加拒绝的理由（如果是拒绝申请了这家公司）
 */
public enum CompanySignupItems {
    NAME,                   //  公司名字
    ADDRESS,                // 包含省份，市县的组合（不再细分省，市了），如果不符合，直接要求他重填整个
    FULLADDRESS,            //  详细地址
    COMPANY_INDUSTRY,       //   公司行业
    //enterpriseNature
    ENTERPRISE_NATURE,       //   企业性质
    //businessLicense
    BUSINESS_LICENSE,        //  营业执照
    //intruduction
    INSTRUDUCTION;             //  介绍信
}
