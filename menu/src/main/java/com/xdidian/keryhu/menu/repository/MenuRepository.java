package com.xdidian.keryhu.menu.repository;

import java.util.Optional;

import com.xdidian.keryhu.menu.domain.core.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface MenuRepository extends MongoRepository<Menu, String> {
  
  public Optional<Menu> findByUserId(String userId);

}
