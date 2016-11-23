package com.xdidian.keryhu.auth_server.security;

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
 * : pring OAuth2 resource 验证方法 注意不要使用 @RequiredArgsConstructor(onConstructor
 * = @__(@Autowired))
 * ]: 2016年6月18日 下午8:03:35
 *
 * @author : keryHu keryhu@hotmail.com
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //也不能，变成 constructor autowired，只能目前这个样子，不要更改
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
        http
                .authorizeRequests().expressionHandler(webExpressionHandler()) // 权限排序
                // 对于auth－server里面的url控制，有2种方法，一个是在这个方法里面添加控制，注意不要加到WebSecurityConfig class 里，那个里面没有用。
                // login* 能够匹配 带参数的 login url
                .antMatchers("/webjars/**", "/favicon.ico", "/oauth/authorize/**","/").permitAll()
                .mvcMatchers(HttpMethod.GET, "/query/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/loginAttemptUsers").hasRole("ADMIN")
                .anyRequest().authenticated();

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources.resourceId(READ_AND_WRITE_RESOURCE_ID);
    }


}
