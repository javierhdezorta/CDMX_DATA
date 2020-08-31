import pandas as pd
import geopandas as gpd
import requests
from kafka import KafkaProducer
import json
from pandas.io.json import json_normalize
import logging
from apscheduler.schedulers import background
from confluent_kafka import Producer, KafkaError

uri_data = "https://data.opendatasoft.com/api/records/1.0/search/?dataset=prueba_fetchdata_metrobus%40lab-cdmx&rows=-1"
file_geo = "alcaldias.geojson"

logging.basicConfig()
scheduler = background.BlockingScheduler()


def get_data(uri):
    r = requests.get(uri)
    data = r.json()
    json_items = data["records"]
    return json_items


def publish_message(producer_instance, topic_name, key, value):
    try:
        key_bytes = bytes(key, encoding='utf-8')
        value_bytes = bytes(value, encoding='utf-8')
        producer_instance.produce(topic_name, key=key_bytes, value=value_bytes)
        producer_instance.poll(0)
        producer_instance.flush()
    except Exception as ex:
        logging.error(str(ex))


def connect_kafka_producer():
    _producer = None
    try:
        _producer = Producer({
            'bootstrap.servers': "pkc-4yyd6.us-east1.gcp.confluent.cloud:9092",
            'sasl.mechanisms': "PLAIN",
            'security.protocol': "SASL_SSL",
            'sasl.username': "35TUSXTAVJUUUGB3",
            'sasl.password': "jhH+OF40RAX/5I60Pk3AC3/SnSebdkpqtDDfre0kQPbSchKI/KMVoioiWWQl9ATa",
        })

    except Exception as ex:
        logging.error(str(ex))
    finally:
        return _producer


def pandas_handler():
    try:
        metrobus_data = get_data(uri_data)
        metrobus_df = pd.DataFrame.from_records(metrobus_data)
        metrobus_flat_df = json_normalize(data=metrobus_df['fields'])
        metrobus_gdf = gpd.GeoDataFrame(metrobus_flat_df,
                                        geometry=gpd.points_from_xy(metrobus_flat_df.position_longitude,
                                                                    metrobus_flat_df.position_latitude))
        geo_df = gpd.read_file(file_geo)
        merge_geo_data_df = gpd.sjoin(metrobus_gdf, geo_df, how="left", op='intersects')
        nomgeo_nomalize = merge_geo_data_df.nomgeo.str.normalize('NFKD').str.encode('ascii',
                                                                                    errors='ignore').str.decode('utf-8')
        merge_geo_data_df.nomgeo = nomgeo_nomalize
        result = merge_geo_data_df.to_json()
        parsed = json.loads(result)
        data_json = parsed["features"]
        cdmx_data = list(map(lambda x: x["properties"], data_json))
        return cdmx_data
    except Exception as ex:
        logging.error(str(ex))


def main():
    data = pandas_handler()
    metrobus_data = json.dumps(data)
    if metrobus_data != "null":
        print(metrobus_data)
        logging.info(metrobus_data)
        kafka_producer = connect_kafka_producer()
        publish_message(kafka_producer, 'metrobus_data_stream', 'cdmx_metrobus', metrobus_data)

    else:
        logging.warning("corrupted data")


if __name__ == '__main__':
    main()
    scheduler.add_job(main, 'interval', minutes=1)
    scheduler.start()
