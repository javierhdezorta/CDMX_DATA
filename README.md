## Data pipeline

Se desarrollo la prueba tecnica de acuerdo a los requerimientos:
Se utilizaron las siguientes herramientas:
- kubernetes
- spark 3.0
- confluent cloud (kafka)
- python pandas
- postgresql 12
- play framework

La estructura del proyecto es:

## producerDataCDMX
Este proyecto es un scrapper de python, el cual cada minuto pullea la informacion del metrobus cdmx, junto con las alcaldias, se utiliza pandas para hacer el merge 
entre los datos que se estan escrapeando del metrobus con las alcaldias, para generar un df completo con ambos datos tanto de metrobus y alcaldias, y posteriormente
se se genera un json del df para la publicación en confluent kafka en el topic correspondiente.
Tambien se incluye el dockerfile, y los yml de kubernetes, ya se encuentra el container en docker hub.

## geodata-spark-stream
Este proyecto es el spark stream que se utiliza para manjear los streams que se genran en confluent kafka.
Es un proyecto de spark con scala, el cual toma de la conexion al topic de confluent y empieza a consumir la informacion que se va ingestando en el topic,
las trasnformacion que se hacen es que se genera un columna de fecha numerica el cual nos sirve  para validar que la informacion que se obtiene sea diferente a la que
se esta registrando, es deci validar que la info del stream no se inserte continuamente.
Actualmente esta configurado en local pero se puede modificar, también com fuente sink se utiliza portgres, una version en cloud.

## cdmx-rest-api
Este proyecto se desarrollo con play framework y scala, es un api rest el cual consume de los datos de postgresql, de los datos generados por el proceso de spark streaming, tiiene 4 endpoints que son de acuerdo a los requerimientos.
Tambien se incluye el dockerfile, además de los yml de kubernetes, el container ya se encuentra registrado en docker hub.
 
## Configuracion
Actualmente se tiene un cluster de confluent kafka, y un cluster de postgresql, se recominda realizar la configuracion adecuado comforme al seteo de la urls de los servicios y configuración de los archivos necesarios tanto en el play framwork, spark submit, spark session, svc.yml, etc:
- postgresql
- spark cluster
- kafka cluster
- kubernetes cluster

Actualmente se puede consumir la informacion ya que son trial, pero el postgresql tiene un limite de conexiones de 5 clients,
* Se anexa el diagrama de la solucion 
