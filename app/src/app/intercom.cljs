(ns app.intercom
  (:require
    [app.logger :as l]
    [app.database :as db]

    [app.blockchain :as blockchain]
    [cljs.core.async :refer [chan close! timeout put!]]

    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]])
  )
(enable-console-print!)
(def intercomState "start")
(def state (atom {}))
;maybe put some dsl in future here that would be agreed in blockchain
;but for now state machine   

;every time message arives it is inserte here
(def inputch (chan))
(def outputch (chan))
(defn makeInv [typ message]
      ;"this function will make inv message "
      ;(def v (.stringify js/JSON (js-obj "type" "inv" "data" (js-obj "type" typ "vector" message))))
      ;(set! (.-type v) "json")
      (js-obj "type" "json" "data" (.stringify js/JSON (js-obj "type" "inv" "data" (js-obj "type" typ "vector" message))))
      )
(def statech (chan))
;this one will send message to peer
(defn sendmsg [peer type msg]

      (l/og :intercom (+ "sending " type) msg)
      (go
        (>! peer (js-obj "type" type "msg" msg))
        ))

;switches state of the state machine     
;for protocol check out 
;![](../protocol.png)

(defn makeGetBlock [hash]
      (go
        (def gtBlock (js-obj "count" 0 "blocks" (array) hash_stop 0))
        ;get block height
        (def heightForBlock (<! (app.blockchain/blockchainHeight 1)))

        (loop [cnt heightForBlock blocksPushed 0]
              (l/og :makeGetBlock (+ "new loop " cnt " ") blocksPushed)
              ;get block with the cnt number
              (def blockg (<! (db/g (+ "b" cnt))))

              (l/og :makeGetBlock "curr block ")
              ;
              (set! (.-count gtBlock) cnt)
              (.push (.-blocks gtBlock) blockg)
              ;for first 10 step is 1
              ; for others step is *2
              (if (< 0 cnt)
                (recur (if (< blocksPushed 10)
                         (- cnt 1)
                         (quot cnt 2)
                         ) (+ blocksPushed 1)
                       ))
              )
        ;return
        gtBlock
        )
      )
(defn getBlocks [peer hash]
      (go
        (l/og :getBlocks "getting data from peer " peer)
        (l/og :getBlocks "getting data from hash " hash)
        (l/og :getBlocks "make Get Blck" (<! (makeGetBlock hash)))
        )
      )
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
                                  (blockchain/handleInvBlock (.-data v) v)
                                  ;(sendmsg (.-peer v) "inv" "0")
                                  (tostate "grind")
                                  )

              (typeof? v "getdata") (do
                                      (sendmsg (.-peer v) "getdata" "0")
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

(defn onMessage [wch type data]
      (l/og :intercom (+ type " message in intercom") data)
      (go
        (>! inputch (js-obj "type" type "data" data "peer" wch))
        ;(>! inputch data)
        )
      )
