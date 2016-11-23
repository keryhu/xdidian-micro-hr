package com.xdidian.keryhu.user.domain.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : 管理员 通过平台，添加客服人员，保存后台的 form
 * @date : 2016年6月18日 下午9:14:58
 */

@Data
@NoArgsConstructor
public class AddServiceForm implements Serializable {

    private String email;
    private String phone;
    private String name;   // 员工姓名
    private String password;


}
