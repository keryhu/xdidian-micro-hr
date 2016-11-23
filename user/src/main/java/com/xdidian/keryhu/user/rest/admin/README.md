# user用户账户系统  包含了 新地点的管理员，客服人员，所有的注册会员的数据库

新地点的管理人员 ，特有的rest  ，全部需要管理员权限。

  1  /admin/add-service 
  
     管理人员，录入新的客服人员，的rest。
     
  2  /admin/queryByName    返回page对象
  
     管理人员，根据新地点的客服人员的名字，查询客服人员。
     
  3  "/admin/delById"
  
     管理人员，根据新地点的客服人员的id，删除客服
     
  4  /admin/queryWithPage  返回的是page对象
  
     管理人员，根据注册时间，最后登录时间，姓名，email，手机号 userId，查找会员。




