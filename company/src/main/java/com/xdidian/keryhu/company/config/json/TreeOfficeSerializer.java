package com.xdidian.keryhu.company.config.json;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xdidian.keryhu.company.domain.Office;
import com.xdidian.keryhu.tree.TreeNode;

/**
 * 
 * @ClassName: OfficeSerializer
 * @Description: TODO(职位 treeNode 格式的的 json 序列化)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年8月12日 下午12:59:43
 */
public class TreeOfficeSerializer extends JsonSerializer<TreeNode<Office>> {

  @Override
  public void serialize(TreeNode<Office> value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
    // TODO Auto-generated method stub

    writeOffice(value, jgen);
  }

  // 分两步序列化
  private void writeOffice(TreeNode<Office> node, JsonGenerator jgen) throws IOException {
    jgen.writeStartObject();
    Office office = node.data();
    if (office == null) {
      jgen.writeNullField("id");
      jgen.writeNullField("name");
      jgen.writeNullField("duty");
      jgen.writeNullField("right");
      jgen.writeNullField("salary");
      jgen.writeNullField("basePay");
      jgen.writeNullField("remarks");
    } else {
      jgen.writeStringField("id", office.getId());
      jgen.writeStringField("name", office.getName());
      jgen.writeStringField("duty", office.getDuty());
      jgen.writeStringField("right", office.getRight());
      jgen.writeStringField("salary", office.getSalary());
      jgen.writeStringField("basePay", office.getBasePay());
      jgen.writeStringField("remarks", office.getRemarks());
    }
    writeOffices(node.subtrees(),jgen);
    jgen.writeEndObject();
  }

  private void writeOffices(Collection<? extends TreeNode<Office>> nodes, JsonGenerator jgen)
      throws IOException {
    if(nodes.isEmpty()) return;
    jgen.writeArrayFieldStart("offices");
    for(TreeNode<Office> node:nodes){
      writeOffice(node,jgen);
    }
    jgen.writeEndArray();

  }

}
