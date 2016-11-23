package com.xdidian.keryhu.menu.config;

import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by hushuming on 2016/11/12.
 *  配置全局的convert formatter
 */


public class WebConfig extends WebMvcConfigurerAdapter{

    @Override
    public void addFormatters(FormatterRegistry registry){

        registry.addConverter(new MenuTypeConvert());


    }
}
