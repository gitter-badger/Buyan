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
;but for now state machine
;(defprotocol versionExchange)
(def inputch (chan))
(def outputch (chan))
(defn makeInv [typ message]
  (.stringify js/JSON (js-obj "type" "inv" "data" (js-obj "type" typ "vector" message)))
)
(def statech (chan))
(defn sendmsg [peer type msg]

  (l/og :intercom  (+ "sending " type) msg)
(go
  (>! peer (js-obj "type" type "msg" msg))
))
(defn tostate [statename]

  (l/og :intercom  "changing state to: " statename)
(go
  (>! statech statename)
))
(defn typeof? [message type]
  (== (.-type message) type)
)
(defn is? [state qstate]
  (== state qstate)
)

        (l/og :intercom "about loop in intercom")
(go 
  ;(>! statech "start")
 (loop [state "start"]
        (l/og :intercom "starting loop in intercom")
        (def v (<! inputch))
        
        ;this is state machine of protocol described in docs 
        ;this should be temporary here 
        (cond 
            (is? state "start") 
                (do
                  (l/og :intercom "state " "start") 
                  (l/og :intercom  "got " v)
                  (cond
                    (typeof? v "conn")(do
                      (if (== (.-connType (.-data v)) "saltan")
                      (sendmsg (.-peer v) "version" "0"))
                      (tostate "version")
                      

                    )
                  )
                )
            (is? state "version") 
                (do
                  (cond
                    (typeof? v "version")(do
                      (sendmsg (.-peer v) "version" "0")
                      (tostate "grind")
                    )
                  )
            )
            (is? state "grind") 
                (do
                  
                  (cond
                    (typeof? v "conn")(do
                      (sendmsg (.-peer v) "version" "0")
                      (tostate "version")
                    )
                    (typeof? v "inv")(do
                      (sendmsg (.-peer v) "inv" "0")
                      (tostate "grind")
                    )
                    (typeof? v "getdata")(do
                      (sendmsg (.-peer v) "getdata" "0")
                      (tostate "grind")
                    )
                    (typeof? v "gettx")(do
                      (sendmsg (.-peer v) "gettx" "0")
                      (tostate "grind")
                    )
                    (typeof? v "tx")(do
                      (sendmsg (.-peer v) "tx" "0")
                      (tostate "grind")
                    )

                    (typeof? v "data")(do
                      (sendmsg (.-peer v) "data" "0")
                      (tostate "grind")
                    )
                  )
                )
        )
    (recur (<! statech))
))

(defn onMessage [wch type data] 
  (l/og :intercom (+ type " message in intercom") data )
 (go
  (>! inputch (js-obj "type" type "data" data "peer" wch))
  ;(>! inputch data)
  )
)
