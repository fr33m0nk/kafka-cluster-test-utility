(ns kafka-cluster-test-utility.kafka-cluster
  (:require
    [kafka-cluster-test-utility.kafka-cluster-state :as s]
    [kafka-cluster-test-utility.utility :as utils])
  (:import (org.apache.kafka.streams.integration.utils EmbeddedKafkaCluster)))

(defn- call-method
  [obj method-name & args]
  (let [m (first (filter (fn [x] (.. x getName (equals (name method-name))))
                         (.. obj getClass getDeclaredMethods)))]
    (. m (setAccessible true))
    (. m (invoke obj (into-array Object args)))))

(defn- get-cluster
  []
  (EmbeddedKafkaCluster. 1))

(defn- cluster-operation
  [kafka-cluster operation]
  (condp = operation
    :start (call-method kafka-cluster :before)
    :stop (call-method kafka-cluster :after)))

(defn start-cluster
  "Starts a embedded Kafka cluster with provided number of brokers.
  Changes state of atom to manage cluster state centrally."
  []
  (let [start (fn [] (do
                       (cluster-operation (:cluster @s/state) :start)
                       (swap! s/state update :running? not)))]
    (cond (-> @s/state :cluster nil?) (do (swap! s/state
                                                 assoc
                                                 :cluster (get-cluster)
                                                 :running? false)
                                          (start))
          (false? (:running? @s/state)) (start)
          :otherwise @s/state)))

(defn stop-cluster
  "Stops embedded Kafka cluster if running. Changes state of atom to manage cluster state centrally."
  []
  (let [stop (fn [] (do
                      (cluster-operation (:cluster @s/state) :stop)
                      (swap! s/state update :running? not)))]
    (if (true? (:running? @s/state))
      (stop)
      @s/state)))

(defn get-bootstrap-server
  "Returns the bootstrap servers if embedded Kafka cluster is running else nil."
  []
  (utils/when-let* [running? (:running? @s/state)
                    ^EmbeddedKafkaCluster kafka-cluster (:cluster @s/state)]
                   (.bootstrapServers kafka-cluster)))




