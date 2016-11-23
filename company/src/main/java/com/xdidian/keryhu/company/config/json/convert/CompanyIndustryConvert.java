package com.xdidian.keryhu.company.config.json.convert;

import com.xdidian.keryhu.company.domain.company.component.CompanyIndustry;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by hushuming on 2016/11/12.
 *
 * 将前台的中文 CompanyIndustry，转为 CompanyIndustry enum对象
 */


public class CompanyIndustryConvert implements Converter<String,CompanyIndustry> {
    @Override
    public CompanyIndustry convert(String s) {
        return CompanyIndustry.forValue(s);
    }
}
