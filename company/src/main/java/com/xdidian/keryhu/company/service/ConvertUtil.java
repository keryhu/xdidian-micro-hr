package com.xdidian.keryhu.company.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;


import com.xdidian.keryhu.company.domain.address.Address;
import com.xdidian.keryhu.company.domain.company.check.CheckCompanyInfoForRead;
import com.xdidian.keryhu.company.domain.company.check.CompanySignupItems;
import com.xdidian.keryhu.company.domain.company.check.Reject;
import com.xdidian.keryhu.company.domain.company.common.CheckCompanyByteItem;
import com.xdidian.keryhu.company.domain.company.common.CheckCompanyStringItem;
import com.xdidian.keryhu.company.domain.company.common.Company;
import com.xdidian.keryhu.company.domain.company.component.CompanyIndustry;
import com.xdidian.keryhu.company.domain.company.component.EnterpriseNature;
import com.xdidian.keryhu.company.domain.company.create.NewCompanyDto;
import com.xdidian.keryhu.service.imageService.FileService;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Component
@Slf4j
public class ConvertUtil {


    private final FileService fileService = new FileService();


    public Function<NewCompanyDto, Company> newCompanyDtoToCompany =
            x -> {
                Company company = new Company();
                company.setName(x.getName());
                company.setAdminId(x.getAdminId());
                Address a = this.stringToAddress.apply(x.getAddress());
                company.setAddress(a);
                company.setFullAddress(x.getFullAddress());
                company.setRegisterTime(LocalDateTime.now());
                company.setCompanyIndustry(x.getCompanyIndustry());
                company.setEnterpriseNature(x.getEnterpriseNature());
                return company;
            };


    // 将company 对象转为 CheckCompanyInfoForRead，方便前台会员注册完公司，查看已经注册了的公司信息，
    // 新地点的工作人员，审核公司资料的时候，查看公司信息
    public Function<Company, CheckCompanyInfoForRead> companyToCheckCompanyInfoForRead =
            x -> {
                Set<Reject> rejects = x.getRejects();
                CheckCompanyInfoForRead c = new CheckCompanyInfoForRead();
                CheckCompanyStringItem name = new CheckCompanyStringItem();
                name.setValue(x.getName());
                //如果name有错误，
                addRejectsToCheckCompanyStringItem(rejects, CompanySignupItems.NAME, name);
                c.setName(name);


                CheckCompanyStringItem address = new CheckCompanyStringItem();
                String a = this.addressToString.apply(x.getAddress());
                address.setValue(a);
                addRejectsToCheckCompanyStringItem(rejects,
                        CompanySignupItems.ADDRESS, address);
                c.setAddress(address);

                CheckCompanyStringItem fullAddress = new CheckCompanyStringItem();
                fullAddress.setValue(x.getFullAddress());
                addRejectsToCheckCompanyStringItem(rejects,
                        CompanySignupItems.FULLADDRESS, fullAddress);
                c.setFullAddress(fullAddress);

                CheckCompanyStringItem companyIndustry = new CheckCompanyStringItem();
                companyIndustry.setValue(x.getCompanyIndustry().toValue());
                addRejectsToCheckCompanyStringItem(rejects,
                        CompanySignupItems.COMPANY_INDUSTRY, companyIndustry);
                c.setCompanyIndustry(companyIndustry);

                CheckCompanyStringItem enterpriseNature = new CheckCompanyStringItem();
                enterpriseNature.setValue(x.getEnterpriseNature().toValue());
                addRejectsToCheckCompanyStringItem(rejects,
                        CompanySignupItems.ENTERPRISE_NATURE, enterpriseNature);
                c.setEnterpriseNature(enterpriseNature);

                // 将  image path 转为 base64， 格式还是原来的图片格式

                byte[] bb = fileService.filePathToOriginalByte(x.getBusinessLicensePath());
                byte[] bi = fileService.filePathToOriginalByte(x.getIntruductionPath());

                CheckCompanyByteItem businessLicense = new CheckCompanyByteItem();
                businessLicense.setValue(bb);
                addRejectsToCheckCompanyByteItem(rejects,
                        CompanySignupItems.BUSINESS_LICENSE, businessLicense);
                c.setBusinessLicense(businessLicense);

                CheckCompanyByteItem intruduction = new CheckCompanyByteItem();
                intruduction.setValue(bi);
                addRejectsToCheckCompanyByteItem(rejects,
                        CompanySignupItems.INSTRUDUCTION, intruduction);
                c.setIntruduction(intruduction);

                //获取img 的图片格式
                c.setBusinessLicenseType(fileService.getTypeFromImgPath(x.getBusinessLicensePath()));
                c.setIntruductionType(fileService.getTypeFromImgPath(x.getIntruductionPath()));

                return c;
            };

    // string "省 ， 地级市， 县" 转 address 对象，
    public Function<String, Address> stringToAddress =
            x -> {
                Address address = new Address();
                String[] m = x.split(",");
                address.setProvince(m[0]);
                address.setCity(m[1]);
                address.setCounty(m[2]);
                return address;
            };


    //  address 对象 转 string "省 地级市 县" ，这个必需要，因为这里
    //  companyToCheckCompanyInfoForRead need it
    public Function<Address, String> addressToString =
            x -> {
                Address address = new Address();
                return new StringBuffer(x.getProvince())
                        .append(" ")
                        .append(x.getCity())
                        .append(" ")
                        .append(x.getCounty())
                        .toString();
            };


    /**
     * reject 存在的情况下，将reject 保存到CheckCompanyStringItem 里面
     * rejects  list 对象
     *
     * @param item 公司注册可选的item
     * @param ccsi CheckCompanyStringItem 审核公司的最基本的string对象，应用到前台
     */
    private void addRejectsToCheckCompanyStringItem(
            Set<Reject> rejects, CompanySignupItems item, CheckCompanyStringItem ccsi) {

        if (rejects != null && rejects.stream().
                anyMatch(e -> e.getItem().equals(item))) {

            ccsi.setReadWrite(1);
            String msg = rejects.stream().
                    filter(e -> e.getItem().equals(item))
                    .map(Reject::getMessage)
                    .findFirst()
                    .orElse(null);
            ccsi.setRejectMsg(msg);
        }
    }

    /**
     * reject 存在的情况下，将reject 保存到CheckCompanyByteItem 里面
     * rejects  list 对象
     *
     * @param item 公司注册可选的item
     * @param ccbi CheckCompanyByteItem 审核公司的最基本的byte对象，应用到前台
     */
    private void addRejectsToCheckCompanyByteItem(
            Set<Reject> rejects, CompanySignupItems item, CheckCompanyByteItem ccbi) {

        if (rejects != null && rejects.stream().
                anyMatch(e -> e.getItem().equals(item))) {

            ccbi.setReadWrite(1);
            String msg = rejects.stream().
                    filter(e -> e.getItem().equals(item))
                    .map(Reject::getMessage)
                    .findFirst()
                    .orElse(null);
            ccbi.setRejectMsg(msg);
        }
    }


}
