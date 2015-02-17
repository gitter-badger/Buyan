(ns router

   (:require
    [intercom :as i]
    [intercomMake :as im]
    [database :as db]
    [logger :as l]
    [blockchain :as b]
    [crypt0 :refer [sha256] :as crypto]
    [peerjs :refer [peerjs peerParams]]
    [mining :refer [mine]]


    [pubsub :refer [pub sub]]
    [cljs.core.async :refer [chan close! timeout put!]]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]])
  )

(defn a[x ]
  (js-obj "a" 1)
  )
(defn route []

  (go

(loop []
      (def a (<! (pubsub/rrs
                  "dumpdb" #(db/dumpdb)
                  "cleandb" #(db/cleandb)
                  "hash" #(crypto/s256)
                  )))
      (l/og :route "received" a)

        (recur)))

  )
(defn routea [a]

  (go


      (def a (<! (pubsub/rrsa
                  a
                  "dumpdb" #(db/dumpdb)
                  "cleandb" #(db/cleandb)
                  "hash" #(crypto/s256)
                  )))
      (l/og :route "received" a)

        (recur))

  )
