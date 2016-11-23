#OAuth2 -server

client  此为 auth-server 调用其他组件服务器的 spring feign 的应用

1  AccountActivateClient

      auth server 远程调用 account-activate 的 spring feign 方法
       * 用在： 当用户用户登录的时候，检查用户的emailStatus如果为false，那么还需要检查该用户的
       * 激活时间有没有过期，如果过了，那么就删除原来的数据，用户需要重新注册，这个结果需要返回给前台，
       * 如果没有过期，那么就导航到email激活，要求输入验证码的页面。
       
       
2  UserClient

     针对于user-account service remote rest 服务
      *   用在： 用户登录的时候，验证用户名，密码的正确与否，需要feign调用user服务器。