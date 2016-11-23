package com.xdidian.keryhu.company.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import static com.xdidian.keryhu.util.Constants.ROLE_HIERARCHY;

@Configuration
public class RoleHierarchyConfig {

  /**
   * 
  * @Title: roleHierarchy
  * 系统用户role 排序管理)
  * @param @return    设定文件
  * @return RoleHierarchyImpl    返回类型
  * @throws
   */
  
  @Bean
  public RoleHierarchyImpl roleHierarchy() {
    RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
    // 新地点管理员》新地点客服人员》客户管理员》客户部门管理员》默认人员
    roleHierarchyImpl.setHierarchy(ROLE_HIERARCHY);
    return roleHierarchyImpl;
  }

}
