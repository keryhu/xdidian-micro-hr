#OAuth2 -server

security 的配置package

CustomAuthenticationProvider

     实现了AuthenticationProvider接口，用来判断用户的用户名，密码是否输入的正确，
     在这里还实现了： 如果输入错误，进行ipBlock 的记录，+1，如果正确，则清零。
     
     同时还判断，用户的email有没有激活的处理。如果没有激活则报错。
     
     如果密码错误也报错给前台。
     
CustomTokenEnhancer

    自定义jwt 返回给前台 价值额外的信息，例如此处设置了，加载额外的userId
    
CustomAuthenticationFailureHandler
    
    httpServletRequest.getSession() 使用这个记录错误信息，将他转移到 login页面
    

CustomAuthSuccessHandler

  通过判断savedRequest，是否为null，查看用户是直接登录login，还是登录了一个secured 页面，被
 * redirect login页面的，如果是第二种情况，那么用户登录后，还会返回到之前的secured页面，如果是
 * 第一种情况，那么根据用户的role权限来决定登录后的页面。
 * 此功能已经实现，angular2 前台无需定义此功能了