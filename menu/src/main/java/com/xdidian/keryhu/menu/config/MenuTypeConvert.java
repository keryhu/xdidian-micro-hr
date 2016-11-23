package com.xdidian.keryhu.menu.config;

import com.xdidian.keryhu.menu.domain.core.MenuType;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by hushuming on 2016/11/12.
 * 将enum 的menu 中文菜单名字， 转为 英文。
 */

public class MenuTypeConvert implements Converter<String, MenuType> {
    @Override
    public MenuType convert(String s) {

        return MenuType.forValue(s);
    }
}
