package com.xdidian.keryhu.company.domain.feign;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hushuming on 2016/10/9.
 * 包含了email和phone，1个user，2个对象的的class，
 * 用来传递spring feign。对方使用userId，来同时获取email和phone
 */

@Getter
@Setter
public class EmailAndPhoneDto implements Serializable {

    private String email;
    private String phone;
}
