### lerna
#### 初始化lerna环境
````  
npm i -g lerna
npm i -g yarn // 需要借助yarn的workspaces特性
cd <workplace>
lerna init
        // ^ >lerna init
        // lerna notice cli v4.0.0
        // lerna info Updating package.json
        // lerna info Creating lerna.json
        // lerna info Creating packages directory
        // lerna success Initialized Lerna files
````
#### 指定工作区域
1. 修改package.json文件
```  
  "private": true,  // private需要为true
  "workspaces": [
    "projects/*",
    "components/*"
  ]
```
2. 修改lerna.json文件
```   
{
  "packages": [
    "packages/*"
  ],
  "npmClient": "yarn",
  "useWorkspaces": true,  // 共用package.json文件的workspaces配置
  "version": "independent"  // 每个项目独立管理版本号
}
```
#### 创建新项目
lerna create cli
// ^ lerna success create New package @demo/cli created at .projects/cli

#### packages.json script
yarn 运行脚本启动，编译项目
