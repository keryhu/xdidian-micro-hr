package com.xdidian.keryhu.company.config.json.convert;

import com.xdidian.keryhu.company.domain.company.component.EnterpriseNature;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by hushuming on 2016/11/12.
 *
 * 将前台的中文 EnterpriseNature，转为 EnterpriseNature enum对象
 */
public class EnterpriseNatureConvert implements Converter<String,EnterpriseNature> {

    @Override
    public EnterpriseNature convert(String s) {
        return EnterpriseNature.forValue(s);
    }
}
