(ns kafka-cluster-test-utility.topic-management
  (:require [kafka-cluster-test-utility.kafka-cluster-state :as s]
            [kafka-cluster-test-utility.utility :as utils])
  (:import (org.apache.kafka.streams.integration.utils EmbeddedKafkaCluster)))

(defn create-topics
  "Creates topic(s) on supplied Kafka cluster"
  [& topic-names]
  (utils/when-let* [running? (:running? @s/state)
                    kafka-cluster (:cluster @s/state)]
                   (try (doall
                          (map #(.createTopic ^EmbeddedKafkaCluster kafka-cluster % 1 1) topic-names))
                        true
                        (catch Exception e
                          false))))

(defn delete-topics
  "Deletes topic(s) on supplied Kafka cluster"
  [& topic-names]
  (utils/when-let* [running? (:running? @s/state)
                    kafka-cluster (:cluster @s/state)]
                   (try (.deleteTopicsAndWait ^EmbeddedKafkaCluster kafka-cluster (into-array topic-names))
                        true
                        (catch Exception e
                          false))))

(defn recreate-topic
  "Recreates topic on supplied Kafka cluster"
  [topic-name]
  (utils/when-let* [running? (:running? @s/state)
                    kafka-cluster (:cluster @s/state)]
                   (try
                     (delete-topics topic-name)
                     (finally
                       (create-topics topic-name)))))

(defn recreate-topics
  "Recreates topics on supplied Kafka cluster"
  [topic-names]
  {:pre [(sequential? topic-names)]}
  (utils/when-let* [running? (:running? @s/state)
                    kafka-cluster (:cluster @s/state)]
                   (try
                     (apply delete-topics topic-names)
                     (finally
                       (apply create-topics topic-names)))))



