(ns router

   (:require
    [intercom :as i]
    [intercomMake :as im]
    [database :as db]
    [logger :as l]
    [blockchain :as b]
    [crypto :refer [sha256]]
    [peerjs :refer [peerjs peerParams]]
    [mining :refer [mine]]


    [pubsub :refer [pub sub]]
    [cljs.core.async :refer [chan close! timeout put!]]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]])
  )

(defn route []
  (go (loop []


      (def a (<! (pubsub/rrs
                  "dumpdb" #(db/dumpdb)
                  "cleandb" #(db/cleandb)

                  )))
      (l/og :route "received" a)

  ))

  )
