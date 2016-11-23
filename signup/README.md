# 用户注册（signup）

一 、 第五个启动的service，

二 、 物业公司注册后台程序流程：

  1 、链接web 的class  propertyForm 类将用户web注册数据搜集起来
    
   2 、然后传给signupDto，
   
   3 、 验证体检的数据的合法性，是否符合email，phone，password规范，查询是否已经注册过，如果满足条件，执行下一步
   
   4 、 将注册的信息打包成message dto 利用spring cloud stream 发送给user-account 
   
   5 、 创建email激活的dto，设定随机码和到期时间，发送message给email-activate 和mail－server,前者接受到消息后，存储数据库，后者实现发送邮件的功能
   
   6 、 前台页面附带参数，跳转到 emailActivate页面，让客户选择后续的操作，是重新发送邮件，还是重新注册。
   
三 、 
