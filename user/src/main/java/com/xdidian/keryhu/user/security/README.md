# user用户账户系统  包含了 新地点的管理员，客服人员，所有的注册会员的数据库

spring security 路由配置



"/users/query/**" Get   permitAll

"/admin/**"         need  XDIDIAN_ADMIN
"/service/**"       need  XDIDIAN_ADMIN OR  XDIDIAN_ADMIN



