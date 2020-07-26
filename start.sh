#!/bin/bash
TZ=Asia/Shanghai
ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
java -jar -Xms1024m -Xmx1024m app.jar > /dev/null 2>&1
