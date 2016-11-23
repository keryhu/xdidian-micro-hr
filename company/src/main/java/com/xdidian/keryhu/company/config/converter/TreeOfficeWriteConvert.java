package com.xdidian.keryhu.company.config.converter;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.xdidian.keryhu.company.domain.Office;
import com.xdidian.keryhu.tree.TreeNode;

@WritingConverter
public class TreeOfficeWriteConvert implements Converter<TreeNode<Office>,DBObject> {

  @Override
  public DBObject convert(TreeNode<Office> source) {

    return convertOffice(source);
  }
  
  private DBObject convertOffice(TreeNode<Office> source){
    DBObject dbo = new BasicDBObject();
    Office office = source.data();
    if(office!=null){
      dbo.put("id", office.getId());
      dbo.put("name", office.getName());
      dbo.put("duty", office.getDuty());
      dbo.put("right", office.getRight());
      dbo.put("salary", office.getSalary());
      dbo.put("basePay", office.getBasePay());
      dbo.put("remarks", office.getRemarks());
      if(source.subtrees()!=null){
        BasicDBList list=convertOffices(source.subtrees());
        dbo.put("offices", list);
      }
          
      return dbo;
    }
    else{
      return null;
    }
    
  }
  
  private BasicDBList convertOffices(Collection<? extends TreeNode<Office>> sources){
    BasicDBList list =new BasicDBList();
    sources.forEach(e->list.add(convertOffice(e)));
    return list;
  }

}
