package com.xdidian.keryhu.company.domain.company.check;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by hushuming on 2016/11/1.
 *
 * 当审核公司，注册材料的时候，如果拒绝了他的提交申请，那么必需提交，拒绝的理由
 *
 * 拒绝的理由，由两部分组成，item（是提交申请的条目），message，该条目拒绝的理由
 */

@Data
@NoArgsConstructor
public class Reject implements Serializable {

    private CompanySignupItems item;
    private String message;
}
