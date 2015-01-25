(ns app.intercom
  (:require
    [app.logger :as l]

    [intercomMake :as im]

    [intercomTake :as it]

    [pubsub :refer [pub sub]]
    [app.blockchain :as blockchain]


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

;every time message arives it is inserte here
(def inputch (chan))
(def outputch (chan))
(def statech (chan))



;this one will send message to peer
(defn sendmsg [peer type msg]

      (l/og :intercom (+ "sending " type) msg)
      (go
        (>! peer (js-obj "type" type "msg" msg))
        ))

;
(defn onMessage [wch type data]
      (l/og :intercom (+ type " message in intercom") data)
      (go
        (>! inputch (js-obj "type" type "data" data "peer" wch))
        ;(>! inputch data)
        )
      )


;switches state of the state machine     
;for protocol check out 
;![](../protocol.png)



(defn tostate [statename]

      (l/og :intercom "changing state to: " statename)
      (go
        (>! statech statename)
        ))
(defn typeof? [message type]
      (== (.-type message) type)
      )
(defn is? [state qstate]
      (== state qstate)
      )


(defn startIntercomLoop [peerchannel]
      (go
        ;(>! statech "start")
        (loop [state "start"]
              (l/og :intercom "starting loop in intercom")
              (def v (<! peerchannel))

              (l/og :intercom "state " state)
              (l/og :intercom "message " v)
              ;this is state machine of protocol described in docs
              ;this should be temporary here
              (cond


                (is? state "start")
                (do
                  (cond
                    (typeof? v "conn") (tostate (it/takeConn  v))
                    true (tostate "grind")
                    )
                  )


                (is? state "version")
                (do
                  (cond
                    (typeof? v "version") (tostate (it/takeVersion  v))
                    true (tostate "grind")
                    )
                  )


                (is? state "grind")
                (do

                  (cond
                    (typeof? v "conn") (tostate (it/takeConn  v))
                    (typeof? v "inv") (tostate (it/takeInv  v))
                    (typeof? v "getdata") (tostate (it/takeGetData  v))
                    (typeof? v "gettx") (tostate (it/takeGetTx  v))
                    (typeof? v "tx") (tostate (it/takeTx v))
                    (typeof? v "data") (tostate (it/takeData v))
                    true (tostate "grind")
                    )
                  )
                )
              (recur (<! statech))
              ))

      )