# Review

Este proyecto tiene como objetivo escrapear la información del metrobus de la cdmx en tiempo real, además de a esa informacion agregarles la alcaldia, para eso se descargo y se agrega el archivo alcaldias.geojson.
Se uiliza pandas y geopandas, el cual se utilizan para pasar la informacion tanto del archivo como del scrapping, y genrar dataframes de ambos, para despues por los
geometry points hacer el join de ambos, una vez que se tiene ese dataframe se procede a pasarlo a json y publicarlo en confluente kafka, en el topic correspondiente.
Se agregan los archivos de requierements y de alcaldias.geojson

## Dockerfile
Se agrega el archivo dockerfile, instrucciones para construccion:
### Build Docker
* docker build -t javierorta/cdmx-scraper:1.0 .

### Publicar Docker 
* docker image push javierorta/cdmx-scraper:1.0

### Run image
* docker run javierorta/cdmx-scraper:1.0

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