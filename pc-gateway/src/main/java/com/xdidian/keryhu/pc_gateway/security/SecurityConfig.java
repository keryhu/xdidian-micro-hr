package com.xdidian.keryhu.pc_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.xdidian.keryhu.util.Constants.READ_AND_WRITE_RESOURCE_ID;

/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : SSO 主方法
 * @date : 2016年6月18日 下午9:12:22
 */
@Configuration
@EnableOAuth2Sso
public class SecurityConfig implements ResourceServerConfigurer {

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

        // n内在配置信息在 ／admin 下，到时候加上权限
        http
                .logout().permitAll()
                .and().authorizeRequests()
                .antMatchers("/api/currentUser","/api/signup/**","/api/user/**",
                        "/api/auth-server/**","/api/account-activate/**",
                        "/api/company/**","/api/menu/**","/api/websocket/**",
                        "/api/message/**").permitAll()
                // spring boot admin 配置变量

                .expressionHandler(webExpressionHandler()) // 权限排序
                .anyRequest().authenticated()
                .and().csrf().disable();
        //.csrfTokenRepository(csrfTokenRepository()).and()
        // .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
        // 增加这个的原因是，因为文件上传的时候需要。
        //  .addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class);
    }

    /**
     * csrf配置
     */
    private Filter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain) throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources.resourceId(READ_AND_WRITE_RESOURCE_ID);
    }

}
