package com.xdidian.keryhu.user.service;

import com.xdidian.keryhu.domain.Role;
import com.xdidian.keryhu.domain.SignupDto;
import com.xdidian.keryhu.service.imageService.FileService;
import com.xdidian.keryhu.user.config.CreateDir;
import com.xdidian.keryhu.user.domain.User;
import com.xdidian.keryhu.user.domain.edit.UserEditDto;
import com.xdidian.keryhu.user.domain.UserInfoDto;

import com.xdidian.keryhu.user.domain.feign.AuthUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.function.Function;

import static com.xdidian.keryhu.util.Constants.INTERVAL_DAYS_OF_NAME_MODIFICATION;

@Component
public class ConverterUtil {

    @Autowired
    private CreateDir createDir;

    private final FileService fileService = new FileService();
    private final String defaultImage="/default.png";
    /**
     * 将User 转为 AuthUser 对象。
     */
    public Function<User, AuthUserDto> userToAuthUser = x -> new AuthUserDto(x.getId(), x.getEmail(),
            x.getPassword(), x.getRoles(), x.isEmailStatus());
    /**
     * 将用户注册数据转为User对象。此时将用户注册时间和权限加进去,注册用户加上默认的权限，只有等到有公司了，有了读取整个部门 的权限后，才会加上其他权限。
     */
    public Function<SignupDto, User> signupDtoToUser = x -> {

        User user = new User();
        user.setEmail(x.getEmail());
        user.setPhone(x.getPhone());
        user.setName(x.getName());
        user.setPassword(x.getPassword());
        user.setRoles(Arrays.asList(Role.ROLE_DEFAULT));
        user.setRegisterTime(LocalDateTime.now());
        user.setEmailStatus(false);
        //第一次的注册用户，默认设置 生日为 1月1日。
        user.setBirthday(LocalDate.of(1999, 1, 1));

        return user;
    };

    /**
     * 将user 对象， 转为前台显示的 userInfoDto,这个里面包含了，用户的生日，所以就不需要再单独查询用户的生日了
     */

    public Function<User, UserInfoDto> userToInfoDto = x -> {
        UserInfoDto dto = new UserInfoDto();
        dto.setId(x.getId());
        dto.setEmail(x.getEmail());
        dto.setPhone(x.getPhone());
        String headerPath = "";
        //如果用户还没有上传图片，就使用默认的，且告诉前台，使用的是默认图片。
        if (x.getHeaderPath() == null) {
            headerPath = new StringBuffer(createDir.getUserHeader())
                    .append(defaultImage)
                    .toString();
        } else {
            headerPath = x.getHeaderPath();
        }

        byte[] b = fileService.filePathToPngByte(headerPath);
        dto.setHeader(b);

        dto.setRegisterTime(x.getRegisterTime());
        dto.setLastLoginTime(x.getLastLoginTime());
        if (x.getCompanyIds() != null) {
            dto.setCompanyIds(x.getCompanyIds());
        }
        if (x.getName() != null) {
            dto.setName(x.getName());
        }

        dto.setBirthday(x.getBirthday());
        return dto;
    };


    public Function<User, UserEditDto> userToEditDto = x -> {
        UserEditDto dto = new UserEditDto();
        dto.setId(x.getId());
        dto.setEmail(x.getEmail());
        dto.setPhone(x.getPhone());
        String headerPath = "";
        //如果用户还没有上传图片，就使用默认的，且告诉前台，使用的是默认图片。
        if (x.getHeaderPath() == null) {
            dto.setUseDefaultHeaderImg(true);
            headerPath = new StringBuffer(createDir.getUserHeader())
                    .append(defaultImage)
                    .toString();
        } else {
            headerPath = x.getHeaderPath();
            dto.setUseDefaultHeaderImg(false);
        }

        byte[] b = fileService.filePathToPngByte(headerPath);

        dto.setHeader(b);

        if (x.getName() != null) {
            dto.setName(x.getName());
        }
        if (x.getNameModifyTime() != null) {
            dto.setNameModifyTime(x.getNameModifyTime());
        }

        dto.setNameCanModify(nameCanModify(x));
        dto.setBirthday(x.getBirthday());
        return dto;
    };


    //计算当前用户的名字，是否可以修改。，默认是修改一次后，再次修改需要等待至少60天。
    public boolean nameCanModify(User u) {
        if (u.getNameModifyTime() == null) {
            return true;
        }
        return LocalDateTime.now().
                isAfter(u.getNameModifyTime().plusDays(INTERVAL_DAYS_OF_NAME_MODIFICATION));
    }

}
