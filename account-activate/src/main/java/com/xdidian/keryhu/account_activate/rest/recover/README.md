密码找回的rest

CheckMethod  

   "/accountActivate/recover/checkMethod"  提交的参数是： AccountMethodDto
   包含了参数： 具体的帐户account和method
   
    当用户在，密码重置，选择何种 方法 找回密码 的页面，所遇到的rest服务
    * 
    *               1 判断account 是否为email，或 phone，是否存储在于数据库，且判断method是否一致。 2
    *               验证email，是否已经激活,不管email是否激活，如果后期密码重设成功了，则将email激活改为true 3 返回前台，method，account，
    *               resendtoken，前台收到后导航到 由前面参数组成的url页面，填写验证码
    

NewPasswordRest

    "/accountActivate/recover/newpassword"  提交的参数是 NewPasswordFormDto
    包含了参数： 帐户，token，新的密码 
    
    用户输入新密码的时候的rest，为了验证，必需要提交之前的account，token，
    
   
   
ResolveAccountAndTokenExist

    "/accountActivate/recover/isAccountAndTokenExist"  
    此服务用在，显示密码找回，需要输入新密码的页面。
     * 前台在 显示路由该页面之前，需要先查看下，该account和token是否存在，是否还在有效期，如果都在，就停留在该页面，
     * 如果已经失效，或者account不存在，那么就返回账号失效，给前台。并且删除本地的账号。
     
     