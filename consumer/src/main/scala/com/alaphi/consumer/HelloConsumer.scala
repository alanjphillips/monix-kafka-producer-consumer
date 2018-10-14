package com.alaphi.consumer

import com.alaphi.common.HelloThere

import monix.execution.Scheduler
import monix.kafka.{KafkaConsumerConfig, KafkaConsumerObservable}

import scala.concurrent.Await
import scala.concurrent.duration._

import io.circe._
import io.circe.parser._
import io.circe.generic.semiauto._


object HelloConsumer extends App {
  implicit val scheduler: Scheduler = monix.execution.Scheduler.global
  implicit val helloDecoder: Decoder[HelloThere] = deriveDecoder[HelloThere]

  val consumerCfg = KafkaConsumerConfig.default.copy(
    bootstrapServers = List("kafka-1:9092"),
    groupId = "hello-group"
  )
  val observable =
    KafkaConsumerObservable[String,String](consumerCfg, List("hello-topic"))

  val running = observable.foreachL { record =>
    val hello = decode[HelloThere](record.value)
    println(s"Received: ${hello.getOrElse(Json.Null)}")
  }

  val result = Await.result(running.runAsync, Duration.Inf)
}
