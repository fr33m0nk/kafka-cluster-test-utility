(ns kafka-cluster-test-utility.consumer
  (:import (org.apache.kafka.clients.consumer KafkaConsumer ConsumerRecord ConsumerRecords)
           (java.time Duration)
           (java.util Map Collection)))

(defn get-consumer
  "Returns a Kafka Consumer"
  ^KafkaConsumer
  [^Map properties]
  (KafkaConsumer. properties))

(defn read-one-from-topic
  "Consumes one message from provided Kafka topic."
  [^KafkaConsumer consumer ^String topic-name timeout-in-seconds]
  (.subscribe consumer ^Collection (list topic-name))
  (let [consumer-records (.poll consumer (Duration/ofSeconds (long timeout-in-seconds)))
        records (iterator-seq (.iterator (.records consumer-records topic-name)))]
    (when-let [^ConsumerRecord record (first records)]
      (.value record))))

(defn read-multiple-from-topic
  "Consumes all message from provided Kafka topic."
  [^KafkaConsumer consumer ^String topic-name timeout-in-seconds]
  (.subscribe consumer ^Collection (list topic-name))
  (let [consumer-records (.poll consumer (Duration/ofSeconds (long timeout-in-seconds)))
        records (iterator-seq (.iterator (.records consumer-records topic-name)))
        record-coll (map #(.value ^ConsumerRecord %) records)]
    record-coll))