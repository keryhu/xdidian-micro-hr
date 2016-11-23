package com.xdidian.keryhu.menu.service;

import java.util.List;

import com.xdidian.keryhu.menu.domain.core.MenuDto;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;


public interface MenuService {
  
  /**
   * 
  * getMenu
  * 获取当前用户的菜单组,说明只有login用户才能访问此菜单数组。
  * 还必需要从前台传回当前用户的role)
  * @param @return    设定文件
  * @return Menu   返回类型
  * @throws
   */
  
  @PreAuthorize("#n==authentication.name")
  List<MenuDto> getMenu(@Param("n") String userId);

}
