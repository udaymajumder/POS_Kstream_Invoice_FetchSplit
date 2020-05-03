package org.example.InvoiceProcess


import java.util.Properties
import org.apache.kafka.streams.StreamsConfig

import org.apache.logging.log4j.{LogManager, Logger}
import org.example.InvoiceProcess.AppUtil._

import org.example.InvoiceProcess.Process.ProcessInvoiceStream


object InvoiceProcessing {

  val logger: Logger = LogManager.getLogger(this.getClass)
  logger.info(s"${logPrefix(this.getClass.getName)} - Started" )

  def main(args: Array[String]): Unit = {

        val prop = new Properties()
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG,AppConfig.applicationID)
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,AppConfig.bootstrapServers)

        ProcessInvoiceStream.fetchNSplitInvoice(prop)

  }

}
