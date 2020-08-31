FROM python:3.8
MAINTAINER javier orta
WORKDIR /code
COPY requirements.txt .
RUN pip install -r requirements.txt
COPY src/ .
EXPOSE 80/tcp
EXPOSE 80/udp

CMD [ "python", "./producer.py" ]