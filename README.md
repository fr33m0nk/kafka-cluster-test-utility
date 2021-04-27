# kafka-cluster-test-utility
[![Build Status](https://travis-ci.org//.svg?branch=master)](https://travis-ci.org//)
[![codecov](https://codecov.io/gh///branch/master/graph/badge.svg)](https://codecov.io/gh//)
[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.fr33m0nk/kafka-cluster-test-utility.svg)](https://clojars.org/net.clojars.fr33m0nk/kafka-cluster-test-utility)

A Clojure library designed to simplify the execution of end-to-end tests for applications that use Kafka as message broker or streaming platform.
The applications may :
* consume messages from Kafka topic
* produce messages to Kafka topic
* use Kafka streaming API

```clojure
[kafka-cluster-test-utility "0.2.0"]
```

## Features
* Small, simple library with thin wrapper over Java API
* Designed for users to provide Kafka dependencies

## Dependencies

This library specifies following as `:scope "provided"`, to enable usage with different versions.
That said, it is tested with the following versions of the dependencies:
```clojure
[org.clojure/clojure "1.10.1"]
[org.apache.kafka/kafka_2.13 "2.8.0"]
[org.apache.kafka/kafka-streams "2.8.0"]
[org.apache.kafka/kafka-clients "2.8.0"]
```

## Usage

This library provides convenience in namespace `kafka-cluster-test-utility.core`
, which can be used in tests.
Following test is an example of this library in action. 
```clojure
(ns kafka-cluster-test-utility.core-test
  (:require [clojure.test :refer :all]
    [kafka-cluster-test-utility.core :as core]
    [kafka-cluster-test-utility.utility :as utility])
  (:import [org.tensorflow.util.testlog PlatformInfo]))

(use-fixtures :once (core/with-embedded-kafka-cluster-and-topics 3 "test-topic"))

(deftest with-embedded-kafka-cluster-test
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
                                (map #(String. %))))))))
```

## Contacting me / contributions

Please use the project's [GitHub issues page] for all questions, ideas, etc. **Pull requests welcome**.

## License

Distributed under the [EPL v2.0].
Copyright © 2021 [Prashant Sinha].

<!--- Standard links -->
[fr33m0nk]: https://github.com/fr33m0nk
[Prashant Sinha]: https://www.linkedin.com/in/prashantsinha0

<!--- Standard links (repo specific) -->
[EPL v2.0]: https://raw.githubusercontent.com/fr33m0nk/kafka-cluster-test-utility/master/LICENSE
[GitHub issues page]: https://github.com/fr33m0nk/kafka-cluster-test-utility/issues
