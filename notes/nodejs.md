## nodejs
### 安装
#### 安装nvm
[nvm install](https://www.jianshu.com/p/130465398038)
1. nvm 命令
```
设置国内下载源

nvm node_mirror https://npm.taobao.org/mirrors/node/
nvm npm_mirror https://npm.taobao.org/mirrors/npm/

nvm -v                 // 查看nvm版本，判断是否安装成功
nvm ls available       // 获取可获取的Node版本
nvm install 10.14.2    //安装指定版本的Node
nvm use 10.14.2        //使用指定版本Node
nvm uninstall 10.14.2  //卸载指定版本Node

安装好nodejs时，npm也同时装好了
node -v
npm -v
npm install 安装包
使用yarn 安装
npm i -g yarn
yarn 
```