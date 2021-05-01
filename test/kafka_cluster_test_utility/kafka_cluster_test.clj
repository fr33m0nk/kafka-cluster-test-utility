(ns kafka-cluster-test-utility.kafka-cluster-test
  (:require
    [kafka-cluster-test-utility.kafka-cluster-state :as state]
    [clojure.test :refer :all]
    [kafka-cluster-test-utility.kafka-cluster :as c :refer [start-cluster stop-cluster get-bootstrap-server]]
    [clojure.string :as str]))

(deftest start-cluster-test
  (testing "should return a map with cluster and running? true if cluster is not up"
    (reset! state/state {:cluster nil :running? false})
    (start-cluster)
    (is (not (nil? (:cluster @state/state))))
    (is (:running? @state/state)))
  (testing "should return a map with cluster and running? true if cluster is up but not running"
    (reset! state/state {:cluster (#'c/get-cluster) :running? false})
    (start-cluster)
    (is (not (nil? (:cluster @state/state))))
    (is (:running? @state/state)))
  (testing "should return a map with cluster and running? true if cluster is running"
    (reset! state/state {:cluster (#'c/get-cluster) :running? true})
    (start-cluster)
    (is (not (nil? (:cluster @state/state))))
    (is (:running? @state/state))))

(deftest stop-cluster-test
  (testing "should return a map with cluster nil and running? false if cluster is not up"
    (reset! state/state {:cluster nil :running? false})
    (stop-cluster)
    (is (= {:cluster nil :running? false} @state/state)))
  (testing "should return a map with cluster and running? false if cluster is up"
    (let [cluster (#'c/get-cluster)]
      (reset! state/state {:cluster cluster :running? false})
      (stop-cluster)
      (is (= {:cluster cluster :running? false} @state/state))))
  (testing "should return a map with cluster and running? false if cluster is up and running"
    (let [cluster (start-cluster)]
      (stop-cluster)
      (is (= {:cluster (:cluster cluster) :running? false} @state/state)))))

(deftest get-bootstrap-server-test
  (testing "should return nil if cluster is not up and running"
    (stop-cluster)
    (is (nil? (get-bootstrap-server))))
  (testing "should return bootstrap server if cluster is up and running"
    (start-cluster)
    (let [bootstrap-server (get-bootstrap-server)]
      (is (not (nil? bootstrap-server)))
      (is (str/includes? bootstrap-server "localhost:")))
    (stop-cluster)))
