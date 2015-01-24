(ns app.intercom
  (:require
    [app.logger :as l]

    [intercomMake :as im]

    [intercomTake :as it]

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

(l/og :intercom "about loop in intercom")
(defn lp []
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
                  (l/og :intercom "got " v)
                  (cond
                    (typeof? v "conn") (do
                                         (if (== (.-connType (.-data v)) "saltan")
                                           (do
                                             (sendmsg (.-peer v) "version" "0")
                                             (tostate "grind"))
                                           (tostate "version")
                                           )

                                         )
                    )
                  )
                (is? state "version")
                (do
                  (cond
                    (typeof? v "version") (do
                                            (sendmsg (.-peer v) "version" "0")
                                            (tostate "grind")
                                            )
                    )
                  )
                (is? state "grind")
                (do

                  (cond
                    (typeof? v "conn") (do
                                         (sendmsg (.-peer v) "version" "0")
                                         (tostate "version")
                                         )
                    (typeof? v "inv") (do
                                        (l/og :inv "got inv here ")
                                        (it/handleInvBlock (.-data v) v)
                                        ;(sendmsg (.-peer v) "inv" "0")
                                        (tostate "grind")
                                        )

                    (typeof? v "getdata") (do
                                            ;(it/makeGetData (.-data v) v)
                                            ;(sendmsg (.-peer v) "getdata" "0")
                                            (tostate "grind" handleInvBlock)
                                            )
                    (typeof? v "gettx") (do
                                          (sendmsg (.-peer v) "gettx" "0")
                                          (tostate "grind")
                                          )
                    (typeof? v "tx") (do
                                       (sendmsg (.-peer v) "tx" "0")
                                       (tostate "grind")
                                       )

                    (typeof? v "data") (do
                                         (sendmsg (.-peer v) "data" "0")
                                         (tostate "grind")
                                         )
                    true
                    (tostate "grind")

                    )
                  )
                )
              (recur (<! statech))
              ))

      )