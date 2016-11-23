package com.xdidian.keryhu.user.stream.company;


import com.xdidian.keryhu.domain.CheckType;
import com.xdidian.keryhu.domain.Role;
import com.xdidian.keryhu.domain.company.CheckCompanyDto;

import com.xdidian.keryhu.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;


/**
 * Created by hushuming on 2016/10/10.
 * <p>
 * 当新公司注册完成后，新地点的工作人员审核完材料后，将审核完的结果从company——info，发送出来，
 * 接受方包含： mail服务器，手机服务器，websocket，user-account，4个。
 * <p>
 * user-account服务器接受到消息后，根据checkType，判断是agree还是reject，
 * 如果是agree，则更新此userId对应的role权限为 company-admin，更新companyId
 * 如果是reject，不做任何处理
 */

@EnableBinding(CheckNewCompanyInputChannel.class)
@Slf4j
public class CheckNewCompanyConsumer {

    @Autowired
    private UserRepository repository;

    @StreamListener(CheckNewCompanyInputChannel.NAME)
    public void receive(CheckCompanyDto dto) {
        log.info(String.valueOf(dto));
        if (dto != null) {
            if (dto.getCheckType().equals(CheckType.AGREE)) {

                repository.findById(dto.getUserId())
                        .ifPresent(v -> {
                            v.addCompanyId(dto.getCompanyId());
                            v.addRole(Role.ROLE_COMPANY_ADMIN);
                            repository.save(v);
                        });
                log.info("user-account 接受到company_info传递过来的审核通过的公司资料，" +
                        "现在将该adminId转为company-admin，并且设置companyId");

            }


        }
    }

}


