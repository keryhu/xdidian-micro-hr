package com.xdidian.keryhu.company.config.propertiesConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by hushuming on 2016/9/22.
 *
 * 针对于  公司的 保存的 营业执照和 介绍信，设置的 图片resize  属性，包含了宽度和 高度。
 */


@Component
@ConfigurationProperties(prefix = "imageResize")
@Getter
@Setter

public class ImageResizeProperties implements Serializable {

    private long maxSize;      //上传的图片，最大的size，单位byte

}
