package com.xdidian.keryhu.account_activate.domain;

import java.io.Serializable;

import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.RecoverMethod;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
*  FormTokenDto
* 这个是 通用的，验证前台 输入的 account 和dto，是否符合要求
* token和account是否匹配，token是否匹配。)
* @author keryhu  keryhu@hotmail.com
* @date 2016年9月9日 下午2:54:16
 */

@Data
@NoArgsConstructor
public class CommonConfirmTokenDto implements Serializable{
 
  private static final long serialVersionUID = -5447564615482843816L;
  private String userId;  //可选，只有当用户资料修改，修改email或phone的时候，才会出现这个。
  private String account;
  private String token; 
  
  //为大些的EMAIL OR PHONE
  private RecoverMethod  method;   //可选，当密码找回的时候，前台需要传递回来，是通过哪种method找回密码。其值只能为EMAIL或PHONE

  private ApplySituation applySituation;  //应用场景，限制为 SIGNUP / RECOVER /EDIT
}
