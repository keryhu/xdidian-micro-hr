package com.xdidian.keryhu.company.config.converter;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.xdidian.keryhu.company.domain.Department;
import com.xdidian.keryhu.tree.TreeNode;

/**
 *
 * 公司，部门tree，保存数据库的时候，从tree对象转为数据库对象的 方法。
 *
 *
 *
 */
@WritingConverter
public class TreeDepartmentWriteConvert implements Converter<TreeNode<Department>,DBObject>{

  @Override
  public DBObject convert(TreeNode<Department> source) {
    // TODO Auto-generated method stub
    return convertDepartment(source) ;
  }

  private DBObject convertDepartment(TreeNode<Department> source){
    DBObject dbo = new BasicDBObject();
    Department department = source.data();
    if(department!=null){
      dbo.put("id", department.getId());
      dbo.put("name", department.getName());
      BasicDBList list =new BasicDBList();
      //单独处理 listOffice
      BasicDBObject b=new BasicDBObject();
      if(!department.getListOffice().isEmpty()){
        department.getListOffice()
                .forEach(e->{
                  list.add(b.put("office", e));
                });
        dbo.put("listOffice", list);
      }

      if(source.subtrees()!=null){
        BasicDBList treeList=convertDepartments(source.subtrees());
        dbo.put("offices", treeList);
      }
      return dbo;
    }
    return null;
  }

  private BasicDBList convertDepartments(Collection<? extends TreeNode<Department>> sources){
    BasicDBList list =new BasicDBList();
    sources.forEach(e->list.add(convertDepartment(e)));
    return list;
  }

}