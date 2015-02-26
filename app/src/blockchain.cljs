(ns blockchain
  (:require
    [pubsub :as ps ]

    [logger :as l]
    [cljs.core.async :refer [chan close! timeout put!]]

    )
  (:require-macros
    [cljs.core.async.macros :as m :refer [go]]
    [util :as a :refer [await sweet c debug]]

                   )
  )
(enable-console-print!)

(def memPool (array))
(def blockhainInfo (js-obj))
(set! (.-dificulty blockhainInfo) 5)
(defn addTransactionToMemPool [x]
      (.push memPool x)
      (l/og :blockchain "new memPool after adding " memPool))
(defn removeTransactionFromMemPool [x] (.splice memPool (.indexOf memPool x))

      (l/og :blockchain "new memPool after remove " memPool)
      )

(defn makeBlockHeader1 [version previous fmroot timestamp bits nonce txcount]
      (js-obj
        "version" 1
        "heightFromRoot" 0
        "previous" previous
        "merkleRoot" fmroot
        "timestamp" timestamp
        "bits" bits
        "nonce" nonce
        "txcount" txcount
        ))

(defn makeTransaction [] (js-obj "" 1))
(defn addTransactionToBlock [] (js-obj "" 1))
;get previous block
(defn prevblk [blockk]
      (c "log" :prevblk "about to get prev blk " blockk)
      (.-previous (.-header blockk))
      )
;blockk parameter can be either block with field hash
;or the hash
;function first tries to find field hash to query and then uses parameter if field is not there
(defn blockKnown? [blockk]
      (go
        (if blockk (do
        (l/og :blockknown "block known? " blockk)
        (def res (if (.-hash blockk)
                   (do
                     (if (c "g" (.-hash blockk))
                       true
                       false
                       ))
                   (do
                     (if (c "g"  blockk)
                       true
                       false
                       )
                     )
                   ))
        res
        ) false
                   ))

      )
;is this block last in blockchain
(defn last? [blockk]

      (go
        (l/og :blockchain "block known? " blockk)
        (def lastt (c "g" "last"))

        (def res (if (.-hash blockk)
                   (do
                     (if (== (.-hash (c "g"  (.-hash blockk))) (.-hash blockk))
                       true
                       false
                       ))
                   (do
                     (if (== (.-hash (c "g"  blockk)) (.-hash blockk))
                       true
                       false
                       )
                     )
                   ))
        res
        )

      )
(defn heightFromBlock [blockk]
      (l/og :heightFromBlock "getting height from block " blockk)
      (.-heightFromRoot (.-header blockk))
      )

(defn makeBlock [work]
      (debug :makeBlock "about to make block " work)
      (go
        ;(def txs (<! (db/g "txs")))
        (def lastt2 (c "g" "last"))

        (debug :makeBlock "last " lastt2)
        (def transactions (c "g" "txs"))
        (def lastv  lastt2)
        ;(if  lastt2 (do
        ;             (def lastv (.-val lastt2))
        ;
        ;             ) (do
        ;                 (def lastv (js-obj "hash" 0))
        ;                 (def transactions (array))
        ;
        ;                 ))
        (debug  :makeBlock "last " lastv)
        ;version previous fmroot timestamp bits nonce txcount
        (def blockHeader (makeBlockHeader1 "0" (.-hash lastv) (.-root work) (.getTime (js/Date.)) (.-dificulty blockhainInfo) (.-nonce work) (.-lenght transactions)))
        (debug :makeBlock "block header " blockHeader)
        (debug :makeBlock "transactions when saving block " transactions)
        (def blockk (js-obj "header" blockHeader "hash" (c "hash" blockHeader) "transactions" transactions))
        (debug :makeBlock "newly made block " blockk)
        blockk
        )
      )
(defn saveBlock [ blockR]
      (go
        (l/og :saveBlock "saving " blockR)
        (def heightForBlock (c "blockchainHeight" 1))
        (set! (.-heightFromRoot (.-header blockR)) heightForBlock)
        ;(db/update "last" #(js-obj "_id" "last" "val" blockR))
        (defn a [] blockR)
        (c "db" "last" (.-hash blockR) blockR )

        ;todo save other info also
        ;(.put dbase (js-obj "_id" (.-hash blockR) "val" blockR))
        ;(.put dbase (js-obj "_id" (.-hash blockR) "val" blockR))
        (c "db" (.-hash blockR) blockR)
        (c "db" (+ "b" heightForBlock) blockR)
        ))

;this is the pseudo code
;
;      is the previous block of first block in array known?
;         if it is then check if the length of the new blockchain is bigger than what we have
;             if it is bigger make new chain since it will be bigger
;             if it is not bigger drop message and send data that we have since we have bigger chain
;         if it is unknown
;           send request for more data
;

(defn addToChain [schain]
      (go
        (l/og :inv "about to add to chain")

        (loop [i 0]
              (l/og :inv "itterating current step " i)
              (if (<! (blockKnown? (prevblk (aget schain i))))
                (l/og :inv "block is known " i)
                (do
                  (l/og :inv "block is not known " i)
                  (saveBlock i)
                  )
                )
              (if (< i (.-length schain))
                (recur (+ i 1))
                )
              )
        (db/update "last" (last schain))
        )
      )

(defn blockchainHeight [x]
      (go
        (def hght (c "db" "height"))
        (l/og :blockchainHeight "blockchain height " hght)
        (if x

          (c "db" "height" (+ hght 1))

          hght
          )
        )
      )


(defn popA [a]
      (.splice a 0 1)
      )
(defn log2 [n]
      (/ (Math/log n) (Math/log 2)))

;(def lastt 1)
;(l/og :blockchain "got last" lastt)
; lastt
