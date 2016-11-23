package com.xdidian.keryhu.company.domain.company.check;

import com.xdidian.keryhu.domain.CheckType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by hushuming on 2016/11/1.
 * 此为审核公司注册 材料的 实体对象
 */

@Data
@NoArgsConstructor
public class CheckCompanySignupInfoDto implements Serializable {


    // 审核公司，同意或拒绝，只能一个个的审核，提交申请，不能多个一起提交，
    // 另外，拒绝的原因，应该 选择某一栏目，是一个下拉框，也就是必需提交  具体是那一项目不符合要求。

    private String companyId;

    private CheckType checkMethod;   //AGREE, REJECT

    // 一个公司的注册申请，不符合要求的，可能由多个，所以是数组,且这里的reject，不允许重复

    private Set<Reject> rejects;





}
