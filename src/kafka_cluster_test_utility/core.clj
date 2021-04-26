(ns kafka-cluster-test-utility.core
  (:require
    [kafka-cluster-test-utility.kafka-cluster :as cluster]
    [kafka-cluster-test-utility.topic-management :as topic]
    [kafka-cluster-test-utility.producer :as p]
    [kafka-cluster-test-utility.consumer :as c]
    [kafka-cluster-test-utility.utility :as u])
  (:import
    (org.apache.kafka.clients.producer ProducerConfig)
    (org.apache.kafka.clients.consumer ConsumerConfig)
    (org.junit.contrib.java.lang.system EnvironmentVariables)))

(defn- with-consumer [f topic timeout-in-seconds]
  (let [properties (assoc u/default-properties
                         ConsumerConfig/BOOTSTRAP_SERVERS_CONFIG (cluster/get-bootstrap-server))]
    (with-open [consumer (c/get-consumer properties)]
      (f consumer topic timeout-in-seconds))))

(defn set-env-bootstrap-servers
  [bootstrap-environment-variable]
  (.set (EnvironmentVariables.) bootstrap-environment-variable (cluster/get-bootstrap-server)))

(defn with-embedded-kafka-cluster-and-topics [number-of-brokers & topics]
  (fn [f]
    (try (let [kafka-cluster (cluster/start-cluster number-of-brokers)]
           (topic/recreate-topics (:cluster kafka-cluster) topics)
           (f))
         (finally
           (cluster/stop-cluster)))))

(defn send-with-producer [topic message]
  (let [properties (assoc u/default-properties
                         ProducerConfig/BOOTSTRAP_SERVERS_CONFIG (cluster/get-bootstrap-server))]
    (with-open [producer (p/get-producer properties)]
      (p/send-message producer topic message))))

(defn with-consumer-read-one [topic timeout-in-seconds]
  (with-consumer c/read-one-from-topic topic timeout-in-seconds))

(defn with-consumer-read-multiple [topic timeout-in-seconds]
  (with-consumer c/read-multiple-from-topic topic timeout-in-seconds))

