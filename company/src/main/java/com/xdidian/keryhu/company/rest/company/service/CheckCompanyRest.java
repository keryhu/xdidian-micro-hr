package com.xdidian.keryhu.company.rest.company.service;

import com.xdidian.keryhu.company.client.UserClient;
import com.xdidian.keryhu.company.domain.company.check.CheckCompanySignupInfoDto;
import com.xdidian.keryhu.company.domain.company.check.Reject;
import com.xdidian.keryhu.company.domain.feign.EmailAndPhoneDto;
import com.xdidian.keryhu.company.repository.CompanyRepository;
import com.xdidian.keryhu.company.stream.CheckNewCompanyProducer;
import com.xdidian.keryhu.company.stream.WebsocketAndMessageProducer;
import com.xdidian.keryhu.domain.CheckType;
import com.xdidian.keryhu.domain.company.CheckCompanyDto;
import com.xdidian.keryhu.domain.message.MessageCommunicateDto;
import com.xdidian.keryhu.domain.message.Operate;
import com.xdidian.keryhu.domain.message.ReadGroup;
import com.xdidian.keryhu.domain.message.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;


/**
 * Created by hushuming on 2016/10/9.
 * <p>
 * 新地点的工作人员，审核公司注册资料的 rest
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckCompanyRest {


    private final CompanyRepository repository;
    private final UserClient userClient;
    private final CheckNewCompanyProducer checkNewCompanyProducer;
    private final WebsocketAndMessageProducer websocketAndMessageProducer;


    /**
     * 具体审核公司的post，此路由是 同意公司的注册资料，表明审核通过，发送message，接收方有邮件服务器，手机平台，websocket
     * <p>
     * 通过传递上来的，checkType，判断是同意，还是拒绝，该companyId的数据。
     * <p>
     * <p>
     * 当新公司注册后，新地点的工作人员，
     * 审核通过了公司的注册资料，由company，发出的，消息，
     * 1 给user，通知他更新 user 的权限为 ROLE_COMPANY_ADMIN，更新companyId为新的。（id或email或phone，和companyId）
     * 2 通知邮件服务器，发送审核成功的通知，(email-必需，companyId）。
     * 3 通知手机平台，发送审核成功的通知，（phone--必需，companyId）
     * 4 通知websocket，给对应的userId，发送通知（userId-必需，companyId）
     * <p>
     * 5 如果审核通过，需要更改本地的 company数据库，checked为true，更新checkedTime为当前时间
     * user-account服务器，设置该user的权限为 公司的管理员，更改companyId，。注意提交的是 companyId，数组。
     * <p>
     * <p>
     * 具体审核公司的post，此路由是 拒绝公司的注册资料，并将拒绝的理由保存起来。接收方有邮件服务器，手机平台，websocket。
     * 注意提交的是 companyId，数组。
     * 审核失败了：
     * 1 通知邮件服务器，发送审核失败的通知，(email-必需，companyId）。
     * 3 通知手机平台，发送审核失败的通知，（phone--必需，companyId）
     * 4 通知websocket，给对应的userId，发送失败通知（userId-必需，companyId）
     * <p>
     * 5 如果审核通过，需要更改本地的 company数据库，checked为false，更新checkedTime为当前时间，加上reject保存
     * <p>
     * <p>
     * <p>
     * 提交的格式是：可选项： xx项目，拒绝原因：xxx；这是map<String,String>。
     * 每一条拒绝的理由item不能重复
     */

    @PostMapping("/service/check-company")
    public ResponseEntity<?> agreeCompany(
            @RequestBody CheckCompanySignupInfoDto dto) {


        Assert.notNull(dto.getCompanyId(), "companyId 必填！");
        Assert.isTrue(repository.findById(dto.getCompanyId()).isPresent(),
                "companyId 无效！");
        Assert.notNull(dto.getCheckMethod(), "checkType不能为空");
        // 如果是拒绝了，那么必需提供拒绝的理由
        if (dto.getCheckMethod().equals(CheckType.REJECT)) {

            Assert.notNull(dto.getRejects(), "必需提供拒绝的理由！");
            // 每一个拒绝的理由中，必需提供
            Predicate<Reject> rejectAllNotNull =
                    x -> x.getItem() != null && x.getMessage() != null;
            boolean m = dto.getRejects().stream().allMatch(rejectAllNotNull);
            Assert.isTrue(m, "必需填写拒绝的条目和理由");
            // 拒绝的item不能重复

            boolean unique = dto.getRejects().stream().map(Reject::getItem)
                    .allMatch(new HashSet<>()::add);
            Assert.isTrue(unique, "拒绝的条目不能重复！");
        }


        Map<String, Boolean> map = new HashMap<>();

        // 为什么需要 repostiroy.findById。因为需要通过companyId，查找到他的adminId，
        // 再找到注册申请人的email，phone，这样同意或拒绝申请材料，才可以通知到他

        // 将本地的company对象，转为CheckCompanyDto，方便发送message出去。
        CheckCompanyDto checkCompanyDto = repository.findById(dto.getCompanyId())
                .map(e -> {
                    CheckCompanyDto d = new CheckCompanyDto();
                    String userId = e.getAdminId();
                    EmailAndPhoneDto ep = userClient.getEmailAndPhoneById(userId);
                    d.setEmail(ep.getEmail());
                    d.setPhone(ep.getPhone());
                    d.setCompanyId(e.getId());
                    d.setUserId(userId);
                    d.setCheckType(dto.getCheckMethod());
                    // 如果是同意了申请，那么更新数据库资料
                    if (dto.getCheckMethod().equals(CheckType.AGREE)) {
                        e.setChecked(true);
                    } else {
                        e.setChecked(false);
                        e.setRejects(dto.getRejects());
                    }
                    e.setCheckedTime(LocalDateTime.now());
                    repository.save(e);
                    return d;
                }).orElse(null);


        checkNewCompanyProducer.send(checkCompanyDto);

        // 发送消息提醒 websocket message
        MessageCommunicateDto messageCommunicateDto = new MessageCommunicateDto();

        if (dto.getCheckMethod().equals(CheckType.AGREE)) {
            // 如果审核成功，那么发送成功的消息通知 公司申请人
            messageCommunicateDto.setSubject(Subject.APPROVE_NEW_COMPANY);
            messageCommunicateDto.setOperate(Operate.ADD);
            messageCommunicateDto.setUserId(checkCompanyDto.getUserId());
            messageCommunicateDto.setReadGroup(ReadGroup.INDIVIDUAL);
            websocketAndMessageProducer.send(messageCommunicateDto);

        } else if (dto.getCheckMethod().equals(CheckType.REJECT)) {
            // 如果审核失败，那么发送失败的消息通知 公司申请人
            messageCommunicateDto.setSubject(Subject.REJECT_NEW_COMPANY);
            messageCommunicateDto.setOperate(Operate.ADD);
            messageCommunicateDto.setUserId(checkCompanyDto.getUserId());
            messageCommunicateDto.setReadGroup(ReadGroup.INDIVIDUAL);
            websocketAndMessageProducer.send(messageCommunicateDto);
        }

        //同时待审核公司，也要发送一条减1的message给 新地点的工作人员

        messageCommunicateDto.setSubject(Subject.NEW_COMPANY);
        messageCommunicateDto.setOperate(Operate.MINUS);
        messageCommunicateDto.setReadGroup(ReadGroup.XDIDIAN);
        websocketAndMessageProducer.send(messageCommunicateDto);

        //审核完成，发送结果给前台。
        map.put("result", true);
        return ResponseEntity.ok(map);
    }


}
