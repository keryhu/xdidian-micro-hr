#message
全系统的消息提示，组件

功能： 建立：一套消息提醒系统，在用户登录后，在导航栏显示目前他关注的消息，有多少未读，
      一个菜单栏，显示这些未读信息具体的主题例如（未审核公司，公司注册通知，充值通知等）
      当用户点击，某一个具体的主题的时候，跳转到指定的页面，显示详细信息。
      当用户再次点击某一个具体的url的时候，状态从未读 变为已读。总的未读数目减去1，直到0，消失。
      
      subjectMsg, 包含有限主题和此主题数目的 class，这个就是显示在前台的的对象
     
     建立一个数据message保存对象，包含下面的属性：
     message（id,userId(如果此message是针对新地点的，那么userId可以为null，否则必填）
         ReadGroup,，Set<subjectMsg>
             
是否需要长久保存数据库？为什么？
 
     不需要，一旦消息从未读转为已读取，就删除该消息，因为他的建立的初衷就是为了，
     让客户了解目前有哪些代办事项，如果客户想要了解具体的记录，应该到具体栏目里面查询，例如：
     充值记录，而像未审核的公司消息提醒或者新公司注册成功或者失败的消息提醒，
     一旦公司审核完成了，或公司注册成功了，那么这条消息提醒也失去了意义，所以从数据库删
     
其他组件，如何通知message创建一条新的消息，如果通知他一条消息已经被读取。

   spring stream  通过他与各个组件沟通。新建，删除。通过此dto MessageCommunicateDto
   
   具体实现方法：
   
   针对message的操作，查询message数据库，首先根据messageCommunicateDto 的ReadGroup来判断，
       查询新地点，还是查询个人，
       如果查询新地点，那么从数据库ReadGroup==新地点的找到一条记录，然后找到包含，subject和count 的list，
       将具体的增减操作做进去。
   
   
例子：

    当一个新公司注册成功，company，发送一条message出去，接收方有：websocket，message，等
    websocket，通知前台，更新主题为"未审核公司"的 数字+1，
    message，接受到消息，首先寻找数据库中ReadGroup==XDIDIAN的记录，
    然后在Set<subjectMsg>，寻找到subject 为-"未审核的公司"，
    再将具体的增减，运用到count里。
    
    
    再一个例子
    
    会员申请加入公司
    当加入成功后，company，发送一条message，接收方有：websocket，message，等
    websocket，通知前台，更新主题为"新同事加入"的 数字+1，--当点击，就会导航到
           新的 页面（查看目前已申请的同事-页面）
    message，接受到消息，首先寻找数据库中ReadGroup==INDIVIDUAL和userID==
         MessageCommunicateDto.userID的记录，
       然后在Set<subjectMsg>，寻找到subject 为  新同事加入，
        再将具体的增减，运用到count里。
        
        
    上面两个例子，如果message被读取，就是前台点击了 导航到新的查询页面的时候，
    通知message，删除此id的数据
    
    显示前台需要的属性（id，Set<subjectMsg>，
      
        前台需不需要只读，这个消息是属于readGroup哪个值？
        
          不需要，因为也只有新地点的客服人员才能感受到，作用不大

    