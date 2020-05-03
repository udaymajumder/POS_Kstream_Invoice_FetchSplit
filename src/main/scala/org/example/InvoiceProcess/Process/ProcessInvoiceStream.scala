package org.example.InvoiceProcess.Process

import java.util.Properties

import org.example.InvoiceProcess.AppConfig
import org.example.InvoiceProcess.Serde.JSONSerde
import org.example.InvoiceProcess.POJO.EntityMapper._

import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

import org.apache.logging.log4j.{LogManager, Logger}
import org.example.InvoiceProcess.AppUtil._

/**
 * scala.Serdes._ -> imp to include base serdes like Int,String or Custom
 * scala.ImplicitConversions._ -> imp to do implicit conversion from scala type  [Int,String] to Serde
 * below 2 import imp to include base scala serdes*/
import org.apache.kafka.streams.scala.Serdes._
import org.apache.kafka.streams.scala.ImplicitConversions._

object ProcessInvoiceStream {

  val logger: Logger = LogManager.getLogger(this.getClass)
  logger.info(s"${logPrefix(this.getClass.getName)} - Started" )

def fetchNSplitInvoice(prop:Properties)={

  implicit val invoiceSerde: JSONSerde[Invoice] = new JSONSerde[Invoice]
  implicit val consumerSerde = new JSONSerde[SinkConsumerDetail]
  implicit val merchantSerde = new JSONSerde[SinkMerchantDetail]
  implicit val billingSerde = new JSONSerde[SinkBillingDetail]
  implicit val productPurchasedSerde = new JSONSerde[SinkPurchasedProduct]

  val streambuilder: StreamsBuilder = new StreamsBuilder
  val incomingInvoiceStream: KStream[Int, Invoice] = streambuilder.stream[Int,Invoice](AppConfig.sourceTopicName)

  /**
   * Process Consumer Details
   */
  val KSSconsumerDetails: KStream[Int, SinkConsumerDetail] = incomingInvoiceStream.mapValues((value:Invoice) =>
                                  value match { case Invoice(invoiceNum,consumerDetails,merchant,location,productPurchased) => SinkConsumerDetail(invoiceNum,consumerDetails)})
  val KSSprimeConsumerDetails: KStream[Int, SinkConsumerDetail] = KSSconsumerDetails.filter((key:Int, value:SinkConsumerDetail) => value.consumerDetails.PRM_IND)
  val KSSnonPrimeConsumerDetails: KStream[Int, SinkConsumerDetail] = KSSconsumerDetails.filter((key:Int, value:SinkConsumerDetail) => !(value.consumerDetails.PRM_IND))

  KSSprimeConsumerDetails.to(AppConfig.sinkPriorityConsumerTopic)
  KSSnonPrimeConsumerDetails.to(AppConfig.sinkNonPriorityConsumerTopic)

  /**
   * Process Merchant Details
   */

  val KSSmerchantDetails: KStream[Int, SinkMerchantDetail] = incomingInvoiceStream.mapValues((value:Invoice) =>
    value match { case Invoice(invoiceNum,consumerDetails,merchant,location,productPurchased) => SinkMerchantDetail(invoiceNum,merchant,location)})

  KSSmerchantDetails.to(AppConfig.sinkMerchantTopic)

  /**
   * Process Billing Details
   */

  val KSSbillingDetails: KStream[Int, SinkBillingDetail] = incomingInvoiceStream.mapValues((value:Invoice) =>
    value match { case Invoice(invoiceNum,consumerDetails,merchant,location,productPurchased) => SinkBillingDetail(invoiceNum,merchant.MRCH_CD,merchant.MRCH_CAT_CD,merchant.MRCH_NM,productPurchased.BILL_AMT)})

  KSSbillingDetails.to(AppConfig.sinkBillingTopic)

  /**
   * Process Product Details
   */

  val KSSprdPurchasedDetails: KStream[Int, SinkPurchasedProduct] = incomingInvoiceStream.mapValues((value:Invoice) =>
    value match { case Invoice(invoiceNum,consumerDetails,merchant,location,productPurchased) => SinkPurchasedProduct(invoiceNum,productPurchased.PRD_LIST)})

  KSSprdPurchasedDetails.to(AppConfig.sinkProductTopic)

  val topology = streambuilder.build()
  val stream = new KafkaStreams(topology,prop)

  stream.start()

}

}
