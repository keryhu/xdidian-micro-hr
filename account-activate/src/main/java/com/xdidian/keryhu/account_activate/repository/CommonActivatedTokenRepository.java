package com.xdidian.keryhu.account_activate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.xdidian.keryhu.account_activate.domain.CommonActivatedLocalToken;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;

public interface CommonActivatedTokenRepository extends MongoRepository<CommonActivatedLocalToken, String>{
  
  public List<CommonActivatedLocalToken> findByAccount(String account);
  public Optional<CommonActivatedLocalToken> findByUserId(String userId);
  public Optional<CommonActivatedLocalToken> findByAccountAndApplySituation(String account, ApplySituation applySituation);

}
