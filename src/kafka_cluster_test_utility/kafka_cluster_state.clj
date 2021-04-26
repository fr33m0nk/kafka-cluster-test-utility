(ns kafka-cluster-test-utility.kafka-cluster-state
  (:require [clojure.tools.namespace.repl :as repl]))

(repl/disable-reload!)

(def state (atom {:cluster  nil
                  :running? false}))