## readme

1. 下载minio 执行文件

``` 
mc ： minio client : 用于上传本地默认images

minio ： minio 可执行文件，部署minio

// 列出桶
mc ls minio/cc/guc
```
2. 添加默认上传文件到images
``` 
play 执行过程中会拷贝images 中的文件到部署机，并将文件上传到对应的配置桶中（minio_create_buckets）
```
