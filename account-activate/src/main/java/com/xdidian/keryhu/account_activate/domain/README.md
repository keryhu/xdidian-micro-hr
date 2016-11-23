所有的实体类：

AccountMethodDto ： 前台提交参数，决定由哪种方法，取回密码。（参数是 account，method）
      account 可能是email或phone的具体值， method，是email或phone
      
ActivatedProperties   设置激活token的属性，过期时间，允许重复发送多少此，多久自动清零。

CommonActivatedLocalToken  保存在本地的激活token的数据，包含，token，email激活时间，过期时间

CommonConfirmTokenDto   通用的，验证前台 输入的 account 和dto，是否符合要求
                        * token和account是否匹配，token是否匹配。
                        
FormAccountEditDto      在前台修改 个人资料，email或者phone的时候，需要前台提交  userId，账号，和可选的密码

NewPasswordFormDto     当前台  提交新密码的时候，提交的resonponseBody ，这个就是主class

TokenType         激活的具体类型，由前台传递。