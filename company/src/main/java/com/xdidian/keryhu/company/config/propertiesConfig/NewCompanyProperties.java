package com.xdidian.keryhu.company.config.propertiesConfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by hushuming on 2016/9/27.
 *
 * 当创建新公司的时候，需要用到的属性，这里设置了，，一个用户最多能够创建几个公司帐户。
 */

@Component
@ConfigurationProperties(prefix = "newCompany")
@Getter
@Setter

public class NewCompanyProperties implements Serializable {

    private int maxNewCompanyQuantity;


}
