(ns kafka-cluster-test-utility.topic-management
  (:require [kafka-cluster-test-utility.kafka-cluster-state :as s]
            [kafka-cluster-test-utility.utility :as utils])
  (:import (org.apache.kafka.streams.integration.utils EmbeddedKafkaCluster)
           (org.apache.kafka.common.config TopicConfig)))

(defn create-topics
  "Creates topic(s) on supplied Kafka cluster"
  [replication & topic-names]
  (utils/when-let* [running? (:running? @s/state)
                    kafka-cluster (:cluster @s/state)]
                   (try (doall
                          (map
                            #(.createTopic ^EmbeddedKafkaCluster kafka-cluster % 1 replication
                                           {TopicConfig/MIN_IN_SYNC_REPLICAS_CONFIG (str replication)})
                            topic-names))
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
  [replication topic-name]
  (utils/when-let* [running? (:running? @s/state)
                    kafka-cluster (:cluster @s/state)]
                   (try
                     (delete-topics topic-name)
                     (finally
                       (create-topics replication topic-name)))))

(defn recreate-topics
  "Recreates topics on supplied Kafka cluster"
  [replication topic-names]
  {:pre [(sequential? topic-names)]}
  (utils/when-let* [running? (:running? @s/state)
                    kafka-cluster (:cluster @s/state)]
                   (try
                     (apply delete-topics topic-names)
                     (finally
                       (apply (partial create-topics replication) topic-names)))))



