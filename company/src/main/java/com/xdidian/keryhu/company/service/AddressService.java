package com.xdidian.keryhu.company.service;

import java.util.List;

import com.xdidian.keryhu.company.domain.address.Address;
import com.xdidian.keryhu.company.domain.address.AreaData;

public interface AddressService {

  public List<AreaData> getProvinces();

  public List<AreaData> getCities(String province);

  public List<AreaData> getCounties(String code);


  // 验证提交的 省份，地级市，县的名字是否是在可选的内容。
  public void validateAddress(Address address);
}

