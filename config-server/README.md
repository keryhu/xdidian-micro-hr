# spring-config
一 、 第二个启动，目前实现了spring security 单独的验证机制，用户名和密码硬编码。

二 、 spring.cloud.git.url 加密功能

其他service 获取config－service配置的属性变量是通过

	cloud:   
	  config:
		  uri: http://15900455491:skdjf7782c@${domain.name:localhost}:8888
	      

三 、 实现了单独的spring security保护。

