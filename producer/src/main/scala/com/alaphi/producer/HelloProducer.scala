package com.alaphi.producer

import com.alaphi.common.HelloThere

import scala.concurrent.Await
import scala.concurrent.duration._
import monix.execution.Scheduler
import monix.kafka.{KafkaProducer, KafkaProducerConfig}

import io.circe.generic.auto._
import io.circe.syntax._


object HelloProducer extends App {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  val producerCfg = KafkaProducerConfig.default.copy(
    bootstrapServers = List("kafka-1:9092")
  )

  val producer = KafkaProducer[String,String](producerCfg, scheduler)

  val publish = for {
    _ <- producer.send("hello-topic", HelloThere("Hellooo", "there").asJson.spaces2).delayExecution(30 seconds)
    close <- producer.close()
  } yield close

  val result = Await.result(publish.runAsync, Duration.Inf)
}
