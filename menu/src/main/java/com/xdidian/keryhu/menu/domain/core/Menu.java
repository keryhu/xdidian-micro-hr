package com.xdidian.keryhu.menu.domain.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;

/**
 * 
* @ClassName: Menu
* 这个包含了不同的userId，和对应的菜单的名字的，是需要保存到数据库里的，
 * 因为一个有了公司名字的user，
* 你并不知道，他是默认菜单，还是管理员菜单。
* 每一个菜单，每一个用户拥有具体的权限，在  各自的菜单的数据库中，进行逻辑分析，，在第一次加载菜单名字和url的时候，不做分析)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月11日 上午9:47:28
 */
@Data
public class Menu implements Serializable{

  private static final long serialVersionUID = 4336915375251779538L;
  
  @Id
  private String id;
  
  @Indexed(unique = true)
  private String userId;  //不同的人，对应不同的菜单，不能根据公司来区分，因为同一个公司，因为不同人会有多个菜单种类。，

  // 是否是默认的菜单数组（指用户已经有公司的情况下，非新地点客服或管理人员）默认为true
  private boolean isDefaultMenus=true;
  
  //将menu 的 中文名字，和 url 保存到 数据库中来。
  private List<MenuType> menuTypes=new ArrayList<MenuType>();   //该userId所包含的菜单数组
  
  
  public Menu(){
    this.id=UUID.randomUUID().toString();
    this.isDefaultMenus=true;
  }

  public void addMenu(MenuType menuType){
    this.menuTypes.add(menuType);
  }
  
  public void removeMenu(MenuType menuType){
    this.menuTypes=this.menuTypes.stream()
        .filter(e->e!=menuType).collect(Collectors.toList());
  }
}
