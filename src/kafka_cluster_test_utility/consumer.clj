(ns kafka-cluster-test-utility.consumer
  (:import (org.apache.kafka.clients.consumer KafkaConsumer)
           (java.time Duration)))

(defn get-consumer
  [properties]
  (KafkaConsumer. properties))

(defn read-one-from-topic
  [consumer ^KafkaConsumer topic-name timeout-in-seconds]
  (.subscribe consumer [topic-name])
  (let [consumer-records (.poll consumer (Duration/ofSeconds (long timeout-in-seconds)))
        records (iterator-seq (.iterator (.records consumer-records topic-name)))]
    (when-let [record (first records)]
      (.value record))))

(defn read-multiple-from-topic
  [consumer ^KafkaConsumer topic-name timeout-in-seconds]
  (.subscribe consumer [topic-name])
  (let [consumer-records (.poll consumer (Duration/ofSeconds (long timeout-in-seconds)))
        records (iterator-seq (.iterator (.records consumer-records topic-name)))
        record-coll (map #(.value %) records)]
    record-coll))