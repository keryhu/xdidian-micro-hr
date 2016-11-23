# company  人力资源公司的一些基础信息，并且是保存在mongo数据库的。


所有有关company的 domain

   create package  是创建公司的domain
   
   component package  是创建公司的组件，例如： 行业，和公司性质，都是有限的数组。
   
   feign package  是调用其他的服务器的 dto，
   
         EmailAndPhoneDto 调用user服务器的组件，返回email和phone
         
         
   check package 是新地点的管理人员或客服审核注册公司的材料，所需要的实体
   
   common  package  最基本的company domain
   
   -----------------------------------------------------------------------------
         
   address package 是包含省份，市县的地址。 areaData 是从统计局下载的全国的地址数据（code，name）
   
   
 
   
   constan  是常量数组，目前里面有一个  UN_CHECKED_COMPANY_KEYS 未审核的公司的数组keys
   
   另外的是未完全  做完的domain
   
    department  是部门
    office   是员工的职位
    staff    包含员工基本信息。
   
   