## 边缘网关

### 硬件网关

对于不存在采集和控制能力的场景时，需要具有选择自建硬件设备和网关 比如农业场景，一般设备都不具备主控制器，需要自建硬件网关

### 软网关

对于工业工厂设备，设备都具有一个主控制器，可以采集和控制设备 利用软网关对接主控制器设备(PLC)协议将数据采集到网关

#### 采集各种PLC设备

西门子S7协议转MQTT、OPC UA、Modbus协议网关 欧姆龙Omron Host Link协议转MQTT、OPC UA、Modbus协议网关 欧姆龙Omron FINS协议转MQTT、OPC UA、Modbus协议网关
基恩士Omron Host Link协议转MQTT、OPC UA、Modbus协议网关 基恩士Omron FINS协议转MQTT、OPC UA、Modbus协议网关 三菱Mitsubishi MELSEC协议转MQTT、OPC
UA、Modbus协议网关 施耐德PLC转MQTT、OPC UA、Modbus TCP协议网关 ABB PLC转MQTT、OPC UA、Modbus TCP协议网关

#### 提供对外数据expose

1. OPC UA 能力
2. MQTT Server 订阅发布能力
3. Modbus RTU/TCP协议

#### 平台对接能力

1. 自身IOT 平台对接（MQTT） 对接平台物模型和topic能力
2. 三方平台能力对接 适配三方物模型和topic

### 参考

1. [Modbus 协议详解](https://zhuanlan.zhihu.com/p/270306935)
