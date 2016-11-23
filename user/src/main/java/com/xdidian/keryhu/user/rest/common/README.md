# user用户账户系统  包含了 新地点的管理员，客服人员，所有的注册会员的数据库

rest 

 freeQueryRest:  不需要验证用户权限的rest。所有用户都可以调用的接口。
     分为下面的rest：
     
     "/users/query/isEmailExist"   前台查看用户email是否存在
     "/users/query/isPhoneExist"   前台查看用户phone是否存在
    "/users/query/emailStatus"   参数： emailOrPhone，，查看用户emailStatus
    "/users/query/isLoginNameExist"  参数 emailOrPhone，，查看用户emailOrPhone，是否存在
    "/users/query/phoneStatus"  参数： emailOrPhone，，查看用户phoneStatus
               
     /users/query/getEmailAndPhone  参数： emailOrPhone  返回email和phone，
        用在密码找回的时候，输入email或phone，返回email和phone，
        方便用户选择哪种方式找回密码
     
     
  feignRest： 是其他服务器，需要feign调用user rest接口的时候，需要的
  
     "/users/query/findByIdentity"  为了auth-server
                 目的： 用户登录时候，根据提交的 帐户，密码，验证用户正确与否。
     
     "/users/query/findById" 用在account－activated 激活的时候，feign user服务器
     
     需要用户登录才能获取
     /users/getIsInCompanyAndName   用在side-menu service，
        access-token，因为feign调用此接口，需要用户登录，获取需要access-token 的header
        
        提供的参数： 另外就是 用户的userid，
       
     需要用户登录才能获取
    /users/getHashPassword   提供的参数userId，
    返回的是用户加密后的密码。
    
    
    "users/emailAndPhone"  request 参数是 userID，access-token header spring feign调用
    用在company 服务 组件，因为在公司注册完后，新地点的人员审核注册的资料时候，当审核通过或者不通过的时候，
            都需要发送email通知和手机消息通知给  注册人，
            所以需要根据用户的id，知道他的email和手机号
     

     "users/findCurrentName" pc gateway 使用，登录后，返回给前台当前用户的姓名
     
     
  需要用户登录后才可以操作，
    
  UploadUserHeaderRest 
         " /users/personalInfo/uploadHeader"     
         用户上传头像的rest，包含更改头像，上传的是图片。保存到本地，
         保存的路径，存放到数据库
         
  securedRest 需要用户登录后，才能获取的 common rest，
     目前只做了一个，还是初期，前台还未需要。
     "/users/userInfo" 用在用户登录后，现在在首页的个人资料，包含公司的一些基础资料，
         后者查看他人的资料。。
         需要的参数是  userId，不是email，phone
 
     
  UserEditRest   用户个人资料修改的时候，需要的rest 所以的都需要用户登录后才可以修改
  
    "/users/edit/name"  用户修改姓名，查看姓名是否能修改的rest，
    
    "/users/edit/birthday"  用户修改生日，
    
    "/users/edit/password"  用户修改密码。
    
    "/users/edit/info"   用户进入个人资料修改页面的时候，需要首先调用用户的基本资料的rest
