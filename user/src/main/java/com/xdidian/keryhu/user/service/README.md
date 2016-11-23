# user用户账户系统  包含了 新地点的管理员，客服人员，所有的注册会员的数据库

新地点的的所有数据库对象，需要的service操作。

  getIsInCompanyAndName：
  
      通过参数，userId，获取他，是否有公司，和姓名， 用在spring feign，menu服务器组件
      
  validateNameEdit 
  
      通过参数 userId，提交的姓名，验证当前的姓名是否可以修改，如果可以修改则保存
      
  validateBirthdayAndSave
  
      通过参数userId，提交的生日，验证是否可以修改，如果可以修改则保存
      
  validateChangePassword
  
      通过参数userid，提交新密码，原密码，验证输入的是否正确，是否可以修改，如果可以修改则保存
      
   