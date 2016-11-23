# company  人力资源公司的一些基础信息，并且是保存在mongo数据库的。


service package  表示只有新地点的客服或管理员才可以操作的rest

      AdminRest
      
           新地点的管理人才可以操作的 rest
           
           "/admin/queryCompanyWithPage"   根据条件，查询公司的信息，返回page,可查询公司名字关键字
           
           "/service/queryUncheckedCompanyWithPage"
           
           新地点的客服人员，查询未审核的公司，返回page对象，也可以根据公司名字关键字搜索

      CheckCompanyRest   需要权限，新地点的客服或管理人
         
           
           新地点的工作人员，审核公司注册资料的 rest
           
      serviceRest   新地点的管理人或客服都可以操作
      
           "/service/queryCompanyWithPage"
           根据公司的名字 搜索公司信息 返回page    可查询公司名字关键字
           
           "/service/queryUncheckedCompanyWithPage"
           搜索所有未审核的公司   新地点的客服人员和工作人员，都使用这个url和方法。
           可查询公司名字关键字
           
           
           "/service/check-company"
           新地点的工作人员，审核新注册公司材料的post 的rest
           
           "/service/queryNewCompanyInfoByCompanyId"
           新地点的工作人员， 审核新注册公司，点击某一行，查看详情的时候，需要获取
           具体的注册信息，就是此rest

     
ApplyCompanyAgainRest

    "/company/getRejectCompanyInfo"  
    
     当用户提交的公司注册资料，被拒绝后，再次编辑，提交申请的rest
     

     
CompanyRest

    
    "/company/isCompanyExist"
    
    查看公司是否存在（参数公司名字）
    
    "/company/createCompanyResolveInfo"
   当打开  新建 公司帐户 页面的时候，，首先需要 加载 3个信息数据
        * 1  所有的 省市，直辖市的 数据
        * 2  所有的公司行业数据
        * 3  所有的公司性质数据
        * <p>
        * 或者新地点的工作人员 审核公司的时候，需要首先 获取该companyId，之前有没有注册过，又没被拒绝过。
        * <p>
        * 如果 参数中，传递了companyId，那说明是新地点的工作人员来之前的。，需要获取该公司的基础信息，
        * 有没有被注册过。
         
 
         
    "/company/findUncheckedCompanyBySelf"
    
    用在 用户刚刚登录  后，点击"新建公司"，查看 刚用户是否注册过  公司帐户，
    是否存在需要 审核的 公司帐户。如果存在未审核
        // 的公司帐户，取出companyId，然后前台再通过，companyId，
        查看已经提交的公司信息，查看新地点工作人员有没有
        // 审核该公司，有没有拒绝的理由。  返回companyId 和 checked，
        如果companyId存在，且checked为false，那么就进行上面的判断
        
        
    
     
    /company/findUncheckedCompanyAfterReject
     
      // 和上面那个类似，但是这个是申请人在材料被拒绝后，的rest
         // 而且这里还实现了，，申请人 注册后，资料被驳回，再次查看申请材料的rest，这个可以查看reject
        
    
CreateOrRecreateCompanyRest

    创建公司和被拒绝后，再次编辑材料，申请注册公司。
    
     "/company/createCompany"
        公司创建的rest
        
      "/company/createCompanyAfterReject"
      
      /**
           * 当新公司的申请人，申请公司后，被拒绝，申请人打开提交材料，准备修改后，再次提交的rest
           * <p>
           * 验证提交参数的 具体方法：
           * 检查reject，是否有该参数，如果有，那么检查提交的参数是否有此参数，且验证是否符合要求。
           * 新地点对于 注册公司已经过几次申请，已经之前被拒绝的理由不做保存数据库，
           * 当用户编辑修改后，再次提交，则覆盖原来的申请信息，原来的被拒绝的信息也将被清除