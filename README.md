# POS_Kstream_Invoice_FetchSplit

this realtime application is dependant on upstream "POS_Kafka_ProducerAPI" application for data. it fetch invoice data from kafka topic and split invoice into Respective Dimentions like (customer details, billing details etc.) and send these splited data to respective downstream kafkatopics to be processed by downstream application.
