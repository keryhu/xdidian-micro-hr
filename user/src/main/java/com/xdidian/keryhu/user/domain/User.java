package com.xdidian.keryhu.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryEntity;
import com.xdidian.keryhu.domain.Role;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hushuming on 2016/10/29.
 */

@Data
@Document     // use desl query must
@QueryEntity  // use desl query must
@ToString(exclude = "password")
public class User implements Serializable {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email; // 注册邮箱

    @Indexed(unique = true)
    private String phone; // 注册手机

    @JsonIgnore
    @RestResource(exported = false)
    // 在spring rest 数据库查询中不显示密码
    private String password; // 为加密后的密码

    private String headerPath;  // 存储的是个人用户头像的 图片地址。


    // 一定要json 序列化 java8 time ,注意json序列号和反序列化中的LocalDateTimeDeserializer，
    // 和LocalDateDeserializer的区别
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)

    private LocalDateTime registerTime; // 用户注册时间


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastLoginTime; // 记录上次登录时间

    // 当用户每次登录成功后，如果发现  这次登录时间 值非 null，那么就将 这次登录时间 复制给上次登录时间，
    //然后重新设置，这次登录时间为  现在时间。
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thisLoginTime; // 记录这次登录时间

    private List<Role> roles = new ArrayList<Role>(); // 权限

    // 邮箱是否被激活 ，之所以要加上index索引，方便定时清理未激活的账户
    @Indexed
    private boolean emailStatus;

    //手机是否被激活，之所以要加上index索引，方便定时清理未激活的账户
    @Indexed
    private boolean phoneStatus;

    //user 对应的company 的id，如果这个id为null或 "",那么就证明他目前没有公司。如果companyId为uuid，那么就证明现在有公司
    // 一个人可以  有 多个公司。
    private Set<String> companyIds=new HashSet<>();

    //增加用户头像。

    private String name; // 对外公布的名字。员工姓名

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime nameModifyTime; // 记录最近一次名字修改的时间


    // 用户的生日，只需要月份－日期，其中年份统一为0000,如果用户生日未设置，默认为1月1日。
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Indexed

    private LocalDate birthday;


    // 用户新注册时候的时候，自动生成Id,其它的变量都为null
    public User() {
        this.id = UUID.randomUUID().toString();
        this.email = null;
        this.password = null;
        this.phone = null;
        this.registerTime = null;
        this.emailStatus = false;
        this.phoneStatus=false;
        this.name=null;
        this.nameModifyTime=null;
        this.birthday=null;
        // roles 已经设置了默认值。
    }


    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addCompanyId(String companyId) {
        this.companyIds.add(companyId);
    }

    public boolean hasRole(Role role) {
        return (this.roles.contains(role));
    }

    public void removeRole(Role role) {
        this.roles = this.roles.stream()
                // 将符合条件的相同role去掉
                .filter(e -> !e.equals(role)).collect(Collectors.toList());
    }


}
