package com.xdidian.keryhu.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.xdidian.keryhu.user.domain.User;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 * @author : keryHu keryhu@hotmail.com
 * @Description : user 的数据库操作
 * @date : 2016年6月18日 下午9:20:55
 */

@RepositoryRestResource(collectionResourceRel = "user", path = "users", exported = false)
public interface UserRepository extends MongoRepository<User, String>,
        QueryDslPredicateExecutor<User> {


    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByPhone(@Param("phone") String phone);

    Optional<User> findById(@Param("id") String id);

    List<User> findByName(@Param("name") String name);

    Optional<User> findByEmailOrPhone(@Param("email") String email,
                                      @Param("phone") String phone);


    Optional<User> findByEmailOrPhoneOrId(@Param("email") String email,
                                          @Param("phone") String phone,
                                          @Param("id") String id);

    @PreAuthorize("hasRole('ROLE_SOME_DEPARTMENT')")
    List<User> findByBirthdayAndCompanyIds(@Param("birthday") LocalDate birthday,
                                           @Param("companyId") List<String> companyIds);


    // 根据email或phone或name,或userId， 搜索用户，返回page 对象。 新地点的客服人员使用
    Page<User> findByEmailOrPhoneOrNameOrIdAllIgnoreCase(
            String email, String phone, String name, String id,
            Pageable pageable);


}
