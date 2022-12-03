## 监控

### prometheus

```shell
# localhost:9090
# --no-create-home 参数避免创建用户根目录，使用 --shell /usr/sbin/nologin 来禁止用户打开 Shell
mkdir -p /work/monitor/prometheus/config
mkdir -p /work/monitor/prometheus/data
cd /work/monitor/prometheus
wget https://github.com/prometheus/prometheus/releases/download/v2.36.2/prometheus-2.36.2.linux-amd64.tar.gz
# 离线模式，下载好手动上传
tar xfz prometheus-*.tar.gz
cd prometheus-*
cp -r ./consoles /work/monitor/prometheus
cp -r ./console_libraries /work/monitor/prometheus
cp ./prometheus /usr/local/bin/
cp ./promtool /usr/local/bin/
useradd --no-create-home --shell /usr/sbin/nologin prometheus
chown -R prometheus:prometheus /work/monitor/prometheus/
chown prometheus:prometheus /usr/local/bin/prometheus
chown prometheus:prometheus /usr/local/bin/promtool
# 使用prometheus用户登录
sudo -u prometheus /usr/local/bin/prometheus --config.file /work/monitor/prometheus/config/prometheus.yml \
--storage.tsdb.path /work/monitor/prometheus/data --web.console.templates=/work/monitor/prometheus/consoles \
--web.console.libraries=/work/monitor/prometheus/console_libraries
# 创建Systemd服务文件，设置开机启动,参考yaml
# vim /etc/systemd/system/prometheus.service
# systemctl daemon-reload
# systemctl enable prometheus
# systemctl start prometheus
```
```yaml
# my global config
＃全局配置 （如果有内部单独设定，会覆盖这个参数）
global:
  scrape_interval: 15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Alertmanager configuration
alerting:
  alertmanagers:
    - static_configs:
        - targets:
           - alertmanager:9093
  ＃告警规则。 按照设定参数进行扫描加载，用于自定义报警规则，其报警媒介和route路由由alertmanager插件实现。
  # Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
  # rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
＃采集配置。配置数据源，包含分组job_name以及具体target。又分为静态配置和服务发现
scrape_configs:
  - job_name: 'prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['localhost:9090']
  - job_name: 'nodeExporter'
    scrape_interval: 5s
    static_configs:
      - targets: ['192.168.96.163:9100','192.168.96.164:9100']
  - job_name: cadvisor1
    static_configs:
      - targets: ['192.168.96.163:8080']

＃用于远程存储写配置
# remote_write:
＃用于远程读配置
# remote_read:

```
```yaml
[Unit]
  Description=Prometheus Monitoring
  Wants=network-online.target
  After=network-online.target

[Service]
  User=prometheus
  Group=prometheus
  Type=simple
  ExecStart=/usr/local/bin/prometheus \
  --config.file /work/monitor/prometheus/config/prometheus.yml \
  --storage.tsdb.path /work/monitor/prometheus/data \
  --web.console.templates=/work/monitor/prometheus/consoles \
  --web.console.libraries=/work/monitor/prometheus/console_libraries
  ExecReload=/bin/kill -HUP $MAINPID

[Install]
  WantedBy=multi-user.target
```
### 
### node exporter

```shell
# localhost:9100
mkdir -p /work/monitor/node
cd /work/monitor/node

useradd --no-create-home --shell /bin/false node_exporter
# wget https://github.com/prometheus/node_exporter/releases/download/v1.3.1/node_exporter-1.3.1.linux-amd64.tar.gz
# 离线模式，下载好手动上传
tar -xvf node_exporter-1.3.1.linux-amd64.tar.gz
cp node_exporter-1.3.1.linux-amd64/node_exporter /usr/local/bin/
chown node_exporter:node_exporter /usr/local/bin/node_exporter

# 删除下载的文件
rm -rf node_exporter-1.3.1.linux-amd64.tar.gz  node_exporter-1.3.1.linux-amd64

# 创建跟随系统启动的systemd服务, 内容为下面的yaml
vim /etc/systemd/system/node_exporter.service
systemctl daemon-reload
systemctl start node_exporter
systemctl status node_exporter
# 设置开机启动
systemctl enable node_exporter


```
```yaml
[Unit]
Description=Node Exporter
Wants=network-online.target
After=network-online.target

[Service]
User=node_exporter
Group=node_exporter
Type=simple
ExecStart=/usr/local/bin/node_exporter

[Install]
WantedBy=multi-user.target
```

### cAdvisor
``` 
docker run -v /:/rootfs:ro \
-v  /var/run:/var/run:rw   \
-v /sys:/sys:ro            \
-v /var/lib/docker/:/var/lib/docker:ro  \
-p 8080:8080               \
--detach=true              \
--name=cadvisor            \
google/cadvisor:latest
```
### grafana
```shell
mkdir -p /work/monitor/grafana
cd /work/monitor/grafana
wget https://dl.grafana.com/enterprise/release/grafana-enterprise-9.0.2-1.x86_64.rpm
yum install grafana-enterprise-9.0.2-1.x86_64.rpm
systemctl daemon-reload
systemctl start grafana-server
systemctl status grafana-server
# systemctl enable grafana-server # 开机启动
```
