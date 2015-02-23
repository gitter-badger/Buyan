(defproject app "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2913"]
                 [servant "0.1.3"]
                 [com.cemerick/clojurescript.test "0.3.1"]
                 [reagent "0.5.0-alpha"]
                 ]
  :preamble ["reagent/react.js"]

  :plugins [
            [lein-marginalia "0.8.0"]
            [lein-cljsbuild "1.0.5"]
            [com.cemerick/clojurescript.test "0.3.1"]
            ]

  :source-paths ["src" "test"]

  :cljsbuild {
              :builds [{:id "app"
                        :source-paths ["src" "test"]
                        :compiler {
                                   :output-to "app.js"
                                   ;:optimizations :none
                                   :source-maps true
                                   }}]
              :test-commands {"unit-tests" ["phantomjs" :runner
                                            "this.literal_js_was_evaluated=true"
                                            "bower_components/jquery/dist/jquery.js"
                                            "bower_components/peerjs/peer.js"
                                            ;"bower_components/localforage/src/localforage.js"
                                            ;"bower_components/pouchdb/dist/pouchdb.js"


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
