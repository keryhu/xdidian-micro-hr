/**
 * @Title: LoginAttemptUserRepository.java
 * @Package com.xdidian.keryhu.authserver.repository
 * @Description: TODO(用一句话描述该文件做什么)
 * @author keryhu keryhu@hotmail.com
 * @date 2016年4月30日 下午1:55:49
 * @version V1.0
 */
package com.xdidian.keryhu.auth_server.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xdidian.keryhu.auth_server.domain.LoginAttemptUser;

import java.util.Optional;

/**
 * 
 *  记录LoginAttemptUser的 mongo data rest repository
 * 2016年6月18日 下午7:57:31
 * @author : keryHu keryhu@hotmail.com
 */
public interface LoginAttemptUserRepository extends MongoRepository<LoginAttemptUser, String> {

  /**
   * 根据remoteIp寻找系统的记录
   */
  public Optional<LoginAttemptUser> findByRemoteIp(String remoteIp);


  /**
   * 根据LoginAttemptUser本身的id来查询。
   */
  public Optional<LoginAttemptUser> findById(String id);

  /**
   * 如果userId存在的情况下，通过此参数查询数据库
   */
  public Optional<LoginAttemptUser> findByUserId(String userId);


}
