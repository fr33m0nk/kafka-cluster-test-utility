(ns kafka-cluster-test-utility.topic-management-test
  (:require
    [clojure.test :refer :all]
    [kafka-cluster-test-utility.topic-management :as tm]
    [kafka-cluster-test-utility.kafka-cluster :as cluster]))

(deftest create-topics-test
  (try (let [kafka-cluster (cluster/start-cluster 3)]
         (testing "should create topics in a kafka cluster"
           (is (true? (tm/create-topics (:cluster kafka-cluster) "test-1" "test-2"))))
         (testing "should delete topics in a kafka cluster"
           (is (true? (tm/delete-topics (:cluster kafka-cluster) "test-1" "test-2"))))
         (testing "should recreate topic in a kafka cluster"
           (is (true? (tm/recreate-topics (:cluster kafka-cluster) ["test-1" "test-2"]))))
         (testing "should recreate topics in a kafka cluster"
           (is (true? (tm/recreate-topic (:cluster kafka-cluster) "test-1")))))
       (finally (cluster/stop-cluster))))
