# company  人力资源公司的一些基础信息，并且是保存在mongo数据库的。


UserClient
    
      isIdExist   feign user 服务器，查userId是否存在
      
      getEmailAndPhoneById   通过userId，返回email和phone，在公司资料审核的时候，
      如果审核完了，需要知道提交人的email和phone，这样才能发送message通知对方。