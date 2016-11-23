# company  人力资源公司的一些基础信息，并且是保存在mongo数据库的。



   
   comapny   是最基本的保存数据库的对象company
   
    CheckCompanyStringItem   
    
      
             * 这是一个基本单位，就是新公司注册以后，，申请人查看注册信息，，新地点的工作人员
             * 审核资料，申请人介绍到被拒绝的申请材料后，再次查看申请材料并且编辑，都需要此 class
             * 他包含了，value，readWrite （0，1），rejectMsg-string
             *
             * 跟这个类型还有一个是 byte【】 类型，方便转换图片。
             
     CheckCompanyByteItem
     
              和上面的类型类似
              
      company 数据库保存对象
             
             
    
   