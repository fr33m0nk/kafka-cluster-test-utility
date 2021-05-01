(ns kafka-cluster-test-utility.topic-management-test
  (:require
    [clojure.test :refer :all]
    [kafka-cluster-test-utility.topic-management :as tm]
    [kafka-cluster-test-utility.kafka-cluster :as cluster]))

(defn fixture
  [f]
  (try (cluster/start-cluster)
       (f)
       (finally
         (cluster/stop-cluster))))

(use-fixtures :once fixture)

(deftest create-topics-test
  (testing "should create topics in a kafka cluster"
    (is (true? (tm/create-topics "test-1" "test-2"))))
  (testing "should delete topics in a kafka cluster"
    (is (true? (tm/delete-topics "test-1" "test-2"))))
  (testing "should recreate topic in a kafka cluster"
    (is (true? (tm/recreate-topics ["test-1" "test-2"]))))
  (testing "should recreate topics in a kafka cluster"
    (is (true? (tm/recreate-topic "test-1")))))
