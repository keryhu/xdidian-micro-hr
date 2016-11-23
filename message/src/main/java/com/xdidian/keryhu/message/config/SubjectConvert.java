package com.xdidian.keryhu.message.config;

import com.xdidian.keryhu.domain.message.Subject;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by hushuming on 2016/11/12.
 * <p>
 * 将中文"待审核公司"，"公司注册成功"。。 转为  subject enum对象
 */
public class SubjectConvert implements Converter<String, Subject> {
    @Override
    public Subject convert(String s) {

        return Subject.forValue(s);
    }
}
