package com.xdidian.keryhu.company.rest.company;

import com.xdidian.keryhu.company.config.propertiesConfig.NewCompanyProperties;
import com.xdidian.keryhu.company.domain.company.check.CheckCompanyInfoForRead;
import com.xdidian.keryhu.company.domain.company.component.CompanyIndustry;
import com.xdidian.keryhu.company.domain.company.component.EnterpriseNature;
import com.xdidian.keryhu.company.domain.company.create.NewCompanyResolveInfo;
import com.xdidian.keryhu.company.repository.CompanyRepository;
import com.xdidian.keryhu.company.service.ConvertUtil;
import com.xdidian.keryhu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@EnableConfigurationProperties(NewCompanyProperties.class)
public class CompanyRest {

    private final CompanyRepository repository;
    private final ConvertUtil convertUtil;
    private final NewCompanyProperties newCompanyProperties;


    /**
     * 公司注册时候，查看公司名字是否已经注册过，还有申请加入公司的时候，查看公司名字是否存在
     *
     * @param name
     * @return
     */

    @GetMapping("/company/findCompanyExistByName")
    public ResponseEntity<?> findCompanyExistByName(@RequestParam("name") String name) {

        boolean e = repository.findByName(name).isPresent();
        return ResponseEntity.ok(e);
    }


    /**
     * 当打开  新建 公司帐户 页面的时候，，首先需要 加载 2个信息数据
     * 1  所有的公司行业数据
     * 2  所有的公司性质数据
     * <p>
     * 或者新地点的工作人员 审核公司的时候，需要首先 获取该companyId，之前有没有注册过，又没被拒绝过。
     * <p>
     * 如果 参数中，传递了companyId，那说明是新地点的工作人员来之前的。，需要获取该公司的基础信息，
     * 有没有被注册过。
     *
     * @return
     */

    @GetMapping("/company/getCheckCompanyCommonInfo")
    public ResponseEntity<?> getCheckCompanyCommonInfo(
            @RequestParam(value = "companyId", required = false) String companyId
    ) {

        NewCompanyResolveInfo info = new NewCompanyResolveInfo();


        // 加入所有的 行业。

        List<CompanyIndustry> cc=new ArrayList<>();

        Collections.addAll(cc, CompanyIndustry.values());
        info.setCompanyIndustries(cc);

        // 加入所有 的 公司性质数据
        List<EnterpriseNature> ee = new ArrayList<EnterpriseNature>();

        Collections.addAll(ee, EnterpriseNature.values());

        info.setEnterpriseNatures(ee);
        String adminId = null;

        if (companyId == null) {
            adminId = SecurityUtils.getCurrentLogin();
        } else {
            adminId = companyId;
        }


        //该用户 注册 公司 数量不能超过 规定的最大数量。
        long newCompanyQuantity = repository.findByAdminId(adminId)
                .stream()
                .filter(Objects::nonNull)
                .count();

        boolean hasRegister = repository.findByAdminId(adminId)
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(n -> (!n.isChecked())
                        && (n.getRejects() == null || n.getRejects().isEmpty()));

        boolean hasRejected = repository.findByAdminId(adminId)
                .stream()
                .filter(Objects::nonNull)
                .anyMatch(n -> (!n.isChecked()) && n.getRejects() != null);


        String msg = new StringBuffer("最多只能注册")
                .append(newCompanyProperties.getMaxNewCompanyQuantity())
                .append("个公司,您的帐户已经超过限制！")
                .toString();

        if (hasRegister) {
            info.setNewCompanyErrMsg("您已经提交过公司帐户申请，请等待审核！");
        } else if (hasRejected) {
            info.setNewCompanyErrMsg("您已经申请的材料不符合要求，请修改后重新提交！");
        } else if (newCompanyQuantity > newCompanyProperties.getMaxNewCompanyQuantity()) {
            info.setNewCompanyErrMsg(msg);
        } else {
            info.setNewCompanyErrMsg("");
        }

        return ResponseEntity.ok(info);

    }


    //用在 用户刚刚登录  后，点击"新建公司"，查看 刚用户是否注册过  公司帐户，是否存在需要 审核的 公司帐户。如果存在未审核
    // 的公司帐户，取出companyId，然后前台再通过，companyId，查看已经提交的公司信息，查看新地点工作人员有没有
    // 审核该公司，有没有拒绝的理由。  返回companyId 和 checked，如果companyId存在，且checked为false，那么就进行上面的判断


    //客户自己，当点击 新建公司的时候，首先需要 查看系统中是否存在 未审核的自己注册的公司。
    // 为什么这个 不和新地点的人员审核公司的rest 重合，，因为这个申请人查看的rest
    // 只需要本人登录权限，而新地点审核的rest需要新地点的客服 以上的权限，所以分开了
    //  另外一个不同点就是，这里的参数是需要user 的id，而另外一个需要的companyId


    @GetMapping("/company/findUncheckedCompanyBySelf")
    public ResponseEntity<?> companyChecked() {

        // 将useId转为 string 数组。
        String id = SecurityUtils.getCurrentLogin();

        CheckCompanyInfoForRead c=repository.findByAdminId(id)
                .stream()
                .filter(e -> !e.isChecked())
                .filter(e -> e.getRejects() == null || e.getRejects().isEmpty())
                .findFirst()
                .map(e->convertUtil.companyToCheckCompanyInfoForRead.apply(e))
                .orElse(null);

        return ResponseEntity.ok(c);
    }


    // 和上面那个类似，但是这个是申请人在材料被拒绝后，的rest
    // 而且这里还实现了，，申请人 注册后，资料被驳回，再次查看申请材料的rest，这个可以查看reject
    @GetMapping("/company/findUncheckedCompanyAfterReject")
    public ResponseEntity<?> companyCheckedAfterReject() {

        // 将useId转为 string 数组。
        String id = SecurityUtils.getCurrentLogin();

        CheckCompanyInfoForRead c=repository.findByAdminId(id)
                .stream()
                .filter(e -> !e.isChecked())
                .filter(e -> e.getRejects() !=null)
                .findFirst()
                .map(e->convertUtil.companyToCheckCompanyInfoForRead.apply(e))
                .orElse(null);

        return ResponseEntity.ok(c);
    }

}