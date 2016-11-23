package com.xdidian.keryhu.company.config.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.xdidian.keryhu.company.domain.Department;
import com.xdidian.keryhu.company.domain.Office;
import com.xdidian.keryhu.tree.LinkedMultiTreeNode;
import com.xdidian.keryhu.tree.TreeNode;

public class TreeDepartmentDeserializer extends JsonDeserializer<TreeNode<Department>> {

  @Override
  public TreeNode<Department> deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    // TODO Auto-generated method stub
    JsonNode node = jp.getCodec().readTree(jp);
    return parseDepartment(node);
  }


  private TreeNode<Department> parseDepartment(JsonNode node)
      throws JsonProcessingException, IOException {
    if (node.has("name")) {
      String name = node.get("name").asText();
      Department department = new Department(name);
      
      if(node.has("id")){
        String id=node.get("id").asText();
        department.setId(id);
      }

      if (node.has("listOffice") && node.get("listOffice").isArray()) {
        List<Office> list = new ArrayList<Office>();
        
        node.get("listOffice").forEach(e -> {
          Gson gson = new Gson();
          String office = e.get("office").asText();
          list.add(gson.fromJson(office, Office.class));
        });
        department.setListOffice(list);
      }

      if(node.has("offices")){
        String officeJson=node.get("offices").asText();
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(TreeNode.class, new TreeOfficeDeserializer());
        mapper.registerModule(module);
        @SuppressWarnings("unchecked")
        TreeNode<Office> offices=mapper.readValue(officeJson, TreeNode.class);
        department.setOffices(offices);
      }

      TreeNode<Department> n1 = new LinkedMultiTreeNode<Department>(department);

      if (node.has("department") && node.get("department").isArray()) {
        ArrayNode departments = (ArrayNode) node.get("department");

        Collection<TreeNode<Department>> backNodes = parseDepartments(departments);
        if (!backNodes.isEmpty()) {
          backNodes.forEach(e -> n1.add(e));
        }

      }
      return n1;
    } else {
      return null;
    }

  }

  private Collection<TreeNode<Department>> parseDepartments(JsonNode arrayNode)
      throws JsonProcessingException, IOException {
    Collection<TreeNode<Department>> a = new HashSet<>();
    if (arrayNode == null)
      return null;
    arrayNode.forEach(e -> {
      try {
        TreeNode<Department> m = parseDepartment(e);
        if (m != null) {
          a.add(m);
        }

      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }
    }

    );
    return a;
  }





}
