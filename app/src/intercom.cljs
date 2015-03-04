(ns intercom
  (:require

    [pubsub :refer [pub sub]]


    [logger :as l]
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
  (go
      (set! (.-intercomstate conn) state)
  )    )

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
            (def newstate (<! (takeConn message)))
            (l/og :intercom "start -> " newstate)
            (tostate newstate)
               )


          (is? state "version")
               (do  ( cond (typeof? message "version") (tostate (<! (takeVersion message) ))))


          (is? state "grind")
          (do

            (cond
              (typeof? message "conn") (tostate (takeConn message))
              (typeof? message "inv") (tostate (takeInv message))
              (typeof? message "getdata") (tostate (takeGetData message))
              (typeof? message "gettx") (tostate (takeGetTx message))
              (typeof? message "tx") (tostate (takeTx message))
              (typeof? message "data") (tostate (takeData message))
              true (tostate "grind")
              )
            )
          )

        ))

(defn sendm [peer payload]

      (l/og :sendm "sending "  payload)

      (l/og :sendm "peer " peer)
      (go
        (>! peer payload)
        ))
(defn takeInv [fullMessage]
      "function to handle inv block"
      (go
        (def blocks (.-data fullMessage))
        (l/og :takeInv "now about to handle inv block message " blocks)
        (if (<! (blockchain/blockKnown? (blockchain/prevblk (aget (.-vector blocks) 0))))
          ;block known
          (do
            (l/og :inv "block is known ")
            (def bchainHeight (sweet "blockchainHeight"))
            (l/og :inv "blockchainHeight " bchainHeight)
            (def newHeight (+
                             (heightFromBlock (<! (db/g (blockchain/prevblk (aget (.-vector blocks) 0)))))
                             (.-length (.-vector blocks))
                             1
                             ))

            (l/og :inv "newHeight " newHeight)
            ;is blockchains length bigger
            (if (< bchainHeight
                   newHeight
                   )
              (do
                ;validate
                ;now add to chain
                (l/og :inv "now adding to chain")
                (blockchain/addToChain blocks)
                )
              (do
                ;drop inv
                (l/og :inv "about to drop inv")
                )
              )

            )
          ;block unknown
          ;now request previous
          (do
            (l/og :inv "request previous" blocks)
            (def lblock (.-hash  (<! (db/g "last"))))
            (l/og :inv "last block " lblock)
            (>! (.-peer fullMessage) (<! ( makeGetBlock lblock)))
            )
          ;
          )
        "grind"
        )
      )

(defn takeData [message]
      (go
        (l/og :getBlocks "take data " message)
        (do
          (l/og :inv "got inv here ")
          (handleInvBlock (.-data v) v)
          ;(sendmsg (.-peer v) "inv" "0")
          (tostate "grind")
          )(do
             ;(it/makeGetData (.-data v) v)
             ;(sendmsg (.-peer v) "getdata" "0")
             (tostate "grind" handleInvBlock)
             )
        )
      )
(defn broadcast [message]
  (go
   (def connections (c "db" "connections"))


   )
  )
(defn takeGetVersion [message]
      (go
        (do
          (sendmsg (.-peer v) "version" "0")
          (tostate "grind")
          )
        (l/og :getBlocks "take get version " message)
        )
      )
(defn takeVersion [message]
      (go
        (l/og :getBlocks "take version " message)
        ;(do
        ;  (if (== (.-connType (.-data message)) "saltan")
        ;    (do
        ;      (sendm (.-peer message) (makeVersion "0"))
        ;      (tostate "grind"))
        ;    (tostate "version")
        ;    )
        ;
        ;  )
        "grind"
        )
      )

(defn takeConn [conn]

      (go
        (do
          (l/og :takeConn "take conn " conn)
          (sendm (.-writec (.-data conn) ) (makeVersion "0"))
          ;(tostate "version")
          (l/og :takeConn "conn type " (.-connType conn))
          (if (== (.-connType (.-data conn) ) "saltan") "grind" "version")
          )

        ;"version"
        )
      )
(defn takeGetBlocks [peer hash]
      (go
        (l/og :getBlocks "getting data from peer " peer)
        (l/og :getBlocks "getting data from hash " hash)
        (l/og :getBlocks "make Get Blck" (<! (makeGetBlock hash)))
        )
      )

(defn makeInv [typ message]
      ;"this function will make inv message "
      ;(def v (.stringify js/JSON (js-obj "type" "inv" "data" (js-obj "type" typ "vector" message))))
      ;(set! (.-type v) "json")
      (js-obj "type" "json" "data" (.stringify js/JSON (js-obj "type" "inv" "data" (js-obj "type" typ "vector" message))))
      )
(defn makeData [typ message]

      )
(defn makeGetVersion [typ message]

      )
(defn makeVersion [message]

      (js-obj "type" "version" "data" message)
      )
(defn makeConn [conn]
      (l/og :makeConn "make conn " conn)
      (js-obj "type" "conn" "data" conn)
      )
(defn makeGetBlock [hash]

      (l/og :makeGetBlock "about to make block " hash)
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
