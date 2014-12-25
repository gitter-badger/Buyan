(ns app.core)

(enable-console-print!)
(. js/console (log (THREE/Scene. )))
(println "Hello wor 32 d rdaldad!")
(def myWorker (js/Worker. "wrkr.js"))