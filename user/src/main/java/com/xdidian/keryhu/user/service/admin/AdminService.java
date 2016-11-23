package com.xdidian.keryhu.user.service.admin;

import com.xdidian.keryhu.user.domain.admin.AddServiceForm;

/**
 * Created by hushuming on 2016/9/29.
 * <p>
 * 新地点，客服人员，管理人员，操作的service
 */


public interface AdminService {

    // 当保存 客服人员 的时候验证的提交信息。
    void validateAddService(final AddServiceForm form);
}
