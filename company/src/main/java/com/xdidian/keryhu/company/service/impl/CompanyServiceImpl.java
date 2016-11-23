package com.xdidian.keryhu.company.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import com.xdidian.keryhu.company.client.UserClient;
import com.xdidian.keryhu.company.config.propertiesConfig.ImageResizeProperties;
import com.xdidian.keryhu.company.domain.*;
import com.xdidian.keryhu.company.domain.address.Address;
import com.xdidian.keryhu.company.domain.company.common.Company;
import com.xdidian.keryhu.company.domain.company.create.NewCompanyDto;
import com.xdidian.keryhu.company.config.propertiesConfig.NewCompanyProperties;
import com.xdidian.keryhu.company.service.AddressService;
import com.xdidian.keryhu.company.service.CompanyService;
import com.xdidian.keryhu.company.service.ConvertUtil;
import com.xdidian.keryhu.domain.Role;
import com.xdidian.keryhu.service.imageService.FileService;
import com.xdidian.keryhu.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.xdidian.keryhu.company.repository.CompanyRepository;
import com.xdidian.keryhu.tree.LinkedMultiTreeNode;
import com.xdidian.keryhu.tree.TreeNode;

import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;


@Component("companyService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties({ImageResizeProperties.class, NewCompanyProperties.class})
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository repository;
    private final UserClient userClient;
    private final AddressService addressService;
    private final ConvertUtil convertUtil;
    private final ImageResizeProperties imageResizeProperties;
    private final NewCompanyProperties newCompanyProperties;
    private final FileService fileService = new FileService();

    /**
     * <p>Title: 根据提供的公司id，查询当前公司的所有的部门,如果这家公司还未设定过  部门，那么就获取默认的
     * 的department</p>
     * <p>Description: </p>
     *
     * @param companyId
     * @return
     *
     * @Override
    public TreeNode<Department> getDepartment(String companyId) {

    boolean defaultDepartment = repository.findById(companyId)
    .map(Company::isDefaultDepartment)
    .orElse(true);

    if (defaultDepartment) {
    return defaultDepartment();
    } else {
    return repository.findById(companyId).map(Company::getDepartment).get();
    }
    }
     *
     *
     */


    // 当新公司注册时候，提交post ，验证信息是否 符合要求。

    /**
     * 其中图片文件的  判断，都在 ImageScaledService 完成
     * 需要验证的信息有：
     * 1  公司名字是否已经存在，如果存在报错
     * 2  公司地址提交的是否符合要求  省份，地级市，县，是否是规定的名字
     * 3  管理员id，是否存在数据库
     * 4  营业执照 ，介绍信是否存在
     * <p>
     * 5 新地点的客服，管理员，不能注册公司。
     */
    @Override
    public void validateNewCompanyPost(NewCompanyDto dto) {
        boolean e = repository.findByName(dto.getName()).isPresent();
        Assert.isTrue(!e, "公司名字已经注册过了！");
        // string address 转address 对象
        Address a=convertUtil.stringToAddress.apply(dto.getAddress());
        addressService.validateAddress(a);
        log.info("adminId is : " + dto.getAdminId() + " ,id is exist: " + userClient.isIdExist(dto.getAdminId()));
        Assert.isTrue(userClient.isIdExist(dto.getAdminId()), "userId不存在");

        Assert.hasText(dto.getFullAddress(), "详细地址不能位空");
        Assert.notNull(dto.getCompanyIndustry(),"公司行业不能位空");
        Assert.notNull(dto.getEnterpriseNature(),"公司性质不能位空");

        Assert.isTrue(fileService.isImage(dto.getBusinessLicense()), "营业执照必需是image格式");
        Assert.isTrue(fileService.isImage(dto.getIntruduction()), "营业执照必需是image格式");

        boolean xdidianService = SecurityUtils
                .getAuthorities()
                .stream()
                .anyMatch(n -> n.equals(Role.ROLE_XDIDIAN_SERVICE.toString()));

        Assert.isTrue(!xdidianService, "新地点人员无权注册公司");

        //如果当前已经注册过公司，且还未审核通过，那么就报错，您已经提交过申请公司,也就是说，
        // 如果帐户里面有未审核过的公司，是不能再次申请公司注册

        boolean hasRegister = repository.findByAdminId(dto.getAdminId())
                .stream()
                .filter(Objects::nonNull)
                .allMatch(Company::isChecked);

        Assert.isTrue(hasRegister, "您已经提交过公司帐户申请，请等待审核！");

        //该用户 注册 公司 数量不能超过 规定的最大数量。
        long newCompanyQuantity = repository.findByAdminId(dto.getAdminId())
                .stream()
                .filter(Objects::nonNull)
                .count();

        String msg = new StringBuffer("最多只能注册")
                .append(newCompanyProperties.getMaxNewCompanyQuantity())
                .append("个公司,您的帐户已经超过限制！")
                .toString();


        Assert.isTrue(newCompanyProperties.getMaxNewCompanyQuantity() >= newCompanyQuantity, msg);

        long maxSize = imageResizeProperties.getMaxSize();
        String m = "上传的文件不能超过 " + maxSize / 1024 + " kb";
        Assert.isTrue(dto.getBusinessLicense().getSize() < maxSize, m);
        Assert.isTrue(dto.getIntruduction().getSize() < maxSize, m);
    }




    /**
     * 获取默认的部门表
     */


    private TreeNode<Department> defaultDepartment() {
        Department d1 = new Department("总经办");
        Office o = new Office("总经理");
        Office o1 = new Office("总监");
        Office o2 = new Office("经理");
        d1.setListOffice(Collections.singletonList(o));
        TreeNode<Department> root = new LinkedMultiTreeNode<Department>(d1);

        Department d2 = new Department("人力资源部");
        d2.setListOffice(Arrays.asList(o1, o2));
        TreeNode<Department> r1 = new LinkedMultiTreeNode<Department>(d2);

        Department d3 = new Department("运营部");
        d3.setListOffice(Arrays.asList(o1, o2));
        TreeNode<Department> r2 = new LinkedMultiTreeNode<Department>(d3);

        root.add(r1);
        root.add(r2);
        return root;
    }

}
