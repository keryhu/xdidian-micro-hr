package com.xdidian.keryhu.company.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xdidian.keryhu.company.config.json.TreeOfficeDeserializer;
import com.xdidian.keryhu.company.config.json.TreeOfficeSerializer;
import com.xdidian.keryhu.tree.TreeNode;

import lombok.Data;
/**
 * 
* @ClassName: Department
* @Description: 企业部门的pojo，最底层是具体的职位名字。例如：实习生，人事专员，销售经理等)
* @author keryhu  keryhu@hotmail.com
* @date 2016年7月27日 下午6:10:04
 */


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Department implements Serializable{

	private static final long serialVersionUID = 7229242816438511357L;
	
	
	private String id;   //department id ,用来区分不同的部门或者官职，例如市场部，销售经理，有着不同的id
	
	
	private String name;   //部门的名字
	
	//当用户 创建部门的职位的时候，在未给他们添加上下级的时候，首先将这些初次创建的 职位保存为数组，只有当建立了上下级，
    //  才保存为上面的treeNode 关系
    private List<Office> listOffice=new ArrayList<Office>();
    
	
	@JsonSerialize(using=TreeOfficeSerializer.class)
	@JsonDeserialize(using=TreeOfficeDeserializer.class)
	private TreeNode<Office>   offices; //职位，，此部门由哪些职位组成，这里不能有重复的出现,另外这是一个tree类型
	 
	
	public Department(){
		super();
	}
	
	public Department(String name){
		this();
		this.setName(name);
		this.id=UUID.randomUUID().toString();
	}
	
	

}
