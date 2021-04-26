(ns kafka-cluster-test-utility.kafka-cluster
  (:require [kafka-cluster-test-utility.kafka-cluster-state :as s])
  (:import (org.apache.kafka.streams.integration.utils EmbeddedKafkaCluster)))

(defmacro when-let*
  ([bindings & body]
   (if (seq bindings)
     `(when-let [~(first bindings) ~(second bindings)]
        (when-let* ~(drop 2 bindings) ~@body))
     `(do ~@body))))

(defn- call-method
  [obj method-name & args]
  (let [m (first (filter (fn [x] (.. x getName (equals (name method-name))))
                         (.. obj getClass getDeclaredMethods)))]
    (. m (setAccessible true))
    (. m (invoke obj (into-array Object args)))))

(defn- get-cluster
  [number-of-brokers]
  (EmbeddedKafkaCluster. number-of-brokers))

(defn- cluster-operation
  [kafka-cluster operation]
  (condp = operation
    :start (call-method kafka-cluster :before)
    :stop  (call-method kafka-cluster :after)))

(defn start-cluster
  [number-of-brokers]
  (let [start (fn [] (do
                       (cluster-operation (:cluster @s/state) :start)
                       (swap! s/state update :running? not)))]
    (cond (-> @s/state :cluster nil?) (do (swap! s/state
                                               assoc
                                               :cluster (get-cluster number-of-brokers)
                                               :running? false)
                                        (start))
          (false? (:running? @s/state)) (start)
          :otherwise @s/state)))

(defn stop-cluster
  []
  (let [stop (fn [] (do
                      (cluster-operation (:cluster @s/state) :stop)
                      (swap! s/state update :running? not)))]
    (if (true? (:running? @s/state))
      (stop)
      @s/state)))

(defn get-bootstrap-server
  []
  (when-let* [running? (:running? @s/state)
             kafka-cluster (:cluster @s/state)]
    (.bootstrapServers kafka-cluster)))




