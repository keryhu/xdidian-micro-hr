# user用户账户系统  包含了 新地点的管理员，客服人员，所有的注册会员的数据库

新地点的客服人员或者管理人员，需要的service 操作。

  adminConvertUtil：
  
          
      addServiceFormToUser   新地点的管理人员，增加客服人员的时候，需要录入人员的资料信息
        这个对象，转为数据库保存对象，user
        需要录入的信息包含（email，phone，name，密码，），其他的是系统自动生产，
         注册时间，权限（新地点的客服）email，phone激活状态
         
   adminService：
   
      validateAddService  新地点的管理人员，增加客服人员的时候 ，需要验证录入的信息正确型
      
   