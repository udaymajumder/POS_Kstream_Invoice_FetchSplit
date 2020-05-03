package org.example.InvoiceProcess.Serde

import java.util

import org.apache.kafka.common.serialization.Deserializer
import org.apache.logging.log4j.{LogManager, Logger}
import org.example.InvoiceProcess.AppUtil.logPrefix

import scala.reflect.runtime.universe._


class JSONDeserializer[T : TypeTag] extends Deserializer[T]
{

  val logger: Logger = LogManager.getLogger(this.getClass)
  logger.info(s"${logPrefix(this.getClass.getName)} - Started" )

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
  }

  override def deserialize(topic: String, data: Array[Byte]):T = {

    SerdeUtils.ByteArray.decode[T](data)
  }

  override def close(): Unit = super.close()
}


