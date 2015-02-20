(ns mining

( :require

  [cljs.core.async :refer [chan close! timeout put!]]
  [servant.core :as servant]
  [servant.worker :as worker]
 [pubsub :as ps :refer [sia]]
  )
  (:require-macros
   [app.util :as a :refer [await sweet]]

                  [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]])

  )

;channel that will receive results from mining
(def hashmine (chan))
;setting type on the channel object so it is possible to distinguish it from other channels
(set! (.-type hashmine) "workerch")
(.log js/console "this runs in the browser")
;now to define how much threads will mine
(def worker-count 2)
;mining script path
(def worker-script "wrkr.js")
;this channel is for servant to know at which thread pool to dispatch
(def servant-channel (servant/spawn-servants worker-count worker-script))
;dispatch to thread pool for mining
(defn mine [rootHash]
      (l/og :mine "about to mine %s " rootHash)
      (def chann (servant/servant-thread servant-channel servant/standard-message "none" "newjob" rootHash))

      (go

        (l/og :mine "about to wait for mining to end " )
        (def v (<! chann))
        (l/og :mine "recieved from mining" v)
        (pub "blockMined" (.parse js/JSON v))
        )
      )
