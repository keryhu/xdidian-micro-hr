package com.xdidian.keryhu.company.config;


import com.xdidian.keryhu.company.config.json.convert.CompanyIndustryConvert;
import com.xdidian.keryhu.company.config.json.convert.EnterpriseNatureConvert;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by hushuming on 2016/11/12.
 *
 * 配置全局的convert formatter
 */

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addFormatters(FormatterRegistry registry){

        registry.addConverter(new CompanyIndustryConvert());
        registry.addConverter(new EnterpriseNatureConvert());


    }
}
