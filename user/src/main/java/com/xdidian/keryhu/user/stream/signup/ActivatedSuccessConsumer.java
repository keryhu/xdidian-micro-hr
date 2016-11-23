package com.xdidian.keryhu.user.stream.signup;

import com.xdidian.keryhu.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import com.xdidian.keryhu.domain.tokenConfirm.ApplySituation;
import com.xdidian.keryhu.domain.tokenConfirm.CommonTokenDto;

import static com.xdidian.keryhu.util.StringValidate.isEmail;
import static com.xdidian.keryhu.util.StringValidate.isPhone;


/**
 * :这个消息通知是一个公用的方法，是在token验证成后，出现的方法，出现的场景有3个，判断依据是： CommonTokenDto 的ApplySituation值
 * 1  注册完，email激活 ，如果在此情况下，只有userService接受此消息，并且作处理
 * 查询email或pone所在的user，修改emailStatus 或phoneStatus为 true。
 * 2  recover 不作处理
 * 3  个人资料修改，接受消息的人是，user service，根据提供的userId，将新的account，更新到对应的id下面，且设置status为true
 */
@EnableBinding(ActivatedSuccessInputChannel.class)
@Slf4j
public class ActivatedSuccessConsumer {

    @Autowired
    private UserRepository repository;

    @StreamListener(ActivatedSuccessInputChannel.NAME)
    public void activateSuccess(final CommonTokenDto dto) {
        if (dto.getApplySituation().equals(ApplySituation.EDIT)) {
            repository.findById(dto.getUserId())
                    .ifPresent(e -> {
                        if (isEmail(dto.getAccount())) {
                            e.setEmail(dto.getAccount());
                            e.setEmailStatus(true);
                            repository.save(e);
                            log.info("已经更新了account " + dto.getAccount());
                        } else if (isPhone(dto.getAccount())) {
                            e.setPhone(dto.getAccount());
                            e.setPhoneStatus(true);
                            repository.save(e);
                            log.info("已经更新了account " + dto.getAccount());
                        }
                    });
        } else if (dto.getApplySituation().equals(ApplySituation.SIGNUP)) {
            log.info("dto.getAccount is : " + dto.getAccount());
            if (!(dto.getAccount() == null || dto.getAccount().isEmpty())) {
                repository.findByEmailOrPhone(dto.getAccount(), dto.getAccount())
                        .ifPresent(e -> {
                            if (isEmail(dto.getAccount())) {
                                e.setEmailStatus(true);
                            }
                            if (isPhone(dto.getAccount())) {
                                e.setPhoneStatus(true);
                            }
                            repository.save(e);
                            log.info("user-account 成功激活 {} 的user  ",
                                    dto.getAccount());
                        });
            }
        }

    }

}
