(ns kafka-cluster-test-utility.producer
  (:import (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)))

(defn get-producer
  [properties]
  (KafkaProducer. properties))

(defn send-message
  [producer ^KafkaProducer topic message]
  (->> message
       (ProducerRecord. topic)
       (.send producer)
       (.get)))