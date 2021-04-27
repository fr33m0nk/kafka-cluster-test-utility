(ns kafka-cluster-test-utility.producer
  (:import (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)))

(defn get-producer
  "Returns a Kafka Producer"
  [properties]
  (KafkaProducer. properties))

(defn send-message
  "Produces to a topic using provided producer"
  [producer ^KafkaProducer topic message]
  (->> message
       (ProducerRecord. topic)
       (.send producer)
       (.get)))