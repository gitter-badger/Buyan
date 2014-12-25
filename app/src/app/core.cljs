(ns app.core
 (:require
[cljs.core.async :refer [chan close! timeout put!]]
[servant.core :as servant]
[servant.worker :as worker])
(:require-macros [cljs.core.async.macros :as m :refer [go]]
[servant.macros :refer [defservantfn]]) )


 (.log js/console "this runs in the browser")

(def worker-count 2)
(def worker-script "hamiyoca/miner.js")
 (def servant-channel (servant/spawn-servants worker-count worker-script))
  (def channel-1 (servant/servant-thread servant-channel servant/standard-message some-random-fn 5 6))
(enable-console-print!)
(. js/console (log (THREE/Scene. )))
 (go
(println
"The value from the first call is "
;; We block on the value from the first servant
(<! channel-1))
;; That's enough fun for now, but I need to get back to my yacht so servants, finish yourselves.
(println "Killing webworkers")
;; Here you have the ability to specify how many workers to kill off.
(servant/kill-servants servant-channel worker-count)
;; Notice how you don't even have to think about setting up a whole new context, dealing with
;; individual web workers, or handling messages!
)

(println "Hello wor 32 d rdaldad!")
;(def myWorker (js/Worker. "hamiyoca/miner.js"))
;(def ^:dynamic o (js-obj "foo" 1 "bar" 2))
;(. myWorker (postMessage o))
