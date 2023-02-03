### OpenSSL CA
```  
openssl genrsa -des3 -out rootCA.key 2048

openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 3650 -out rootCA.pem

```
### OpenSSL 生成自签证书
1. 创建密钥key
``` 
//生成rsa私钥，des3算法，1024位强度，ssl.key是秘钥文件名。
openssl genrsa -des3 -out ssl.key 1024
//后续输入密码,这里会输入两次. 填写一样即可. 随意填写一个. 下一步就会删除这个密码
```
2. 删除密码
``` 
//终端执行删除密码命令
//这里目录和生成私钥的目录一致
openssl rsa -in ssl.key -out ssl.key
```
3. 生成CSR（证书签名请求）
``` 
openssl req -new -key ssl.key -out ssl.csr 
执行以上命令后，需要依次输入国家、地区、城市、组织、组织单位、Common Name、Email和密码。其中Common Name应该与域名保持一致。密码我们已经删掉了,直接回车即可
温馨提示Common Name就是证书对应的域名地址. 我们开发微信小程序时必须要让我们的外链的https的域名和证书统一才行
```
4. 生成自签名证书
``` 
//这里3650是证书有效期(单位：天)。这个大家随意。最后使用到的文件是key和crt文件。
openssl x509 -req -days 3650 -in ssl.csr -signkey ssl.key -out ssl.crt

需要注意的是，在使用自签名的证书时，浏览器会提示证书的颁发机构是未知的
```
