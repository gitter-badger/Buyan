(ns app.intercom
(:require
    [app.logger :as l]
    [app.database :as db]
    [cljs.core.async :refer [chan close! timeout put!]]
    
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   )
    )
(enable-console-print!)
(def intercomState "start")
(def state (atom {}))
;maybe put some dsl in future here that would be agreed in blockchain
;(defprotocol versionExchange)
(def inputch (chan))
(def outputch (chan))

(def statech (chan))
(go  (loop [state (<! statech)]
        
        (def v (<! inputch))
        ;get value
        (def vrecieved (nth v 0))
        ;get channel that received value
        (def ch2 (nth v 1))
        (cond 
            (== (.-type input) "writech") 
                (do
                  ; println vrecieved
                  (l/og :mloop  "sending to peer " vrecieved)
                  (l/og :mloop  (.-conn ch2))
                )
        )
    (recur (<! statech))
))

(defn onMessage [type data] (l/og :intercom "message %s in intercom" data ))
