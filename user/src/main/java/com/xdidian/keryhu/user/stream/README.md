# user用户账户系统  包含了 新地点的管理员，客服人员，所有的注册会员的数据库

使用spirng cloud stream ， user 组件和其他各个组件相互message 的 服务

1  common package
   
   功能：  removeUser，提交的参数是 email、phone或userId，，删除user
      （是 account-activate传递过来的消息，一般是在下面几个情景下会收到这个消息：
      
      新用户注册时候，email激活用户，当激活时间超过时， account-activate 会
      传递消息过来，删除原来注册过的消息。
      
2   company package

    功能： 当新地点的工作人员或者客服人员，审核完公司资料后，不管是审核通过还是不通过，都会
    发出message出去，通知几个服务器。
    接受方包含： mail服务器，手机服务器，websocket，user-account，4个。


3   login package

    功能： 当用户登录系统后，从auth-service 发过来的message，目的，更新用户最后登录时间
    
4  recover package

    功能： 当用户密码找回完成后，更改新的密码，并修改emailStatus为true（如果原来为false）
    
 
5  signup package

      acctivatedSuccess ，功能： 用户注册完或者用户资料修改后，验证码验证成功后，由
             account-activate 发送来的message，，user更新用户资料并保存
             
      signup   功能，保存新注册的用户数据到数据库，message从signup 服务器发送过来。