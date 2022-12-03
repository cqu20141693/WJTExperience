## 充电桩协议对接
充电桩协议是一种基于TCP且一请求一应答型协议
### 家桩
1. 报文
``` 
Device -> IOT
Login ： 登录报文 ，IOT 会返回签名Token,心跳和上报数据频率
PING：空闲状态下的心跳
ChargingStart : 设备侧充电开始
ChargingData ： 充电实时数据
ChargingBill: 充电完成时生成的账单

IOT -> Deice
StartCharging ： 开启充电
StopCharging ： 停止充电
BookCharging ： 预约充电
CancelBookCharging ： 取消预约充电
OTAUpgrade ： ota升级
```

### 商用桩
