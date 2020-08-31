package com.cdmx.utils

object Constants {

  final val KAFKA_HOST = "pkc-4yyd6.us-east1.gcp.confluent.cloud:9092"
  final val KAFKA_TOPIC = "metrobus_data_stream"
  final val KAFKA_BOOTSTRAP = "bootstrap.servers"
  final val KAFKA_KEY_DESERIALIZER = "key.deserializer"
  final val KAFKA_VALUE_DESERIALIZER = "value.deserializer"
  final val KAFKA_OFFSET = "auto.offset.reset"
  final val KAFKA_OFFSET_VAL = "auto.offset.latest"
  final val KAFKA_AUTO_COMMIT = "enable.auto.commit"
  final val KAFKA_GROUP_ID = "group.id"
  final val KAFKA_GROUP_ID_VAL = "banco_base"
  final val JSON_TEMPVIEW_NAME = "json_values"
  final val QUERY_JSON_TEMPVIEW = "select * from json_values"
  final val REGEX_PATTERN = "[\\s:-]*"
  final val DATE_COLUMN = "date_updated"
  final val NUMERIC_DATE_COLUMN = "numeric_date"
  final val PROP_DRIVER = "driver"
  final val PROP_DRIVER_VAL = "org.postgresql.Driver"
  final val PROP_USER = "user"
  final val PROP_USER_VAL = "nufkisct"
  final val PROP_PASSWORD = "password"
  final val PROP_PASSWORD_VAL = "e89whMPKLMkd4HddsWpGDPRBi8uy-M3A"
  final val POSTGRES_URI = "lallah.db.elephantsql.com:5432/nufkisct"
  final val POSTGRES_TABLE = "metrobus_cdmx"
  final val POSTGRES_JDBC_URI = s"jdbc:postgresql://${POSTGRES_URI}"
  final val QUERY_NAME = "query"
  final val QUERY_MAX_PG_DATE = "select max(numeric_date) from metrobus_cdmx"

}
