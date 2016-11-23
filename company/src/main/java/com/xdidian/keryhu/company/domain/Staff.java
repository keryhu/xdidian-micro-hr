package com.xdidian.keryhu.company.domain;

import java.io.Serializable;
import java.util.Collection;

import com.xdidian.keryhu.tree.TreeNode;

import lombok.Data;

/**
 * 员工基本class，包含员工的姓名，userId，公司名字，直接上级领导，同事（同一个部门的人员），下级。)
 * @date : 2016年7月30日 上午10:23:36
 * @author : keryHu keryhu@hotmail.com
 */
@Data
public class Staff implements Serializable
{
  private static final long serialVersionUID = 4243039967691164549L;
  
  private String id;
  private String name;  //员工信息
  private String userId;  //user id;
  private TreeNode<Department> leader;  //直接领导的职位
  private Collection<TreeNode<Department>> colleague;  //同事人员的职位
  private Collection<TreeNode<Department>>  subodinate; // 部下人员的职位

}
