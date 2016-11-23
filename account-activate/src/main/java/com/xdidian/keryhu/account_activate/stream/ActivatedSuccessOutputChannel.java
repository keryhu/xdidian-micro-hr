package com.xdidian.keryhu.account_activate.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;


/**
 * @Description : 
 * 这个消息通知是一个公用的方法，是在token验证成后，出现的方法，出现的场景有3个，判断依据是： CommonTokenDto 的ApplySituation值
 * 1  注册完，email激活 ，如果在此情况下，只有userService接受此消息，并且作处理 
 *     查询email或pone所在的user，修改emailStatus 或phoneStatus为 true。
 * 2  recover 不作处理
 * 3  个人资料修改，接受消息的人是，user service，根据提供的userId，将新的account，更新到对应的id下面，且设置status为true
 * 
 * 
 * @date : 2016年6月18日 下午9:01:03
 * @author : keryHu keryhu@hotmail.com
 */
public interface ActivatedSuccessOutputChannel {

    //此channel的值和 application bindings下面的值一致
    String NAME = "activatedSuccessOutputChannel";

    @Output(NAME)
    MessageChannel success();

}
