(ns intercomTake
  (:require

 [pubsub :as ps :refer [sia]]
    [cljs.core.async :refer [chan close! timeout put!]]
    )
  (:require-macros
   [app.util :as a :refer [await sweet]]

   [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]])
  )
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
            (>! (.-peer fullMessage) (<! ( im/makeGetBlock lblock)))
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
          (it/handleInvBlock (.-data v) v)
          ;(sendmsg (.-peer v) "inv" "0")
          (tostate "grind")
          )(do
             ;(it/makeGetData (.-data v) v)
             ;(sendmsg (.-peer v) "getdata" "0")
             (tostate "grind" handleInvBlock)
             )
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
        ;      (sendm (.-peer message) (im/makeVersion "0"))
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
          (sendm (.-writec (.-data conn) ) (im/makeVersion "0"))
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

