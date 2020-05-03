package org.example.InvoiceProcess.Serde

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.logging.log4j.{LogManager, Logger}
import org.example.InvoiceProcess.AppUtil.logPrefix

import scala.reflect.runtime.universe._
import reflect.ClassTag

object SerdeUtils {

  val logger: Logger = LogManager.getLogger(this.getClass)
  logger.info(s"${logPrefix(this.getClass.getName)} - Started" )

  val mapper = new ObjectMapper with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  object ByteArray {
    def encode(value: Any): Array[Byte] = mapper.writeValueAsBytes(value)

    /*def decode[T: TypeTag : ClassTag](value: Array[Byte]): T =
      mapper.readValue[T](value)*/

    def decode[T](value: Array[Byte])(implicit ttag:TypeTag[T]) : T = {
      val rTimeValueClsInfo: Class[_] = ttag.mirror.runtimeClass(ttag.tpe)
      implicit val ctag = ClassTag[T](rTimeValueClsInfo)
      mapper.readValue[T](value)
    }
  }

}
