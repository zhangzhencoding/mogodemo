#!/bin/bash
TZ=Asia/Shanghai
ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
java -jar -Xms1024m -Xmx1024m app.jar --spring.profiles.active=dev > /dev/null 2>&1
