package com.xdidian.keryhu.company.config.json;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.xdidian.keryhu.company.domain.Office;
import com.xdidian.keryhu.tree.LinkedMultiTreeNode;
import com.xdidian.keryhu.tree.TreeNode;

/**
 * 
* @ClassName: OfficeDeserializer
* @Description: TODO(office 反序列化)
* @author keryhu  keryhu@hotmail.com
* @date 2016年8月12日 下午2:18:27
 */
public class TreeOfficeDeserializer extends JsonDeserializer<TreeNode<Office>>{

  @Override
  public TreeNode<Office> deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    // TODO Auto-generated method stub
    JsonNode node = jp.getCodec().readTree(jp);
    return parseOffice(node);
  }

  private TreeNode<Office> parseOffice(JsonNode node)
      throws JsonProcessingException, IOException{
    if (node.has("name")){
      String name = node.get("name").asText();
      Office office=new Office(name);
      if(node.has("id")){
        String id=node.get("id").asText();
        office.setId(id);
      }
      if(node.has("duty")){
        String duty=node.get("duty").asText();
        office.setDuty(duty);
      }
      if(node.has("right")){
        String right=node.get("right").asText();
        office.setRight(right);
      }
      if(node.has("salary")){
        String salary=node.get("salary").asText();
        office.setSalary(salary);
      }
      if(node.has("basePay")){
        String basePay=node.get("basePay").asText();
        office.setBasePay(basePay);
      }
      if(node.has("remarks")){
        String remarks=node.get("remarks").asText();
        office.setRemarks(remarks);
      }
      
      TreeNode<Office> o1=new LinkedMultiTreeNode<Office>(office);
      
      if(node.has("offices")&&node.get("offices").isArray()){
        ArrayNode offices=(ArrayNode)node.get("offices");
        Collection<TreeNode<Office>> backNodes=parseOffices(offices);
        if(!backNodes.isEmpty()){
          backNodes.forEach(e->o1.add(e));
        }
        
      }
      return o1;  
    }
        return null;
    
  }
  
  private Collection<TreeNode<Office>> parseOffices(JsonNode arrayNode)
      throws JsonProcessingException, IOException{
    Collection<TreeNode<Office>> a=new HashSet<>();
    if (arrayNode == null)
      return null;
    arrayNode.forEach(e->{
      TreeNode<Office> m;
      try {
        m = parseOffice(e);
        if(m!=null){
          a.add(m);
        }    
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
     
    });
    return a;
    
  }
}
