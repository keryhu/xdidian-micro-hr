package com.xdidian.keryhu.user.domain.edit;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

/**
 * 当前台用户修改密码的时候，前台创建的dto
 * <p>
 * Created by hushuming on 16/9/14.
 */

@Data
@NoArgsConstructor
public class ChangePasswordDto implements Serializable {
    private String id;
    private String originPassword;
    private String newPassword;

}
