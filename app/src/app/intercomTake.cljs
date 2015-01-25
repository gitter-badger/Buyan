(ns intercomTake
  (:require
    [app.logger :as l]
    [app.database :as db]

    [pubsub :refer [pub sub]]
    [app.blockchain :as blockchain]

    )

  )

(defn handleInvBlock [blocks fullMessage]
      "function to handle inv block"
      (go

        (l/og :inv "now about to handle inv block message " blocks)
        (if (<! (blockKnown? (prevblk (aget (.-vector blocks) 0))))
          ;block known
          (do
            (def bchainHeight (<! (blockchainHeight)))
            (def newHeight (+
                             (heightFromBlock (<! (db/g (prevblk (aget (.-vector blocks) 0)))))
                             (.-length (.-vector blocks))
                             1
                             ))
            (l/og :inv "blockchainHeight " bchainHeight)
            (l/og :inv "newHeight " newHeight)
            ;is blockchains length bigger
            (l/og :inv "block is known ")
            (if (< bchainHeight
                   newHeight
                   )
              (do
                ;validate
                ;now add to chain
                (l/og :inv "now adding to chain")
                (addToChain blocks)
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
            (i/getBlocks (.-peer fullMessage) (.-hash (<! (db/g "last"))))
            )
          ;
          )
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
        (do
          (if (== (.-connType (.-data message)) "saltan")
            (do
              (sendmsg (.-peer message) "version" "0")
              (tostate "grind"))
            (tostate "version")
            )

          )

        )
      )

(defn takeConn [message]

      (go
        (do
          (sendmsg (.-peer v) "version" "0")
          (tostate "version")
          )
        (l/og :getBlocks "take conn " message)
        )
      )
(defn takeGetBlocks [peer hash]
      (go
        (l/og :getBlocks "getting data from peer " peer)
        (l/og :getBlocks "getting data from hash " hash)
        (l/og :getBlocks "make Get Blck" (<! (makeGetBlock hash)))
        )
      )

