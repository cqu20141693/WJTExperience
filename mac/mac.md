## mac

### 基础信息

1. apple ID
   `163/Wq`

### 使用技巧

桌面左上角苹果图标点击后可以:1.查看系统信息，2.配置系统信息3.进入App Store，4.查看最近打开的软件和文件，5.开关机，锁屏，登出用户

1. 中英文切换
   `使用中｜英键切换`
2. 搜索
   `使用command+空格 键搜索，可以选择搜索引擎(google),或者是Filder搜索`
3. 电源按钮

```

```

4. 剪切、拷贝、粘贴

```
Command-X：剪切所选项并拷贝到剪贴板。
Command-C：将所选项拷贝到剪贴板。 ...
Command-V：将剪贴板的内容粘贴到当前文稿或App 中。 ...
Command-Z：撤销上一个命令。 ...
Command-A：全选各项。
Command-F：查找文稿中的项目或打开“查找”窗口。
Command-Tab：在打开的 App 中切换到下一个最近使用的 App
Shift-Command+3/4: 截取屏幕全屏和选择截屏
Shift-Command+5: 录屏
Control-Command-F：全屏使用 App
Command-逗号(,):进入AppSetting
option-Command-c : 选中文件夹时可以复制路径
Command-空格 ： 搜索应用
```

### 常用软件

#### 开发软件

1. jetbrains
2. jdk,python,golang
3. Homebrew
4. vscode
5. vpn
6. google
7. 邮箱使用自带Mail
8. maven
9. git
10. brew
``` 
docker run -itd -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms128m -Xmx512m" \
-v ~/docker/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v ~/docker/elasticsearch/data:/usr/share/elasticsearch/data \
-v ~/docker/elasticsearch/plugins:/usr/share/elasticsearch/plugins \
--name es elasticsearch:7.17.6

安装redis
brew search redis
brew install redis@6.2
/opt/homebrew/opt/redis@6.2/bin/redis-server /opt/homebrew/etc/redis.conf

安装postgresql
This formula has created a default database cluster with:
  initdb --locale=C -E UTF-8 /opt/homebrew/var/postgresql@11
For more details, read:
  https://www.postgresql.org/docs/11/app-initdb.html

postgresql@11 is keg-only, which means it was not symlinked into /opt/homebrew,
because this is an alternate version of another formula.

If you need to have postgresql@11 first in your PATH, run:
  echo 'export PATH="/opt/homebrew/opt/postgresql@11/bin:$PATH"' >> ~/.zshrc

For compilers to find postgresql@11 you may need to set:
  export LDFLAGS="-L/opt/homebrew/opt/postgresql@11/lib"
  export CPPFLAGS="-I/opt/homebrew/opt/postgresql@11/include"


To restart postgresql@11 after an upgrade:
  brew services restart postgresql@11
Or, if you don't want/need a background service you can just run:
  /opt/homebrew/opt/postgresql@11/bin/postgres -D /opt/homebrew/var/postgresql@11
==> Summary
🍺  /opt/homebrew/Cellar/postgresql@11/11.17_3: 3,216 files, 40.5MB
==> Running `brew cleanup postgresql@11`...

安装cassandra
To restart cassandra after an upgrade:
  brew services restart cassandra
Or, if you don't want/need a background service you can just run:
  /opt/homebrew/opt/cassandra/bin/cassandra -f
==> Summary
🍺  /opt/homebrew/Cellar/cassandra/4.0.7: 1,132 files, 70MB
==> Running `brew cleanup cassandra`...
Disable this behaviour by setting HOMEBREW_NO_INSTALL_CLEANUP.
Hide these hints with HOMEBREW_NO_ENV_HINTS (see `man brew`).
==> Caveats
==> cassandra
To restart cassandra after an upgrade:
  brew services restart cassandra
Or, if you don't want/need a background service you can just run:
  /opt/homebrew/opt/cassandra/bin/cassandra -f

安装elasticsearch：
Data:    /opt/homebrew/var/lib/elasticsearch/
Logs:    /opt/homebrew/var/log/elasticsearch/elasticsearch_gymd.log
Plugins: /opt/homebrew/var/elasticsearch/plugins/
Config:  /opt/homebrew/etc/elasticsearch/

elasticsearch@6 is keg-only, which means it was not symlinked into /opt/homebrew,
because this is an alternate version of another formula.

If you need to have elasticsearch@6 first in your PATH, run:
  echo 'export PATH="/opt/homebrew/opt/elasticsearch@6/bin:$PATH"' >> ~/.zshrc


To start elasticsearch@6 now and restart at login:
  brew services start elasticsearch@6
Or, if you don't want/need a background service you can just run:
  elasticsearch
==> Summary
🍺  /opt/homebrew/Cellar/elasticsearch@6/6.8.23: 136 files, 103.3MB
==> Running `brew cleanup elasticsearch@6`...
Disable this behaviour by setting HOMEBREW_NO_INSTALL_CLEANUP.
Hide these hints with HOMEBREW_NO_ENV_HINTS (see `man brew`).
==> Caveats
==> elasticsearch@6
Data:    /opt/homebrew/var/lib/elasticsearch/
Logs:    /opt/homebrew/var/log/elasticsearch/elasticsearch_gymd.log
Plugins: /opt/homebrew/var/elasticsearch/plugins/
Config:  /opt/homebrew/etc/elasticsearch/

elasticsearch@6 is keg-only, which means it was not symlinked into /opt/homebrew,
because this is an alternate version of another formula.

If you need to have elasticsearch@6 first in your PATH, run:
  echo 'export PATH="/opt/homebrew/opt/elasticsearch@6/bin:$PATH"' >> ~/.zshrc


To start elasticsearch@6 now and restart at login:
  brew services start elasticsearch@6
Or, if you don't want/need a background service you can just run:
  elasticsearch


nginx :
/opt/homebrew/etc/nginx
nginx -t : 检查配置
nginx -s reload ： 重启
nginx -s reload ： 重启
```
``` 
配置ssh利用git协议管理远程仓库 
```

10. postman
11. 新建文件和命令窗口
```
touch file

```
12. 查看隐藏文件
```  
cmd+shift+. 
```
