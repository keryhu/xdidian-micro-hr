package com.xdidian.keryhu.company.domain.company.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by hushuming on 2016/11/6.
 * 这是一个基本单位，就是新公司注册以后，，申请人查看注册信息，，新地点的工作人员
 * 审核资料，申请人介绍到被拒绝的申请材料后，再次查看申请材料并且编辑，都需要此 class
 * 他包含了，value，readWrite （0，1），rejectMsg-string
 *
 * 跟这个类型还有一个是 String 类型，方便转换图片。
 *
 */

@Data
@NoArgsConstructor
public class CheckCompanyByteItem implements Serializable {
    private byte[] value;   // 这里类型，string
    private int readWrite;  // 0 =read  1=write
    private String rejectMsg;
}