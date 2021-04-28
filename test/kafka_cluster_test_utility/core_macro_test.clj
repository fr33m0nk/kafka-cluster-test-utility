(ns kafka-cluster-test-utility.core-macro-test
  (:require [clojure.test :refer :all]
            [kafka-cluster-test-utility.core :as core]))

(deftest with-embedded-kafka-cluster-test
  (testing "should produce and consume message with in the with-embedded-kafka-cluster macro"
    (core/with-embedded-kafka-cluster 3
                                      ["test-1"]
                                      (core/send-with-producer "test-topic" (.getBytes "hello dost"))
                                      (is (= "hello dost"
                                             (->> (core/with-consumer-read-one "test-topic" 2)
                                                  String.))))))
