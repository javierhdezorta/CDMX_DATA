package com.cdmx

import com.cdmx.utils.Constants._
import com.cdmx.utils.Utilities._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


object MetrobusCMDXData{

  def main(args: Array[String]): Unit = {

    // crear spark streaming context
    val sparkConfig =  createSparkConf
    val sc = new SparkContext(sparkConfig)
    val ssc = new StreamingContext(sc,Seconds(1))
    // configuracion de kafka
    val kafkaParams = getKafkaParams(KAFKA_HOST)
    val kafkaTopics = Array(KAFKA_TOPIC).toSet

    // creaf streaming con la configuracion de kafka, esto genera dstream
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](kafkaTopics,kafkaParams)
    )

    // obtener el valos del dstream, aqui se obtienen el json en string del topic de kafka
    val res_stream: DStream[String] = stream.map(record => record.value()).repartition(4)

    // le apicafor un foreach al dstream, aqui se abre la conenxion al streaming del topic, bre el pipeline de conexion
    res_stream.foreachRDD( rdd =>{

      // evaluamos si el rdd esta vacio, esto ya que el stream es continuio, y el kafka se llena cada minuto, se tiene que validar que no sea vacio
      if(!rdd.isEmpty()){

        // abrimos la configuracion para utilizar spark sql sobre el rdd
        val sparkSession = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
        import sparkSession.implicits._

        // creamos una tabla temporal del rdd, s utiliza el json ya que somo el stream de kafka es un array de json, el read.jsonfunciona para esos casos sin la necesidad de un esquema
        sparkSession.read.json(rdd).createOrReplaceTempView(JSON_TEMPVIEW_NAME)

        // pasamos el tempview a df y le agregaos unas tablas, la tabla de numeric_data, nos sirve para tener una referencia de la fecha mas reciente en formato int
        // se utiliza para hacer las coparaciones con la fuente de postgres
        val cdmxDataDF = sparkSession.sql(QUERY_JSON_TEMPVIEW)
          .withColumn(NUMERIC_DATE_COLUMN,regexp_replace(col(DATE_COLUMN),REGEX_PATTERN,"").cast(LongType)).cache()
        cdmxDataDF.show()

        // obtenemos la fecha mas eciente de postgres sink, es la fuenta en donde se almacena el df, se necesita de la fecha mas reciente para
        // cachar los cambios que se generen del stream, es para validar si los datos del stream ya cambiaron osea tienen una nueva fecha
        // o son datos con una fecha ya registrada
        val dateStream = cdmxDataDF.select(NUMERIC_DATE_COLUMN).limit(1).collect.map(x => x(0)).toList.head.toString.toLong
        val maxDateSource =getMaxDateCDMX(sparkSession)
        println(maxDateSource+"--------------------------------------------------------------------------------------------------------------------------------------------------------->")

        // se valida que la fecha del df sea mayor que la fecha que se encuentra almacenada en la fuente
        if (dateStream > maxDateSource){
          println("entro ------------------------------------------------xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
          // se ecribe la nueva info en postgres sink
          val prop = getProps
          //cdmxDataDF.write.mode(SaveMode.Append).jdbc(POSTGRES_JDBC_URI,POSTGRES_TABLE,prop)
        }
      }
    })

    ssc.start()
    ssc.awaitTermination()

    }
}
