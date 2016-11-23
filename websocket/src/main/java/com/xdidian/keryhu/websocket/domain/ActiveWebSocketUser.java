package com.xdidian.keryhu.websocket.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


import java.util.Calendar;

/**
 * Created by hushuming on 2016/11/16.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveWebSocketUser {

    @Id
    private String id;

    private String userId;

    private Calendar connectionTime;
}
