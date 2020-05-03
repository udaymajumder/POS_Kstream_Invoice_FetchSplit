package org.example.InvoiceProcess.Serde

import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}
import org.apache.logging.log4j.{LogManager, Logger}
import org.example.InvoiceProcess.AppUtil.logPrefix

import scala.reflect.runtime.universe._


class JSONSerde[T : TypeTag] extends Serde[T]
{

  val logger: Logger = LogManager.getLogger(this.getClass)
  logger.info(s"${logPrefix(this.getClass.getName)} - Started" )

  override def serializer(): Serializer[T] = new JSONSerializer[T]

  override def deserializer(): Deserializer[T] = new JSONDeserializer[T]

}
