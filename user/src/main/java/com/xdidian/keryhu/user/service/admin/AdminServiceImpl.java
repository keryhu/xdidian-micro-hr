package com.xdidian.keryhu.user.service.admin;

import com.xdidian.keryhu.user.repository.UserRepository;
import com.xdidian.keryhu.user.domain.admin.AddServiceForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.xdidian.keryhu.util.StringValidate.*;

/**
 * Created by hushuming on 2016/9/29.
 */

@Component("adminService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImpl implements AdminService {

    private final UserRepository repository;
    private final MessageSource messageSource;

    // 当保存 客服人员 的时候验证的提交信息。
    @Override
    public void validateAddService(AddServiceForm form) {

        Assert.isTrue(isEmail(form.getEmail()),
                messageSource.getMessage("message.user.emailNotCorrect",
                        null, LocaleContextHolder.getLocale()));

        Assert.isTrue(isPhone(form.getPhone()),
                messageSource.getMessage("message.user.phoneNotCorrect",
                        null, LocaleContextHolder.getLocale()));

        Assert.isTrue(isPassword(form.getPassword()),
                messageSource.getMessage("message.user.passwordTypeNotCorrect",
                        null, LocaleContextHolder.getLocale()));

        Assert.isTrue(isPeopleName(form.getName()),
                messageSource.getMessage("message.user.nameNotCorrect",
                        null, LocaleContextHolder.getLocale()));

        Assert.isTrue(!repository.findByEmail(form.getEmail()).isPresent(),
                messageSource.getMessage("message.user.emailExist",
                        null, LocaleContextHolder.getLocale()));

        Assert.isTrue(!repository.findByPhone(form.getPhone()).isPresent(),
                messageSource.getMessage("message.user.phoneExist",
                        null, LocaleContextHolder.getLocale()));

    }
}
