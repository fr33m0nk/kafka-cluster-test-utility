(ns kafka-cluster-test-utility.core-test
  (:require [clojure.test :refer :all]
            [kafka-cluster-test-utility.core :as core]
            [kafka-cluster-test-utility.utility :as utility]
            [clojure.string :as str])
  (:import [org.tensorflow.util.testlog PlatformInfo]))

(use-fixtures :once (core/with-embedded-kafka-cluster-and-topics "test-topic"))

(deftest with-embedded-kafka-cluster-and-topics-test
  (testing "should produce and consume message from cluster"
    (let [message {:release "4.19.0"}]
      (core/send-with-producer "test-topic" (utility/clj-map->bytes PlatformInfo message))
      (is (= {:release "4.19.0"}
             (->> (core/with-consumer-read-one "test-topic" 2)
                  (utility/bytes->clj-map PlatformInfo))))))

  (testing "should produce and consume many message from cluster"
    (let [first-message "Hello 1!"
          second-message "Hello 2!"
          third-message "Hello 3!"]
      (core/send-with-producer "test-topic" (.getBytes first-message))
      (core/send-with-producer "test-topic" (.getBytes second-message))
      (core/send-with-producer "test-topic" (.getBytes third-message))
      (is (= [first-message second-message third-message]
             (->> (core/with-consumer-read-multiple "test-topic" 2)
                  (map #(String. ^"[B" %))))))))

(deftest set-env-bootstrap-servers-test
  (testing "should set the environment variable with values"
    (core/set-env-bootstrap-servers "BOOTSRAPSERVERS")
    (is (str/starts-with? (System/getenv "BOOTSRAPSERVERS") "localhost"))))
