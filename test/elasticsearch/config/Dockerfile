FROM ubuntu

RUN apt-get update && \
apt-get install -y curl

COPY ./es_schema.sh /

ENTRYPOINT ["/es_schema.sh"]