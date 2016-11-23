package com.xdidian.keryhu.company.domain.company.create;

import com.xdidian.keryhu.company.domain.address.AreaData;
import com.xdidian.keryhu.company.domain.company.component.CompanyIndustry;
import com.xdidian.keryhu.company.domain.company.component.EnterpriseNature;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 当打开  新建 公司帐户 页面的时候，，首先需要 加载 3个信息数据
 * 1  所有的 省市，直辖市的 数据
 * 2  所有的公司行业数据
 * 3  所有的公司性质数据
 *
 */


@Data
@NoArgsConstructor
public class NewCompanyResolveInfo implements Serializable {

    // 这里需要排序，所以使用list。而不是set
    private List<CompanyIndustry> companyIndustries;   //所有的 公司行业 名字的 list

    // 这里需要排序，所以使用list。而不是set
    private List<EnterpriseNature>  enterpriseNatures;   //所有的 公司性质  名字的  list

    //当新建公司帐户的时候，出现的错误信息，（例如一个用户新建数量超过了规定的最大数量）
    private String newCompanyErrMsg;

}
