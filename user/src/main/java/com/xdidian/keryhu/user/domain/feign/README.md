# user用户账户系统  包含了 新地点的管理员，客服人员，所有的注册会员的数据库


其他service 组件，需要用户user 数据库信息时，所需要的  dto


AuthUserDto，是 auth-server 需要的，在用户登录时，需要验证，用户的登录号和密码是否匹配。


EmailAndPhoneDto  是company  服务器，需要的feign，为什么使用？

        用在company 服务 组件，因为在公司注册完后，新地点的人员审核注册的资料时候，当审核通过或者不通过的时候，
        都需要发送email通知和手机消息通知给  注册人，
        所以需要根据用户的id，知道他的email和手机号
        
        

