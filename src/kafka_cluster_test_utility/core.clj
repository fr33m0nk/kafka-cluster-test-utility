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

(defn get-bootstrap-server []
  "Returns the bootstrap servers provided by the embedded Kafka cluster instance.
  Bootstrap server is returned only if embedded Kafka cluster is running else nil."
  (cluster/get-bootstrap-server))

(defn set-env-bootstrap-servers
  "Sets the provided environment variable with bootstrap servers provided by the embedded Kafka cluster instance.
  This variable needs to be same as one used by the application for connecting to Kafka"
  [bootstrap-environment-variable]
  (.set (EnvironmentVariables.) bootstrap-environment-variable (cluster/get-bootstrap-server)))

(defn with-embedded-kafka-cluster-and-topics
  "Convenience method for returning a function which takes a function as parameter.
  Returned function would start embedded Kafka cluster as well as create the provided topics.
  This can be used as a test fixtures."
  [& topics]
  (fn [f]
    (try
      (cluster/start-cluster)
      (topic/recreate-topics topics)
      (f)
      (finally
        (cluster/stop-cluster)))))

(defmacro with-embedded-kafka-cluster
  "Macro that wraps and executes body within after starting Kafka cluster creating topics"
  [topic-list & body]
  {:pre [(sequential? topic-list)]}
  `(try
     (cluster/start-cluster)
     (topic/recreate-topics ~topic-list)
     ~@body
     (finally
       (cluster/stop-cluster))))

(defn send-with-producer [topic message]
  "Produces the provided message to Kafka topic provided.
  This uses the default properties to do so."
  (let [properties (assoc u/default-properties
                     ProducerConfig/BOOTSTRAP_SERVERS_CONFIG (cluster/get-bootstrap-server))]
    (with-open [producer (p/get-producer properties)]
      (p/send-message producer topic message))))

(defn with-consumer-read-one
  "Consumes one message from provided Kafka topic.
  This uses the default properties to do so."
  [topic timeout-in-seconds]
  (with-consumer c/read-one-from-topic topic timeout-in-seconds))

(defn with-consumer-read-multiple
  "Consumes all message from provided Kafka topic.
  This uses the default properties to do so."
  [topic timeout-in-seconds]
  (with-consumer c/read-multiple-from-topic topic timeout-in-seconds))

