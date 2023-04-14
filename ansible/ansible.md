## ansible
### vars 环境变量配置
### templates 模版配置
### tasks
1. shell 命令执行
2. register 变量注入和判断
3. file 目录创建
4. block 任务状态变更
5. template 模版拷贝
6. service 启动并设置开机启动
7. service 启动并设置开机启动
8. yum 包安装
9. unarchive 解压缩包
10. when 条件判断
11. 判断文件是否存在
```  
- name: check init
  stat:
    path: "{{ home_path }}/superset.db"
  register: initMsg
- name: Task name
  debug:
    msg: "{{ home_path }}/superset.db exists"
  when: initMsg.stat.exists
```
12. ansible 启动携带环境变量
