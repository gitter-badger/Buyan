(ns app.core)

(enable-console-print!)
(. js/console (log (THREE/Scene. )))
(println "Hello wor 32 d rdaldad!")
(def myWorker (js/Worker. "hamiyoca/miner.js"))
(def ^:dynamic o (js-obj "foo" 1 "bar" 2))
(. myWorker (postMessage o))
