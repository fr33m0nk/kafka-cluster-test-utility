(ns kafka-cluster-test-utility.utility-test
  (:require [clojure.test :refer :all])
  (:require [kafka-cluster-test-utility.utility :refer [clj-map->bytes bytes->clj-map]])
  (:import (org.tensorflow.util.testlog PlatformInfo)))

(deftest clj-map->bytes-test
  (testing "should return byte-array for clojure map representation of protobuf class"
    (let [actual-byte-array (clj-map->bytes PlatformInfo {:release "4.19.0"})]
      (is (= "4.19.0" (.getRelease (PlatformInfo/parseFrom ^"[B" actual-byte-array)))))))

(deftest bytes->clj-map-test
  (testing "should return clojure map representation of protobuf class for its byte-array"
    (let [byte-array (-> (PlatformInfo/newBuilder) (.setRelease "4.19.0") .build .toByteArray)]
      (is (= {:release "4.19.0"} (bytes->clj-map PlatformInfo byte-array))))))
