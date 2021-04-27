(defproject net.clojars.fr33m0nk/kafka-cluster-test-utility "0.2.0"
  :description "Embedded Kafka Cluster and Protobuf util"
  :url "https://github.com/fr33m0nk/kafka-cluster-test-utility"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v20.html"}
  :scm {:name "git"
        :url "https://github.com/fr33m0nk/kafka-cluster-test-utility"}
  :pom-addition [:developers [:developer {:id "benbit"}
                              [:name "Prashant"]
                              [:url "http://www.example.com/benjamin"]]]
  :dependencies [[org.clojure/tools.namespace "1.1.0"]
                 [junit/junit "4.13.2"]
                 [org.junit.jupiter/junit-jupiter-api "5.7.1"]
                 [com.github.stefanbirkner/system-rules "1.16.1" :exclusions [junit/junit-dep]]
                 [clojusc/protobuf "3.5.1-v1.1"]
                 [org.clojure/clojure "1.10.1" :scope "provided"]
                 [org.apache.kafka/kafka_2.13 "2.8.0" :scope "provided"]
                 [org.apache.kafka/kafka-streams "2.8.0" :scope "provided"]
                 [org.apache.kafka/kafka-clients "2.8.0" :scope "provided"]]
  :plugins [[lein-cloverage "1.0.13"]
            [lein-shell "0.5.0"]
            [lein-ancient "0.6.15"]
            [lein-changelog "0.3.2"]]
  :profiles {:uberjar  {:aot :all}
             :dev      {:global-vars  {*warn-on-reflection* true}
                        :dependencies [[org.tensorflow/proto "1.15.0"]
                                       [org.apache.kafka/kafka_2.13 "2.8.0" :classifier "test" :scope "provided"]
                                       [org.apache.kafka/kafka-streams "2.8.0" :classifier "test" :scope "provided"]
                                       [org.apache.kafka/kafka-clients "2.8.0" :classifier "test" :scope "provided"]]}
             :provided {:dependencies []}}
  :deploy-repositories [["clojars" {:url      "https://repo.clojars.org"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass}]]
  :aliases {"update-readme-version" ["shell" "sed" "-i" "s/\\\\[kafka-cluster-test-utility \"[0-9.]*\"\\\\]/[kafka-cluster-test-utility \"${:version}\"]/" "README.md"]}
  :release-tasks [["shell" "git" "diff" "--exit-code"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag"]
                  ["deploy"]
                  ["vcs" "push"]])
