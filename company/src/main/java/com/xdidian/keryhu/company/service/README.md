# company  人力资源公司的一些基础信息，并且是保存在mongo数据库的。


addressService

    getProvinces   获取全部的省份。
    
    getCities  根据提交的省份的code，获取 该省份的 市（code，name对象）
    
    getCounties 根据提交的市的code，获取县（code，name）
    
    
    validateAddress  验证地址信息是否正确
    
    readArea   读取本地的 全国省份的txt文件
    
    
companyService

    getDepartment   获取部门的service 以后移到其他的java
    
    validateNewCompanyPost   验证新公司注册提交的材料
    
    findUncheckedCompany    查询未审核的公司  以后用page qdsl做
    
    isKeyExistInRejectCompany   判断提交的key是否在未审核的公司中
    
    defaultDepartment   移植到 department service