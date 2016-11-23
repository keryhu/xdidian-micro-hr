所有的实体类：


AccountEditService         个人资料修改的时候，需要验证提交的 账号信息的service
    validateAccountEdit
    
    
    
CommonTokenService     

    validateCodeAndAccount     前台输入token和account，来验证是否和数据库的匹配。
    
    isCodeExired              查看token是否过期。
    
RecoverService

    validateNewPassword      密码找回的时候，验证新的密码
    
    validatAccountMethod    验证accountMethod是否匹配
    
TokenConfirmSuccessService

    当前台输入token验证成功后，需要做的事情，，发送message，通知其他的组件更新信息。
    
TokenExpiredService

    当token过期，需要执行的服务，删除本地数据，如果是注册，还需要发送message出去。
    
VerifyTokenAccountService

    验证token是否存在于数据库 有3中情况的token需要被验证 
     * 分别为 token，resendToken resignupToken（其中resignupToken为可选）