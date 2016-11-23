# user用户账户系统  包含了 新地点的管理员，客服人员，所有的注册会员的数据库

user domain 实体对象需要的package


User  是 所有用户最基础的 domain。

userInfoDto  是用户登录后，显示个人资料，所需要显示的信息，这个就是dto

//------------child package-----------

child-package  edit：   用户资料修改，需要的domain

feign  package  是其他的服务器组件，需要user数据库信息的时候，通过spring feign来沟通
     的domain


admin   是新地点管理人员或客服人员，需要的domain