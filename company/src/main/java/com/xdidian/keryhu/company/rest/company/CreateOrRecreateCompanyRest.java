package com.xdidian.keryhu.company.rest.company;

import com.xdidian.keryhu.company.config.CreateDir;
import com.xdidian.keryhu.company.config.propertiesConfig.ImageResizeProperties;
import com.xdidian.keryhu.company.domain.address.Address;
import com.xdidian.keryhu.company.domain.company.check.CompanySignupItems;
import com.xdidian.keryhu.company.domain.company.check.Reject;
import com.xdidian.keryhu.company.domain.company.common.Company;
import com.xdidian.keryhu.company.domain.company.create.NewCompanyDto;
import com.xdidian.keryhu.company.repository.CompanyRepository;
import com.xdidian.keryhu.company.service.AddressService;
import com.xdidian.keryhu.company.service.CompanyService;
import com.xdidian.keryhu.company.service.ConvertUtil;
import com.xdidian.keryhu.company.stream.WebsocketAndMessageProducer;
import com.xdidian.keryhu.domain.message.MessageCommunicateDto;
import com.xdidian.keryhu.domain.message.Operate;
import com.xdidian.keryhu.domain.message.ReadGroup;
import com.xdidian.keryhu.domain.message.Subject;
import com.xdidian.keryhu.service.imageService.FileService;
import com.xdidian.keryhu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Created by hushuming on 2016/11/8.
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@EnableConfigurationProperties({ImageResizeProperties.class})
public class CreateOrRecreateCompanyRest {

    private final CompanyService companyService;
    private final CreateDir createDir;
    private final ConvertUtil convertUtil;
    private final CompanyRepository repository;
    private final WebsocketAndMessageProducer producer;
    private final AddressService addressService;
    private final ImageResizeProperties imageResizeProperties;
    private final FileService fileService = new FileService();

    /**
     * 用户登录后，如果还没有公司的情况下，如果想创建公司，那么此就是一个提交的的rest
     * 第一步创建公司，需要提交的信息为： 公司名字，公司地址，公司管理员的userId，默认是当前user，公司营业执照图片)
     * <p>
     * 需要验证的信息有：
     * 1  公司名字是否已经存在，如果存在报错
     * 2  公司地址提交的是否符合要求   省份，地级市，县，是否是规定的名字
     * 3  管理员id，是否存在数据库
     * 4  营业执照 ，介绍信是否存在
     * 5  公司性质，公司行业不能位空
     * <p>
     * ## 营业执照和介绍信，不执行resize，只限制 最大不能超过500kb，原来是什么格式，还是保存成什么格式。例如
     * jpeg，还是保存为jpeg， png还是png
     * <p>
     * 6  如果注册成功后， 且审核通过。要发送  此userId 是公司管理员的 信息给  userAccount ，让userAccount更新权限。
     */


    @PostMapping("/company/createCompany")
    public ResponseEntity<?> newCompany(
            @RequestPart("body") final NewCompanyDto dto,
            @RequestPart("businessLicense") MultipartFile businessLicense,
            @RequestPart("intruduction") MultipartFile intruduction) throws IOException {


        Assert.notNull(businessLicense, "营业执照不能为空");
        Assert.notNull(intruduction, "介绍信不能为空");

        String businessType = businessLicense.getContentType().split("/")[1];   //获取上传文件的格式
        String instruductionType = intruduction.getContentType().split("/")[1];  // 获取上传文件的格式
        dto.setBusinessLicense(businessLicense);
        dto.setIntruduction(intruduction);

        companyService.validateNewCompanyPost(dto);

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        log.info("新建公司，提交的注册信息，已经验证验证成功，提交的信息为： " + dto.toString());


        Company company = convertUtil.newCompanyDtoToCompany.apply(dto);

        String businessLicenseDir = new StringBuffer(createDir.getCompanyInfo())
                .append("/")
                .append(company.getId())
                .append("/businessLicense")
                .toString();

        //介绍信图片保存的 文件夹
        String intruductionDir = new StringBuffer(createDir.getCompanyInfo())
                .append("/")
                .append(company.getId())
                .append("/intruduction")
                .toString();

        File bf = new File(businessLicenseDir);
        if (!bf.exists()) {
            bf.mkdirs();  //创建多层目录，包含子目录
        }

        File inf = new File(intruductionDir);
        if (!inf.exists()) {
            inf.mkdirs();  //创建多层目录，包含子目录
        }

        // 设置各自的 文件全名，包含了  png名字
        String businessLicenseImgPath = new StringBuffer(businessLicenseDir)
                .append("/")
                .append(System.currentTimeMillis())
                .append(".")
                .append(businessType)
                .toString();


        String intruductionImgPath = new StringBuffer(intruductionDir)
                .append("/")
                .append(dto.getAdminId())
                .append(".")
                .append(instruductionType)
                .toString();

        BufferedImage businessSource = ImageIO.read(businessLicense.getInputStream());
        BufferedImage intruductionSource = ImageIO.read(intruduction.getInputStream());

        //保存到本地
        ImageIO.write(businessSource, businessType, new File(businessLicenseImgPath));
        ImageIO.write(intruductionSource, instruductionType, new File(intruductionImgPath));


        company.setBusinessLicensePath(businessLicenseImgPath);
        company.setIntruductionPath(intruductionImgPath);

        repository.save(company);

        map.put("result", true);

        //发送message给websocket app
        MessageCommunicateDto messageCommunicateDto=new MessageCommunicateDto();
        messageCommunicateDto.setSubject(Subject.NEW_COMPANY);
        messageCommunicateDto.setOperate(Operate.ADD);
        messageCommunicateDto.setReadGroup(ReadGroup.XDIDIAN);
        producer.send(messageCommunicateDto);

        return ResponseEntity.ok(map);
    }


    /**
     * 当新公司的申请人，申请公司后，被拒绝，申请人打开提交材料，准备修改后，再次提交的rest
     * <p>
     * 验证提交参数的 具体方法：
     * 检查reject，是否有该参数，如果有，那么检查提交的参数是否有此参数，且验证是否符合要求。
     * 新地点对于 注册公司已经过几次申请，已经之前被拒绝的理由不做保存数据库，
     * 当用户编辑修改后，再次提交，则覆盖原来的申请信息，原来的被拒绝的信息也将被清除
     *
     * @param dto
     * @param businessLicense
     * @param intruduction
     * @return
     */
    @PostMapping("/company/createCompanyAfterReject")
    public ResponseEntity<?> updateCreateCompanyInfo(
            @RequestPart(value = "body", required = false) final NewCompanyDto dto,
            @RequestPart(value = "businessLicense", required = false) MultipartFile businessLicense,
            @RequestPart(value = "intruduction", required = false) MultipartFile intruduction
    ) throws IOException {
        String adminId = SecurityUtils.getCurrentLogin();
        repository.findByAdminId(adminId).stream()
                .filter(e -> !e.isChecked())
                .filter(e -> e.getRejects() != null && !e.getRejects().isEmpty())
                .findFirst()
                .ifPresent(e -> {
                    Set<Reject> rejects = e.getRejects();
                    long maxSize = imageResizeProperties.getMaxSize();
                    String m = "上传的文件不能超过 " + maxSize / 1024 + " kb";
                    boolean nameInvalid = rejects.stream()
                            .anyMatch(q -> q.getItem().equals(CompanySignupItems.NAME));
                    boolean addressInvalid = rejects.stream()
                            .anyMatch(q -> q.getItem().equals(CompanySignupItems.ADDRESS));
                    boolean fullAddressInvalid = rejects.stream()
                            .anyMatch(q -> q.getItem().equals(CompanySignupItems.FULLADDRESS));
                    boolean companyIndustryInvalid = rejects.stream()
                            .anyMatch(q -> q.getItem().equals(CompanySignupItems.COMPANY_INDUSTRY));
                    boolean enterpriseNatureInvalid = rejects.stream()
                            .anyMatch(q -> q.getItem().equals(CompanySignupItems.ENTERPRISE_NATURE));
                    boolean businessLicenseInvalid = rejects.stream()
                            .anyMatch(q -> q.getItem().equals(CompanySignupItems.BUSINESS_LICENSE));
                    boolean intruductionInvalid = rejects.stream()
                            .anyMatch(q -> q.getItem().equals(CompanySignupItems.INSTRUDUCTION));

                    if (nameInvalid) {
                        Assert.notNull(dto, "公司姓名不能为空");
                        Assert.hasText(dto.getName(), "公司姓名不能为空");
                        boolean w = repository.findByName(dto.getName()).isPresent();
                        Assert.isTrue(!w, "公司名字已经注册过了！");
                        boolean cannotSame=!dto.getName().equals(e.getName());
                        Assert.isTrue(cannotSame, "公司名字未更正！");
                        e.setName(dto.getName());
                    }

                    if (addressInvalid) {
                        Assert.notNull(dto, "公司所在地不能为空");
                        Assert.notNull(dto.getAddress(), "公司所在地不能为空");
                        Address a = convertUtil.stringToAddress.apply(dto.getAddress());
                        addressService.validateAddress(a);
                        boolean cannotSame=!dto.getAddress().
                                equals(convertUtil.addressToString.apply(e.getAddress()));
                        Assert.isTrue(cannotSame, "公司所在地未更正！");
                        e.setAddress(a);
                    }
                    if (fullAddressInvalid) {
                        Assert.notNull(dto, "公司详细地址不能为空");
                        Assert.hasText(dto.getFullAddress(), "详细地址不能位空");
                        boolean cannotSame=!dto.getFullAddress()
                        .equals(e.getFullAddress());
                        Assert.isTrue(cannotSame, "公司详细地址未更正！");
                        e.setFullAddress(dto.getFullAddress());
                    }
                    if (companyIndustryInvalid) {
                        Assert.notNull(dto, "公司行业不能为空");
                        Assert.notNull(dto.getCompanyIndustry(), "公司行业错误");

                        boolean cannotSame=!dto.getCompanyIndustry()
                                .equals(e.getCompanyIndustry());
                        Assert.isTrue(cannotSame, "公司行业未更正！");
                        e.setCompanyIndustry(dto.getCompanyIndustry());
                    }
                    if (enterpriseNatureInvalid) {
                        Assert.notNull(dto, "企业性质不能为空");
                        Assert.notNull(dto.getEnterpriseNature(), "公司性质不能位空");

                        boolean cannotSame=!dto.getEnterpriseNature()
                                .equals(e.getEnterpriseNature());
                        Assert.isTrue(cannotSame, "公司性质未更正！");
                        e.setEnterpriseNature(dto.getEnterpriseNature());
                    }


                    if (businessLicenseInvalid) {
                        Assert.notNull(dto, "营业执照不能为空");
                        String businessType = businessLicense
                                .getContentType().split("/")[1];   //获取上传文件的格式
                        Assert.isTrue(fileService
                                .isImage(businessLicense), "营业执照必需是image格式");
                        Assert.isTrue(businessLicense.getSize() < maxSize, m);

                        String businessLicenseDir = new StringBuffer(createDir.getCompanyInfo())
                                .append("/")
                                .append(e.getId())
                                .append("/businessLicense")
                                .toString();
                        File bf = new File(businessLicenseDir);
                        if (!bf.exists()) {
                            bf.mkdirs();  //创建多层目录，包含子目录
                        }

                        // 设置各自的 文件全名，包含了  png名字
                        String businessLicenseImgPath = new StringBuffer(businessLicenseDir)
                                .append("/")
                                .append(System.currentTimeMillis())
                                .append(".")
                                .append(businessType)
                                .toString();

                        BufferedImage businessSource = null;
                        try {
                            businessSource = ImageIO
                                    .read(businessLicense.getInputStream());
                            //保存到本地
                            ImageIO.write(businessSource, businessType, new File(businessLicenseImgPath));

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        e.setBusinessLicensePath(businessLicenseImgPath);

                    }
                    if (intruductionInvalid) {
                        Assert.notNull(dto, "介绍信不能为空");
                        String instruductionType = intruduction
                                .getContentType().split("/")[1];  // 获取上传文件的格式
                        Assert.isTrue(fileService
                                .isImage(intruduction), "营业执照必需是image格式");
                        Assert.isTrue(intruduction.getSize() < maxSize, m);
                        //介绍信图片保存的 文件夹
                        String intruductionDir = new StringBuffer(createDir.getCompanyInfo())
                                .append("/")
                                .append(e.getId())
                                .append("/intruduction")
                                .toString();
                        File inf = new File(intruductionDir);
                        if (!inf.exists()) {
                            inf.mkdirs();  //创建多层目录，包含子目录
                        }
                        String intruductionImgPath = new StringBuffer(intruductionDir)
                                .append("/")
                                .append(dto.getAdminId())
                                .append(".")
                                .append(instruductionType)
                                .toString();
                        BufferedImage intruductionSource = null;
                        try {
                            intruductionSource = ImageIO
                                    .read(intruduction.getInputStream());
                            //保存到本地
                            ImageIO.write(intruductionSource,
                                    instruductionType, new File(intruductionImgPath));

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        e.setIntruductionPath(intruductionImgPath);

                    }
                    e.setRegisterTime(LocalDateTime.now());
                    log.info("新建公司注册信息，修改后，提交验证成功。。。");
                    //清空所有的rejects
                    e.getRejects().clear();
                    repository.save(e);
                });

        // 需要发送message出去，websocket 通知，前台去审核公司
        MessageCommunicateDto messageCommunicateDto=new MessageCommunicateDto();
        messageCommunicateDto.setSubject(Subject.NEW_COMPANY);
        messageCommunicateDto.setOperate(Operate.ADD);
        messageCommunicateDto.setReadGroup(ReadGroup.XDIDIAN);
        producer.send(messageCommunicateDto);
        Map<String, Boolean> map = new HashMap<>();
        map.put("result", true);
        return ResponseEntity.ok(map
        );
    }


}
