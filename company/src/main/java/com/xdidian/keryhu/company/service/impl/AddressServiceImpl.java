package com.xdidian.keryhu.company.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.xdidian.keryhu.company.domain.address.Address;
import com.xdidian.keryhu.company.service.AddressService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.xdidian.keryhu.company.domain.address.AreaData;
import org.springframework.util.Assert;


@Component("addressService")
public class AddressServiceImpl implements AddressService {

    /**
     *
     * Description: 获取所有的省份，直辖市
     * @return
     */
    @Override
    public List<AreaData> getProvinces() {
        return readArea().stream().filter(e -> e.getCode().endsWith("0000"))
                .collect(Collectors.toList());

    }


    /**
     *
     * getCities
     * 只有提交的是省份代码的code，程序才会有正确的执行。
     * 此方法是筛选出复合 参数为省份的 的下面的直辖市)
     */
    @Override
    public List<AreaData> getCities(String code) {

        //首先参数code，必需是6为数字组合，且最后4位为0

        if(!isProvince(code)){
            return null;
        }

        String prefix=code.substring(0, 2);

        return readArea().stream()
                .filter(e -> e.getCode().startsWith(prefix)&&isCity(e.getCode()))
                .collect(Collectors.toList());
    }

    /**
     *
     * Title: getCounties
     * Description: 根据传递上来的，参数，市级code，返回当前市，所有的县
     */


    @Override
    public List<AreaData> getCounties(String code) {
        if(!isCity(code)){
            return null;
        }
        String prefix=code.substring(0, 4);
        return readArea().stream()
                .filter(e -> e.getCode().startsWith(prefix)&&!e.getCode().endsWith("00"))
                .collect(Collectors.toList());
    }

    // 验证提交的 省份，地级市，县的名字是否是在可选的内容。
    @Override
    public void validateAddress(Address address) {

        // 验证 省份名字的正确性
        boolean checkProvince=getProvinces().stream()
                .anyMatch(e->e.getName().equals(address.getProvince()));

        Assert.isTrue(checkProvince,"省份，直辖市错误");

        String provinceCode=getProvinces().stream()
                .filter(e->e.getName().equals(address.getProvince()))
                .findFirst()
                .orElse(null)
                .getCode();



        boolean checkCity= getCities(provinceCode).stream()
                .anyMatch(e->e.getName().equals(address.getCity()));

        Assert.isTrue(checkCity,"地级市名字错误");

        String cityCode=getCities(provinceCode).stream()
                .filter(e->e.getName().equals(address.getCity()))
                .findFirst()
                .orElse(null)
                .getCode();

        boolean checkCounty=getCounties(cityCode).stream()
                .anyMatch(e->e.getName().equals(address.getCounty()));

        Assert.isTrue(checkCounty,"县名字有误");



    }


    private List<AreaData> readArea() {
        // 此文件更新 于 2015年9月30日
        Resource resource = new ClassPathResource("static/area.txt");
        List<AreaData> list = new ArrayList<>();
        try {
            File a = resource.getFile();
            if (!a.exists() || a.isDirectory()) {
                return null;
            }
            BufferedReader br = new BufferedReader(new FileReader(a));

            String line = null;
            while ((line = br.readLine()) != null) {
                String code = line.substring(0, 6);
                String name = line.substring(6, line.length()).replaceAll("[^\u4e00-\u9fa5]", "");
                AreaData data = new AreaData(code, name);
                list.add(data);
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;

    }

    private boolean isProvince(String code){
        String regex="^[1-9][0-9]0000$";   //因为重庆的代码是  500000
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(code);
        return m.matches();
    }

    private boolean isCity(String code){
        String regex="^[1-9][0-9][0-9]{2}00$";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(code);
        return m.matches()&&!code.endsWith("0000");
    }

}