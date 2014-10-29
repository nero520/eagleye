git pull

mvn package
#cp target/config.properties /opt/flume-1.4.0/lib
scp -P 60777 target/yougou-flume-sink.jar root@10.10.10.220:/usr/local/flume-1.4.0/lib
#cp -r target/lib /opt/flume-1.4.0/lib/lib