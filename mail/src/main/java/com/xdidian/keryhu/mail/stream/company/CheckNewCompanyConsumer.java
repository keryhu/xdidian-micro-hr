package com.xdidian.keryhu.mail.stream.company;


import com.xdidian.keryhu.domain.CheckType;
import com.xdidian.keryhu.domain.company.CheckCompanyDto;
import com.xdidian.keryhu.mail.domain.Host;
import com.xdidian.keryhu.mail.mail.EmailHtmlSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.thymeleaf.context.Context;


/**
 * Created by hushuming on 2016/10/10.
 * <p>
 * 当新公司注册完成后，新地点的工作人员审核完材料后，将审核完的结果从company——info，发送出来，
 * 接受方包含： mail服务器，手机服务器，websocket，user-account，4个。
 * <p>
 * mail服务器接受到消息后，根据checkType，判断是agree还是reject，写不同的主题邮件，
 * 然后将审核通过／审核拒绝的 消息发送给 email账号。
 */

@EnableBinding(CheckNewCompanyInputChannel.class)
@Slf4j
public class CheckNewCompanyConsumer {

    @Autowired
    private Host host;

    @Autowired
    private EmailHtmlSender mailSender;

    @StreamListener(CheckNewCompanyInputChannel.NAME)
    public void receive(CheckCompanyDto dto) {

        if (dto != null) {
            final Context ctx = new Context();
            String type = "";
            String content = "";  // 邮件的主要内容

            String url = new StringBuffer(host.getHostName())
                    .append(":8080/profile/new-company")
                    .toString();

            if (dto.getCheckType().equals(CheckType.AGREE)) {
                type = "公司注册审核通过";
                content = "恭喜您，提交的公司材料已通过申请！现在就可以进入：";

            } else if (dto.getCheckType().equals(CheckType.REJECT)) {
                type = "公司注册审核失败";
                content = "很遗憾！您提交的公司材料未被通过，详细了解：";
            }
            String subject = "新地点－" + type;

            ctx.setVariable("content", content);
            ctx.setVariable("url", url);
            ctx.setVariable("email", dto.getEmail());

            mailSender.send(dto.getEmail(), subject, "checkCompany", ctx);
        }


    }


}
