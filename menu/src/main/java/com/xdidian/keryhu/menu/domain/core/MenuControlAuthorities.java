package com.xdidian.keryhu.menu.domain.core;



/**
 * 
* @ClassName: MenuControlAuthorities
* 针对菜单的控制权限，包含了读写改删)
* @author keryhu  keryhu@hotmail.com
 *
 * 这个以后再改，根据业务需求
 * 
* @date 2016年8月10日 下午9:39:17
 */
public enum MenuControlAuthorities {
  READ,
  EDIT,
  ADD,
  REMOVE;

  /**
   * 返回中文名字，方便前台显示
   */
  public String getName(){
    MenuControlAuthorities a=this;
    String result="";
    switch(a){
      case READ:
       result="读";
       break;
      case EDIT:
        result="编辑";
        break;
      case ADD:
        result="新建";
        break;
      case REMOVE:
        result="删除";
        break;
      default:
        break;
      
    }
    return result;
  }

}
