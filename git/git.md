### git

Git 不仅仅是个版本控制系统，它也是个内容管理系统(CMS)，工作管理系统等

#### git 安装和配置

1. 下载安装
2. 配置git 环境配置
3. 配置IDE git 配置
4. 配置git远程仓库

```markdown
1. [github 仓库配置](https://www.runoob.com/w3cnote/git-guide.html)
2. 私有仓库配置
```

#### idea git 使用

1. 拉取远程仓库代码

``` 
1. idea 拉取： File->new project->from version control
2. git clone 仓库地址 
```

2. 拉取合并远程代码

``` 
1. idea: Git: 箭头拉取合并代码; ctrl+T
2. 终端输入： git pull /fetch
```

3. 提交代码到本地仓库

``` 
1. idea: Git: 提交箭头 ； ctrl + K
2. 终端输入： git add 文件； git commit -m 'message'
```

4. 推送本地仓库到远程仓库

``` 
1. idea: 推送箭头； ctrl + shift + K 
2. 终端输入： git push
```
5. [git 分支管理](https://blog.csdn.net/whereismatrix/article/details/46443471)
``` 
 
```
6. 其他功能

``` 
1. git 分支管理
2. git tag 管理
3. git remote  
4. 代码回滚管理
5. git 子模块管理

```

### 代码提交规范

``` 
feat - 新功能 feature
fix - 修复 bug
docs - 文档注释
style - 代码格式(不影响代码运行的变动)
refactor - 重构、优化(既不增加新功能，也不是修复bug)
perf - 性能优化
test - 增加测试
chore - 构建过程或辅助工具的变动
revert - 回退
build - 打包
```

### centos 安装git

``` 
yum install git
git --version

git config --global user.name "wcc"
git config --global user.email wcc@cc.com
git config --list
```
