# Review

El proyecto es un api rest, utilizando play framework, scala, postgresql, docker, kubernetetes.
El gestor del proyecto es sbt, por lo que se recomienda tener instalado sbt.
Se recomienda utilizar el ide intellijidea para desarrollo


El proyecto se encarga de generar 4 endpoints de acuerdo a los requerimientos:
* Obtener una lista de unidades disponibles
* Consultar los el historial de ubicaciones/fechas de una unidad dado su ID
* Obtener una lista de alcaldías disponibles
* Obtener una lista de unidades que hayan estado dentro de una alcaldía

## Estructura
### app
Esla estructura del codigo del proyecto

### conf
En esta carpeta se encuentra toda la configuracion correspondiente de acuerdo a la conexion de la bd, la seguridad secrets, permisos de edpoints publico.

### app controllers
 Son los controladores del servicio de exposicion, como tal solo se necesita un service ya que de ahi se generan 4 rutas o endpoints.

### app dao
en esta carpeta se tienen el orm a la bases de datos, de acuerdo a la configuracion dada en el archivo ```application.conf ```, se encarga de generar los query's para obtener la información y realizar la serializacion del resultado del query con los case class.

## app models
En esta carpeta se tienen los case class de acuero a la serializacion de los request y de la serializacon del dao orm, es para mantener la informacion con case class y tenga una estructura definida para el manejo y exposicion de la informacion.

## Routes

### Obtener una lista de unidades disponibles
* GET http://192.168.99.100:31289/unidades

### Obtener una lista de alcaldías disponibles
* GET http://192.168.99.100:31289/alcaldias

### Obtener una lista de unidades que hayan estado dentro de una alcaldía
* GET http://192.168.99.100:31289/unidadesalcaldias

### Consultar los el historial de ubicaciones/fechas de una unidad dado su ID
* POST http://192.168.99.100:31289/historial
* payload post, agregar el id correspondiente a buscar:
``` { "id" : 178 } ```

## Build play
### Compilar el proyecto de play
Para compilar se debe de ejecutar el siguiente comando:
* ``` sbt dist ```.
De la carpeta ``` /cdmx-rest-api/target/universal ``` copiar el archivo ``` cdmx-rest-api-1.0-SNAPSHOT.zip ``` y descomprimirlo y colocarlo en una ubicación cercana al ``` Dockerfile ```, ya que el dockerfile necesita del contenido de ese archivo descomprimido para hacer el build.

Para ejecutar de modo desarrollo se recomiendo importarlo en ide como idea y ejecutar sbt run, y probar la aplicación.

## Dockerfile
Se agrega el archivo dockerfile, instrucciones para construccion:

### Build Docker
* docker build -t javierorta/cdmx-rest-api:2.0 .

### Publicar Docker 
* docker image push javierorta/cdmx-rest-api:2.0

### Run image
* docker run  -it -p 9000:9000 -p 9443:9443  javierorta/cdmx-rest-api:2.0

## Kubernetes
Dentro de la carpeta de kubernetes se agregan los archivo yml de deploy y svc

### deploy.yml
Es el archivo de configuracion de deploy del pod, de acuerdo a la imagen publicada en docker hub.

## svc.yml
Es el archivo de Service del deploy y del pod, tiene la configuracion del servicio de exponer el pod correspondiente,
la configuración utilizada es : NodePort pero se le puede configurar para un loadbalancer de una nube.

### minikube 
Esta configurado kubernetes en modo local con minikube, prueba :
* minikube start
* minikube addons enable ingress
* minikube ip

### Kubernetes despliegue:

* kubectl apply -f deploy.yml 
* kubectl apply -f svc

Ver pods:
* kubectl get pods -o wide

Ver puertos de salida del service:
* kubectl get svc


De acuero a la ip externa ya sea por minikube o por un loadbalancer