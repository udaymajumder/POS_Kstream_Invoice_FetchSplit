kafka-topics.bat --create --zookeeper localhost:2181 --topic merchant --partitions 5 --replication-factor 3 --config segment.bytes=100000