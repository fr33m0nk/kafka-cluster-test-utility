(ns kafka-cluster-test-utility.utility
  (:require [protobuf.core :as proto])
  (:import
    (org.apache.kafka.clients.producer ProducerConfig)
    (org.apache.kafka.clients.consumer ConsumerConfig)
    (java.util UUID)))

(def default-properties
  {ProducerConfig/KEY_SERIALIZER_CLASS_CONFIG   "org.apache.kafka.common.serialization.StringSerializer"
   ProducerConfig/VALUE_SERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.ByteArraySerializer"
   ConsumerConfig/AUTO_OFFSET_RESET_CONFIG      "earliest"
   ConsumerConfig/KEY_DESERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.StringDeserializer"
   ConsumerConfig/GROUP_ID_CONFIG               (str "test_consumer" (str (UUID/randomUUID)))
   ConsumerConfig/VALUE_DESERIALIZER_CLASS_CONFIG "org.apache.kafka.common.serialization.ByteArrayDeserializer"})

(defn clj-map->bytes
  "Generates byte array representation from clojure map using Protobuf class"
  [proto-klass message] ^"[B"
  (->> message
       (proto/create proto-klass)
       proto/->bytes))

(defn bytes->clj-map
  "Generates clojure map from byte array representation of protobuf object using Protobuf class"
  [proto-klass bytes]
  (-> (proto/create proto-klass)
      (proto/bytes-> bytes)
      ((partial into {}))))
