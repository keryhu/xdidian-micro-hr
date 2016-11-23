# company  人力资源公司的一些基础信息，并且是保存在mongo数据库的。
1 公司创建，由一位人员登记，创建，填写公司名字，公司地址，营业执照等信息
2 储存和记录，公司的基本信息，例如公司名字，组织架构，等

3  一个 用户，最多默认最多只能创建 5家公司，

4 ,当有新的公司创建的时候发送，message给websocket服务器，然后由他统一发送给前台。
5 ／service／** ，限定了权限必需是 新地点的客服人员或管理员。--用在审核公司的时候。

 当新公司注册后，新地点的工作人员，
 *    审核通过了公司的注册资料，由company，发出的，消息，
 * 1 给user——account，通知他更新 user 的权限为 ROLE_COMPANY_ADMIN，更新companyId为新的。（id或email或phone，和companyId）
 * 2 通知邮件服务器，发送审核成功的通知，(email-必需，companyId）。
 * 3 通知手机平台，发送审核成功的通知，（phone--必需，companyId）
 * 4 通知websocket，给对应的userId，发送通知（userId-必需，companyId）
 * 
 *    审核失败了：
 * 1 通知邮件服务器，发送审核失败的通知，(email-必需，companyId）。
 * 3 通知手机平台，发送审核失败的通知，（phone--必需，companyId）
 * 4 通知websocket，给对应的userId，发送失败通知（userId-必需，companyId）
 *

websocket stomp 支持，需要安排，rabbitmq-plugins enable rabbitmq_stomp 插件