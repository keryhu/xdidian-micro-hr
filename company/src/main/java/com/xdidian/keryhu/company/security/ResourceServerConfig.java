package com.xdidian.keryhu.company.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import static com.xdidian.keryhu.util.Constants.READ_AND_WRITE_RESOURCE_ID;


/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : spring OAuth2 Resource 方法
 * @date : 2016年6月18日 下午9:22:20
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private RoleHierarchyImpl roleHierarchy;

    /**
     * 调用spring security role 权限大小排序bean
     */
    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler =
                new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        return defaultWebSecurityExpressionHandler;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().and().authorizeRequests().expressionHandler(webExpressionHandler()) // 权限排序
                .mvcMatchers("/favicon.ico", "/query/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("XDIDIAN_ADMIN")
                .mvcMatchers("/service/**").hasAnyRole("XDIDIAN_ADMIN", "XDIDIAN_SERVICE")
                .anyRequest().authenticated();

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources.resourceId(READ_AND_WRITE_RESOURCE_ID);
    }
}
