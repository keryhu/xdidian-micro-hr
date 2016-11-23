package com.xdidian.keryhu.user.service;


import com.xdidian.keryhu.user.domain.edit.ChangePasswordDto;
import com.xdidian.keryhu.user.domain.feign.LoggedWithMenuDto;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import com.xdidian.keryhu.user.domain.edit.BirthdayModifyDto;
import com.xdidian.keryhu.user.domain.User;


/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : user-account 的一些service
 * @date : 2016年6月18日 下午9:23:16
 */
public interface UserService {

    /**
     * (查看制定的user id的用户，是否已经在公司组织里,这个需要用户登录后，才能查看)
     * 使用在  personal——core 服务器里面，用户获取自身 菜单的时候，同时获取，用户的姓名，用户的companyId。
     *
     * @param id userId
     */

    @PreAuthorize("#n==authentication.name or hasRole('ROLE_XDIDIAN_SERVICE')")
    public LoggedWithMenuDto getIsInCompanyAndName(@Param("n") final String id);


    /**
     * validateNameEdit
     * 当用户提交姓名修改的时候，验证是否可以通过
     *
     * @param name
     * @param id   设定文件 userId
     */


    @PreAuthorize("#n==authentication.name or hasRole('ROLE_XDIDIAN_SERVICE')")
    public void validateNameEdit(final String name, @Param("n") final String id);

    /**
     * validateBirthdayAndSave
     * 用户修改生日)
     *
     * @param dto    设定文件
     *
     */


    @PreAuthorize("#c.userId==authentication.name or hasRole('ROLE_XDIDIAN_SERVICE')")
    public void validateBirthdayAndSave(final @P("c") BirthdayModifyDto dto);

    /**
     * @param dto    设定文件
     * validateEmailEdit
     * @Description: 前台用户需要修改email的时候，粗发的后台验证方法
     */

    @PreAuthorize("#c.id==authentication.name")
    public void validateChangePassword(final @P("c") ChangePasswordDto dto);


}
