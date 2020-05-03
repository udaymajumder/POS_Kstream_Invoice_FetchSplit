package org.example.InvoiceProcess

object AppConfig {

  val applicationID = "InvoiceFetchAndSplit"
  val bootstrapServers = "localhost:9092"

  val sourceTopicName = "invoice"
  val sinkPriorityConsumerTopic = "primeConsumer"
  val sinkNonPriorityConsumerTopic = "nonPrimeConsumer"
  val sinkMerchantTopic = "merchant"
  val sinkBillingTopic = "billing"
  val sinkProductTopic = "product"

}
