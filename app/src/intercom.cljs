(ns intercom
  (:require

    [pubsub :refer [pub sub]]


    [cljs.core.async :refer [chan close! timeout put!]]

    )
  (:require-macros
   [util :as a :refer [await sweet]]

   [cljs.core.async.macros :as m :refer [go]]
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

(defn setIntercomState [conn state]
      (set! (.-intercomstate conn) state)
      )

(defn getIntercomState [conn]
      (l/og :getIntercomState "intercom state" conn)
      (aget conn "intercomstate")
      )

(defn tostateu [conn statename]

      (l/og :intercom "changing state to: " statename)
      (aset conn "intercomstate" statename)
      )
(defn typeof? [message type]
      (== (.-type message) type)
      )
(defn is? [state qstate]
      (== state qstate)
      )


(defn intercomstatemachine [conn message]
      (go
        (def tostate (partial tostateu conn))
        (def state (getIntercomState conn))
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
          (do
            (def newstate (<! (it/takeConn message)))
            (l/og :intercom "start -> " newstate)
            (tostate newstate)
               )


          (is? state "version")
               (do  ( cond (typeof? message "version") (tostate (<! (it/takeVersion message) ))))


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

