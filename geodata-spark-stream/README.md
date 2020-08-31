# Review

Este proyecto es el spark streaming, encargado de obtener los dats del topic de conleunte kafka y hacer validaciones de no registrar datos ya almacenados, solo datos recientes, y agregando una columna de fecha numerica clave el cual se encarga de hacer esa validacion de fechas.
Se utiliza maven con gestor del proyecto, scala.
Se utiliza ``` spark 3.0 ```, por lo que se recomienda utilizar un cluster con esta version de spark.
Se recomienda utilizar el ide intellijidea para desarrollo


## Compilar

Dentro de la carpeta abrir una terminal y ejecutar el siguiente comando, el cual se encarga de compilar y generar el jar conrrepondiente.
```mvn clean package```

Dentro de la carpeta que se genere ```/target```, copiar el jar ```spark-scala-maven-project-0.0.1-SNAPSHOT-jar-with-depencencies.jar``` junto con ```postgresql-42.2.5.jar ```.
El jar ```spark-scala-maven-project-0.0.1-SNAPSHOT-jar-with-depencencies.jar``` se utilizara para deployar en un cluster de spark, se tiene configurado en un cluster local pero se puede cambiar de acuerdo al cluster, tambien el spark submit se tiene configurado para un cluster local standalone pero igual se puede modificar.

Tambien se agrega un archivo ```jaas``` de configuración de conexion a confluente kafka; se utiliza postgresql como db sink, en el cual se almacenan los datos procesados del scrapping y que sean vaidos por la fecha.

## Ejecucion
En un cluster de spark, default standalone, realizar el siguiente submit:
``` spark-submit --driver-class-path postgresql-42.2.5.jar --jars postgresql-42.2.5.jar --class com.cdmx.MetrobusCMDXData --files /<path>/geodata-spark-stream/kafka_client_jaas.conf --conf "spark.executor.extraJavaOptions=-Djava.security.auth.login.config=/<path>/geodata-spark-stream/kafka_client_jaas.conf" --conf "spark.driver.extraJavaOptions=-Djava.security.auth.login.config=/<path>/geodata-spark-stream/kafka_client_jaas.conf" spark-scala-maven-project-0.0.1-SNAPSHOT-jar-with-dependencies.jar ```

## Notas
* agregar el ``` <path> ```, setear el path correspondiente
* tomar en cuenta que se tiene configurado para un cluster local
* el archivo de ```postgresql-42.2.5.jar ```, ```spark-scala-maven-project-0.0.1-SNAPSHOT-jar-with-depencencies.jar``` y  ```kafka_client_jaas.conf ``` deben ser alcanzables para el cluster se recomienda untilizar hdfs, s3, o cualquier filesystem distribuido cloud, o si es local en una carpeta donde esten juntos.
* se recomienda utilizar un cluster local standalone por la configuracion actual, pero se puede utilizar es aws emr, databricks, cloudera.