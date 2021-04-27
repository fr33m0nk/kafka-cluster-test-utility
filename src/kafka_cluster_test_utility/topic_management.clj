(ns kafka-cluster-test-utility.topic-management
  (:import (org.apache.kafka.streams.integration.utils EmbeddedKafkaCluster)))

(defn create-topics
  "Creates topic(s) on supplied Kafka cluster"
  [kafka-cluster ^EmbeddedKafkaCluster & topic-names]
  (try (.createTopics kafka-cluster (into-array topic-names))
       true
       (catch Exception e
         false)))

(defn delete-topics
  "Deletes topic(s) on supplied Kafka cluster"
  [kafka-cluster ^EmbeddedKafkaCluster & topic-names]
  (try (.deleteTopicsAndWait kafka-cluster (into-array topic-names))
       true
       (catch Exception e
         false)))

(defn recreate-topic
  "Recreates topic on supplied Kafka cluster"
  [kafka-cluster ^EmbeddedKafkaCluster topic-name]
  (try
    (delete-topics kafka-cluster topic-name)
    (finally
      (create-topics kafka-cluster topic-name))))

(defn recreate-topics
  "Recreates topics on supplied Kafka cluster"
  [kafka-cluster ^EmbeddedKafkaCluster topic-names]
  {:pre [(sequential? topic-names)]}
  (try
    (apply (partial delete-topics kafka-cluster) topic-names)
    (finally
      (apply (partial create-topics kafka-cluster) topic-names))))



