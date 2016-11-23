package com.xdidian.keryhu.company.config.converter;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.xdidian.keryhu.company.domain.Office;
import com.xdidian.keryhu.tree.LinkedMultiTreeNode;
import com.xdidian.keryhu.tree.TreeNode;


/**
 *
 * 公司，职位 tree，保存数据库的时候，从数据库对象转为tree对象的 方法。
 *
 *
 *
 */

@ReadingConverter
public class TreeOfficeReadConvert implements Converter<DBObject,TreeNode<Office>> {

  @Override
  public TreeNode<Office> convert(DBObject source) {
     
    return convertOffice(source);
  }
  
  private TreeNode<Office> convertOffice(DBObject source){
    if(source.containsField("name")){
      String name=source.get("name").toString();
      Office office=new Office(name);
      if(source.containsField("duty")){
        String duty=source.get("duty").toString();
        office.setDuty(duty);
      }
      if(source.containsField("right")){
        String right=source.get("right").toString();
        office.setRight(right);
      }
      if(source.containsField("salary")){
        String salary=source.get("salary").toString();
        office.setSalary(salary);
      }
      if(source.containsField("basePay")){
        String basePay=source.get("basePay").toString();
        office.setBasePay(basePay);
      }
      if(source.containsField("remarks")){
        String remarks=source.get("remarks").toString();
        office.setRemarks(remarks);
      }
      
      TreeNode<Office> o1=new LinkedMultiTreeNode<Office>(office);
      
      if(source.containsField("office")&&source.get("office").getClass().isArray()){
        BasicDBList  offices=(BasicDBList) source.get("office");
        Collection<TreeNode<Office>> back=convertOffices(offices);
        if(!back.isEmpty()){
          back.forEach(o1::add);
          
        }
      }
      return o1;  
    }
    
    return null;
  }
  
  
  @SuppressWarnings("unchecked")
  private Collection<TreeNode<Office>> convertOffices(DBObject sources){
    
    Collection<TreeNode<Office>> a=new HashSet<>();
    if(sources==null)
      return null;
    ((ArrayList<DBObject>) sources).forEach(e->{
      TreeNode<Office> m;
      m =convertOffice(e);
      if(m!=null){
        a.add(m);
      }
    });
    return a;
  }

}
