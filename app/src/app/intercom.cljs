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


(defn intercomstatemachine [state message]
      (go
        ;(>! statech "start")

        (l/og :intercom "starting loop in intercom")
        (l/og :intercom "state in intercom " state)
        (l/og :intercom "message in intercom " message)
        (def v (.-data message))

        (l/og :intercom "state " state)
        (l/og :intercom "message " message)
        ;this is state machine of protocol described in docs
        ;this should be temporary here
        (cond


          (is? state "start")
               (tostate (<! (it/takeConn message)))



          (is? state "version")
                (typeof? message "version") (tostate (it/takeVersion message))


          (is? state "grind")
          (do

            (cond
              (typeof? message "conn") (tostate (it/takeConn message))
              (typeof? message "inv") (tostate (it/takeInv message))
              (typeof? message "getdata") (tostate (it/takeGetData message))
              (typeof? message "gettx") (tostate (it/takeGetTx message))
              (typeof? message "tx") (tostate (it/takeTx message))
              (typeof? message "data") (tostate (it/takeData message))
              true (tostate "grind")
              )
            )
          )

        ))

