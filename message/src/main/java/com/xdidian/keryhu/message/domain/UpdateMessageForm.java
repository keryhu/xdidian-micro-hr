package com.xdidian.keryhu.message.domain;

import com.xdidian.keryhu.domain.message.Subject;
import lombok.Data;

/**
 * Created by hushuming on 2016/11/13.
 *
 *  用户前台点击，，未读消息，促发后台更新 未读消息数量的 form class
 *
 */

@Data
public class UpdateMessageForm {

    private Subject subject;
}
