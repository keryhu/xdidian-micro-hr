 #!/bin/sh
 #停止所有的在运行的container 进程
 docker stop $(docker ps -a -q )
 # 休息5s
 sleep 5
 #删除所有的在运行的container 进程
 docker rm $(docker ps -a -q -f status=exited)

#删除所有带can-autoremove label的docker volume
sleep 5
docker volume rm $(docker volume ls -qf dangling=true)

#删除所有带有 can-autoremove 标签的images
sleep 5
docker rmi $(docker images -q -f "label=can-autoremove=true")