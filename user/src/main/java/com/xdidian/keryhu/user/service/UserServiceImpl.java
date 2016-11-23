package com.xdidian.keryhu.user.service;

import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.domain.feign.LoggedWithMenuDto;
import com.xdidian.keryhu.user.repository.UserRepository;
import com.xdidian.keryhu.user.domain.edit.BirthdayModifyDto;
import com.xdidian.keryhu.user.domain.edit.ChangePasswordDto;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Optional;

import static com.xdidian.keryhu.util.StringValidate.isPeopleName;


/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : 继承自userService 的方法实现
 * @date : 2016年6月18日 下午9:23:30
 */
@Component("userService")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {


    private final PasswordEncoder encoder;

    private final UserRepository repository;
    private final ConverterUtil converterUtil;
    private final MessageSource messageSource;


    /**
     * (查看制定的user id的用户，是否已经在公司组织里,这个需要用户登录后，才能查看)
     * 使用在  personal——core 服务器里面，用户获取自身 菜单的时候，同时获取，
     * 用户的姓名，用户的companyId。
     *
     * @param id userId
     * @return
     */
    @Override
    public LoggedWithMenuDto getIsInCompanyAndName(String id) {
        return repository.findById(id).map(e -> {
            LoggedWithMenuDto dto = new LoggedWithMenuDto();
            boolean isInCompany = !e.getCompanyIds().isEmpty();
            dto.setInCompany(isInCompany);
            dto.setPeopleName(e.getName());
            return dto;
        })
                .orElse(null);
    }

    /**
     * 用户登录后，修改姓名时候，需要验证的service
     *
     * @param name
     * @param id   设定文件 userId
     */
    @Override
    public void validateNameEdit(String name, String id) {

        Assert.isTrue(isPeopleName(name),
                messageSource.getMessage("message.user.nameNotCorrect",
                        null, LocaleContextHolder.getLocale()));

        Optional<User> user = repository.findById(id);
        validateUserId(id);
        User u = user.get();
        Assert.isTrue(!name.equals(u.getName()),
                messageSource.getMessage("message.user.nameCannotSameWithOrigin",
                        null, LocaleContextHolder.getLocale()));

        //算出上次修改时间，返回给前台。

        boolean canModify = converterUtil
                .nameCanModify(repository.findById(id).get());

        if(u.getNameModifyTime()!=null){
            Object[] args = {u.getNameModifyTime().getYear(),
                    u.getNameModifyTime().getMonth(),
                    u.getNameModifyTime().getDayOfMonth()};

            Assert.isTrue(canModify,
                    messageSource.getMessage("message.user.nameCannotModify",
                            args, LocaleContextHolder.getLocale()));

        }

    }


    /**
     * 用户修改生日，需要验证的方法
     *
     * @param dto
     */
    @Override
    public void validateBirthdayAndSave(BirthdayModifyDto dto) {

        //验证传递来的id，是否存在，密码是否对。

        validateUserId(dto.getUserId());

        validatePasswordMatch(dto.getPassword(), dto.getUserId());

        repository.findById(dto.getUserId()).ifPresent(e -> {
            e.setBirthday(dto.getBirthday());
            repository.save(e);
        });
    }

    /**
     * 当用户修改密码的时候，后台验证码修改的密码，提交的数据的准确性
     *
     * @param dto 验证的信息：
     *            1 id必需存在
     *            2 原密码必需匹配
     */

    @Override
    public void validateChangePassword(ChangePasswordDto dto) {

        validateUserId(dto.getId());

        validatePasswordMatch(dto.getOriginPassword(), dto.getId());

        Assert.isTrue(!dto.getOriginPassword().equals(dto.getNewPassword()),
                messageSource.getMessage("message.user.newPasswordCannotSameWithOrigin",
                        null,LocaleContextHolder.getLocale()));
    }


    /**
     * 验证id是否存在数据库，用在上面的 个人资料修改的时候，验证
     *
     * @param id userId
     */
    private void validateUserId(String id) {
        Optional<User> user = repository.findById(id);
        Object[] args={id};

        Assert.isTrue(user.isPresent(),
                messageSource.getMessage("message.user.idNotFound",
                args,LocaleContextHolder.getLocale()));
    }

    /**
     * 验证两个密码是否匹配,用在上面的密码修改，和生日修改上。
     *
     * @param inputPassword 用户提交的password
     * @param id            用户的userId
     */
    private void validatePasswordMatch(String inputPassword, String id) {
        String hashPassword = repository
                .findById(id).get()
                .getPassword();
        boolean match = encoder.matches(inputPassword, hashPassword);

        Assert.isTrue(match,messageSource.getMessage("message.user.passwordNotCorrect",
                null,LocaleContextHolder.getLocale()));
    }

}
