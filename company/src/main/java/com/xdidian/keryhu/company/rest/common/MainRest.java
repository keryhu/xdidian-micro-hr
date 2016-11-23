package com.xdidian.keryhu.company.rest.common;

import com.xdidian.keryhu.company.domain.address.Address;
import com.xdidian.keryhu.company.domain.company.component.CompanyIndustry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xdidian.keryhu.company.domain.Department;
import com.xdidian.keryhu.company.domain.Office;
import com.xdidian.keryhu.company.repository.CompanyRepository;
import com.xdidian.keryhu.company.service.ConvertUtil;
import com.xdidian.keryhu.tree.LinkedMultiTreeNode;
import com.xdidian.keryhu.tree.TreeNode;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class MainRest {


  private final ConvertUtil convertUtil;
  private final CompanyRepository repository;

  @GetMapping("/query/2")
  public Department d() {

    TreeNode<Office> o1 = new LinkedMultiTreeNode<Office>(new Office("2222"));
    TreeNode<Office> o2 = new LinkedMultiTreeNode<Office>(new Office("3333"));
    TreeNode<Office> o3 = new LinkedMultiTreeNode<Office>(new Office("4444"));
    TreeNode<Office> o4 = new LinkedMultiTreeNode<Office>(new Office("5555"));
    o1.add(o2);
    o1.add(o3);
    o2.add(o4);
    Department d = new Department("nn");
    d.setOffices(o1);
    return d;

  }


  @GetMapping("/query/3")
  public ResponseEntity<?> t(){
    Address a=new Address();
    a.setProvince("北京市");
    a.setCity("市场区");
    a.setCounty("嘉定区");
    return ResponseEntity.ok(a);
  }


  @PostMapping("/query/4")
  public void r(@RequestBody Address address){
    log.info(String.valueOf(address));
  }




  @GetMapping("/query/6")
  public ResponseEntity re(){
    return ResponseEntity.ok(CompanyIndustry.CAI_KUANG);
  }

  @PostMapping("/query/5")
  public void rew(@RequestParam("c") CompanyIndustry c){
    log.info(c.toValue());
  }


  
  
}
