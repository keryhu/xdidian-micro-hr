package com.xdidian.keryhu.message.domain;

import com.xdidian.keryhu.domain.message.Subject;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by hushuming on 2016/11/10.
 * 包含 subject enum 和 count 的 对象，这个用在message里面，记录一个id有哪些具体的 subject
 */

@Data
public class SubjectMsg implements Serializable{


    private Subject subject;   // message 的 主题关键字
    private int count;          //  此关键字有多少未读

}
