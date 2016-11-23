package com.xdidian.keryhu.message.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * Created by hushuming on 2016/11/11.
 *
 * 配置message组件的所有  convert
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Add formatters and/or converters
        registry.addConverter(new SubjectConvert());
    }
}
