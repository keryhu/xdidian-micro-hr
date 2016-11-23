package com.xdidian.keryhu.company.service;

import com.xdidian.keryhu.company.domain.company.create.NewCompanyDto;
import com.xdidian.keryhu.tree.TreeNode;

import java.util.List;

public interface CompanyService {

    //public TreeNode<Department> getDepartment(String companyId);

    // 当新公司注册时候，提交post ，验证信息是否 符合要求。
     void validateNewCompanyPost(final NewCompanyDto dto);



}
