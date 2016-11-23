package com.xdidian.keryhu.company.config.json;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.xdidian.keryhu.company.domain.Department;
import com.xdidian.keryhu.company.domain.Office;
import com.xdidian.keryhu.tree.TreeNode;

public class TreeDepartmentSerializer extends JsonSerializer<TreeNode<Department>> {

  @Override
  public void serialize(TreeNode<Department> value, JsonGenerator jgen, SerializerProvider provider)
      throws IOException, JsonProcessingException {
    // TODO Auto-generated method stub

    writeDepartment(value, jgen);

  }

  private void writeDepartment(TreeNode<Department> node, JsonGenerator jgen) throws IOException {
    jgen.writeStartObject();
    Department department = node.data();
    if (department == null) {
      jgen.writeNullField("id");
      jgen.writeNullField("name");
      jgen.writeNullField("listOffice");
      jgen.writeNullField("offices");
    } else {
      jgen.writeStringField("id", department.getId());
      jgen.writeStringField("name", department.getName());

      if (department.getListOffice() != null && department.getListOffice().size() > 0) {
        jgen.writeArrayFieldStart("listOffice");
        List<Office> listOffice = department.getListOffice();
        Gson gson = new Gson();

        for (Office o : listOffice) {
          jgen.writeStringField("office", gson.toJson(o));
        }
        jgen.writeEndArray();
      }

      if (department.getOffices() != null) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new TreeOfficeSerializer());
        mapper.registerModule(module);
        String officeJson=mapper.writeValueAsString(department.getOffices());

        jgen.writeStringField("offices", officeJson);
      }

    }
    // 写他的下代，

    writeDepartments(node.subtrees(), jgen);

    jgen.writeEndObject();
  }

  private void writeDepartments(Collection<? extends TreeNode<Department>> nodes,
      JsonGenerator jgen) throws IOException {

    if (nodes.isEmpty())
      return;
    jgen.writeArrayFieldStart("department");

    for (TreeNode<Department> node : nodes) {
      writeDepartment(node, jgen);
    }
    jgen.writeEndArray();
  }
}
