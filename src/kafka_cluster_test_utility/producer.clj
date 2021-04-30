(ns kafka-cluster-test-utility.producer
  (:import (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (java.util Map)))

(defn get-producer
  "Returns a Kafka Producer"
  ^KafkaProducer
  [^Map properties]
  (KafkaProducer. properties))

(defn send-message
  "Produces to a topic using provided producer"
  [^KafkaProducer producer topic message]
  (->> message
       (ProducerRecord. topic)
       (.send producer)
       (.get)))