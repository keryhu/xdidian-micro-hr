package com.xdidian.keryhu.company.config.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.xdidian.keryhu.company.domain.Department;
import com.xdidian.keryhu.company.domain.Office;
import com.xdidian.keryhu.tree.LinkedMultiTreeNode;
import com.xdidian.keryhu.tree.TreeNode;

/**
 *
 * 公司，部门tree，保存数据库的时候，从数据库对象转为tree对象的 方法。
 *
 *
 *
 */



@ReadingConverter
public class TreeDepartmentReadConvert implements Converter<DBObject, TreeNode<Department>> {

  @Override
  public TreeNode<Department> convert(DBObject source) {
    // TODO Auto-generated method stub

    return convertDepartment(source);
  }

  private TreeNode<Department> convertDepartment(DBObject source) {
    if (source.containsField("name")) {
      String name = source.get("name").toString();
      Department department = new Department(name);

      if (source.containsField("id")) {
        String id = source.get("id").toString();
        department.setId(id);
      }

      if (source.containsField("listOffice")) {
        BasicDBList dbObjects = (BasicDBList) source.get("listOffice");

        if (!dbObjects.isEmpty()) {

          List<Office> list = dbObjects.stream().map(e -> {
            DBObject object = (DBObject) e;
            String office = object.get("office").toString();
            Gson gson = new Gson();
            Office o = gson.fromJson(office, Office.class);
            return o;
          }).collect(Collectors.toList());

          department.setListOffice(list);
        }
      }

      if(source.containsField("offices")){
        DBObject treeOffice=(DBObject) source.get("offices");
        TreeOfficeReadConvert officeConvert=new TreeOfficeReadConvert();
        TreeNode<Office> o=officeConvert.convert(treeOffice);
        department.setOffices(o);
      }


      TreeNode<Department> n1 = new LinkedMultiTreeNode<Department>(department);


      if (source.containsField("department") && source.get("department").getClass().isArray()) {
        BasicDBList departments = (BasicDBList) source.get("department");
        Collection<TreeNode<Department>> back = convertDepartments(departments);
        if (!back.isEmpty()) {
          back.forEach(e -> n1.add(e));
        }
      }
      return n1;
    }
    return null;

  }

  @SuppressWarnings("unchecked")
  private Collection<TreeNode<Department>> convertDepartments(DBObject sources) {
    Collection<TreeNode<Department>> a = new HashSet<>();
    if (sources == null) {
      return null;
    }
    ((ArrayList<DBObject>) sources).forEach(e -> {
      TreeNode<Department> m = convertDepartment(e);
      if (m != null) {
        a.add(m);
      }
    });
    return a;
  }
}