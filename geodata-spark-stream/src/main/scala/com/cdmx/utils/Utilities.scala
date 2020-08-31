package com.cdmx.utils

import com.cdmx.utils.Constants._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Utilities {
  def createSparkConf: SparkConf = {

    val sparkConfig =  new SparkConf()
      .setMaster("local[*]")
      .setAppName("SparkKafkaStreamTest")
      .set("spark.executor.memory","6g")
      .set("spark.driver.memoryOverhead","8g")
      .set("spark.sql.autoBroadcastJoinThreshold", "20971520")
      .set("spark.driver.cores","4")
      .set("spark.sql.inMemoryColumnarStorage.compressed","true")
      .set("spark.shuffle.spill","true")
      .set("spark.sql.join.preferSortMergeJoin", "true")
      .set("spark.sql.defaultSizeInBytes", "100000")
    sparkConfig
  }

  def getKafkaParams(kafkaServer:String): Map[String, Object] ={

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> kafkaServer,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean),
      "group.id" -> "cdmx_data",
      "security.protocol" -> "SASL_SSL",
      "sasl.mechanism" -> "PLAIN"
    )
    kafkaParams
  }

  def getProps:java.util.Properties={

    val prop = new java.util.Properties
    prop.setProperty("driver", PROP_DRIVER_VAL)
    prop.setProperty("user", PROP_USER_VAL)
    prop.setProperty("password", PROP_PASSWORD_VAL)
    prop
  }

  def getMaxDateCDMX(spark:SparkSession): Long = {

    var maxDate:Long = 0
    try {
      val dateDF = spark.read.format("jdbc")
        .option("url", POSTGRES_JDBC_URI)
        .option("user", PROP_USER_VAL)
        .option("password", PROP_PASSWORD_VAL)
        .option("query", QUERY_MAX_PG_DATE)
        .load()

      maxDate =dateDF.collect.map(x => x(0)).toList.head.toString.toLong

    } catch {
      case ex: Exception  => print(ex)
    }
    maxDate
  }

}
