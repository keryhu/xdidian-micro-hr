# company  人力资源公司的一些基础信息，并且是保存在mongo数据库的。


config  配置package

    createDir
      
      创建 公司营业执照，和介绍信的 本地保存地址。如果没有此文件夹，需要系统启动时创建
      
    
       
       
converter

     是对象tree ，存储mongo，读写的convert的配置。
     
json  package
     是自定义的 部门，职位的 序列化和反序列化
     
propertiesConfig

    是通过yml配置文件来设置的配置文件
    
    imageResizeProperties
        
           图片，java resize 的配置文件，就是修改成多大的文件。限制了上传文件的最大多少。
           
    NewCompanyProperties
    
             当创建新公司的时候，需要用到的属性，这里设置了，，一个用户最多能够创建几个公司帐户。