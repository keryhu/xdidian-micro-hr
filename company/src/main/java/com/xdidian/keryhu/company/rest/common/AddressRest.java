package com.xdidian.keryhu.company.rest.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xdidian.keryhu.company.service.AddressService;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;


/**
 * 获取全国，省份的 直辖市, 自治区)
 *
 * @date 2016年8月15日 下午3:40:25
 */


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressRest {

    private final AddressService addressService;

    //用在 公司新建的时候，写地址信息，还有  公司信息修改的时候，，修改地址
    @GetMapping("/address/provinces")
    public ResponseEntity<?> getProvinces() {

        return ResponseEntity.ok(addressService.getProvinces());
    }


    /**
     * getCites
     * 获取当前参数code，所在 地级市，地区，自治州，盟)
     */


    @GetMapping("/address/cities")
    public ResponseEntity<?> getCites(@RequestParam("code") String code) {

        return ResponseEntity.ok(addressService.getCities(code));
    }


    /**
     * getAreas
     * 根据 地级市，地区，自治州，盟所在的code，返回当前的市辖区，县级市，县)
     */


    @GetMapping("/address/counties")
    public ResponseEntity<?> getCounties(@RequestParam("code") String code) {

        return ResponseEntity.ok(addressService.getCounties(code));
    }



}
