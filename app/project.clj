(defproject app "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2371"]
                 [servant "0.1.3"]
                 [com.cemerick/clojurescript.test "0.3.1"]
                 ]
  :plugins [
            [lein-marginalia "0.8.0"]
            [lein-cljsbuild "1.0.4-SNAPSHOT"]
            [com.cemerick/clojurescript.test "0.3.1"]
            ]

  :source-paths ["src" "test"]

  :cljsbuild {
              :builds [{:id "app"
                        :source-paths ["src" "test"]
                        :compiler {
                                   :output-to "app.js"
                                   :optimizations :whitespace
                                   }}]
              :test-commands {"unit-tests" ["phantomjs" :runner
                                            "this.literal_js_was_evaluated=true"
                                            "pouchdb-3.2.0.min.js"
                                              "bower_components/jquery/dist/jquery.js"
                                               "bower_components/peerjs/peer.js"
                                            ;
                                              "scripts/cjs.js"
                                            ; "goog.require('cemerick.cljs.test')"
                                            ;  "goog.require('app.main')"
                                            ;s"target/cljsbuild-compiler-0/goog/base.js"
                                             "app.js"
                                            ;"target/cljsbuild-compiler-0/testt.js"
                                            ;"goog.require('app.testt')"
                                            ; "target/cljsbuild-compiler-0/test.js"

                                            ]}


              })
