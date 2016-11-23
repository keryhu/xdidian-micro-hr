package com.xdidian.keryhu.company.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.xdidian.keryhu.company.domain.company.common.Company;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author keryhu  keryhu@hotmail.com
 * @ClassName: CompanyRepository
 * @Description: TODO(公司信息，包含了公司名字，公司地址，公司管理人员，公司部门，公司职位。)
 * @date 2016年8月14日 下午9:45:15
 */

@RepositoryRestResource(collectionResourceRel = "company", path = "companies",
        exported = false)
public interface CompanyRepository extends MongoRepository<Company, String>,
        QueryDslPredicateExecutor<Company> {

    Optional<Company> findByName(String name);

    Optional<Company> findById(String id);

    List<Company> findByAdminId(String adminId);


    Page<Company> findByName(String name, Pageable pageable);

    Page<Company> findByNameLike(String name, Pageable pageable);
}
