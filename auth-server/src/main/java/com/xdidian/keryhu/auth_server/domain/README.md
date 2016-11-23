#OAuth2 -server

domain 实体

AuthUserDto  

   用户登录的时候，需要验证的实体，他是通过feign调用user组件返回的结果，包含了用户名，
    密码，role，email激活状态等。
    
JwtOfReadAndWrite

   jwt 设置的一些属性，包含了clientId，clientSecret;等
 
LoginAttemptProperties

   用户尝试登录的一些属性设置，和上面的一样，都是通过yml属性设置，例如限制一共可以登录几次，冷却时间多久。
   
   
   
LoginAttemptUser

   用户尝试登录后，保存在本地的用户数据，包含用户的ip，登录帐户，只有保存了数据库，下次再登录失败才可以统计