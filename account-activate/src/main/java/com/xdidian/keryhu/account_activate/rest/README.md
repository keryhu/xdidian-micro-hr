AccountEditRest  

    "/accountActivate/edit"   提交的参数：FormAccountEditDto 和header access-token
    
    修改email 或phone验证完可以修改email的后，在发送message出去之前，先要判断下，
    account activated 数据库是否已经存在了该account
      // ,且该token没有过期，如果存在此情况，那么直接返回前台，
      想要的resendtoken，让前台转动输入token的页面
      
      
ConfirmTokenRest 

    "/accountActivate/confirmToken"  提交的参数： 
    
    这是一个通用的 验证码提交，验证 验证码token的 post 方法，3合一
         * 适用于  密码找回，注册完的email验证，个人资料修改。只需要按照 参数提交即可。
         
         
ForLoginRest

     "/query/emailActivate"  提交的参数： email
     
     login的后台，后台遇到email未激活的用户，调用此方法
     
ResendRest

    "/accountActivate/resend"   提交的参数--CommonConfirmTokenDto
    
    激活时候，重新发送验证码的 rest
    
    
ResignupRest

    "/accountActivate/resignup"   提交的参数 CommonConfirmTokenDto
    只有在用户注册完，email激活的时候，才有用，是他独有的