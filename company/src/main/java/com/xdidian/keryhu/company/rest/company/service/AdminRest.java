package com.xdidian.keryhu.company.rest.company.service;

import com.mysema.commons.lang.Assert;
import com.querydsl.core.types.Predicate;
import com.xdidian.keryhu.company.domain.company.common.Company;
import com.xdidian.keryhu.company.domain.company.common.QCompany;
import com.xdidian.keryhu.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created by hushuming on 2016/10/29.
 * <p>
 * 新地点的管理人员，对于company 组件的操作。
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class AdminRest {


    private final CompanyRepository repository;
    private final MessageSource messageSource;

    /**
     * 新地点的客服人员，根据公司的名字 搜索公司信息，这里的content指 公司名字 关键字
     */
    @GetMapping("/admin/queryCompanyWithPage")
    public Page<Company> get(

            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
                    Pageable pageable,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "registerTimeBegin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime registerTimeBegin,
            @RequestParam(value = "registerTimeEnd", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime registerTimeEnd) {

        boolean contentNotNull = content != null;
        boolean registerTimeNotNull = registerTimeBegin != null
                && registerTimeEnd != null;

        Predicate contentPredicate = null;
        Predicate registerTimePredicate = null;

        // content is not null, registerTime  is not null
        Predicate contentAndRegisterTimePredicate = null;

        String err = null;
        if (registerTimeNotNull) {
            err = messageSource.getMessage("message.adminRest.regsterTimeBeginMustBeforeEnd",
                    null, LocaleContextHolder.getLocale());

            Assert.isTrue(registerTimeBegin.isBefore(registerTimeEnd), err);
        }


        QCompany company = new QCompany("company");

        if (contentNotNull) {
            contentPredicate = company.name.contains(content);
        }

        if (registerTimeNotNull) {
            registerTimePredicate = company.registerTime.after(registerTimeBegin)
                    .and(company.registerTime.before(registerTimeEnd));
            if(contentNotNull){
                contentAndRegisterTimePredicate=company.name.contains(content)
                        .and(registerTimePredicate);
            }
        }


        if (content == null) {
            // content is null  registerTimeNotNull
            if (registerTimeNotNull) {
                return repository.findAll(registerTimePredicate,pageable);
            }
            // content is null registerTime is Null
            else {
                return repository.findAll(pageable);
            }

        }
        // content not null
        else {
            // content not null   registerTimeNotNull
            if (registerTimeNotNull) {
                return repository.findAll(contentAndRegisterTimePredicate,pageable);
            }
            // content not null    registerTime is Null
            else {
                return repository.findAll(contentPredicate,pageable);
            }
        }

    }
}
