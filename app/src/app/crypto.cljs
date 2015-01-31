(ns app.crypto

  (:require

    [app.blockchain :refer [addTransactionToMemPool]]
    [app.database :as db]
    [app.logger :as l]
    [pubsub :refer [pub sub]]
    [cljs.core.async :refer [chan close! timeout put!]])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   )
  )
;(blockchain/blockKnown (js-obj "hash" "asdsad"))
;(blockchain/blockKnown (js-obj "hash" "last"))
(defn bHash [blockHeader]
      (go
        (def stringified (.stringify js/JSON blockHeader))
        (l/og :blockchain "stringified" stringified)
        (def blockHash (<! (blockchain/s256 stringified)))
        blockHash
        )
      )

(defn arraybtostring [buff]
      (js/arrayBToString buff)
      )
(defn encode [x] (.encode (js/TextEncoder. "utf-8") x))
;note this is fake merkle root but does the trick it hashes chain of transactions
;for now it will be here due to simplicity and bcz I could not find math functions in clojurescript easily
;merkleRoot is more efficient at getting any transaction in block because you need to supply only path to the root to verify
;while here it varies depending whether transaction searched is last one or first one
;will save mempool here so we can retrieve them later when merkle root is ready and when block structure is to be made

(defn merkleRoot [transactions]

      (db/update "txs" #(do transactions))
      (go
        (def originl (.-length transactions))
        (def tx (.shift transactions))
        (def next (.shift transactions))
        (sha256c (partial resultToCh shaC) (+ tx next))
        (loop [next (.shift transactions) cnt (.-length transactions) txs transactions]
              (l/og :merkleRoot "count  %s" cnt)

              (def tx (<! shaC))
              (sha256c (partial resultToCh shaC) (+ tx next))
              (if (== 0 cnt)
                (go

                  )
                (recur (.shift txs) (.-length txs) txs)))

        (def fromC (<! shaC))
        (l/og :merkleRoot "from ch " fromC)
        (set! (.-type fromC) "fmr")

        (>! app.main.cryptoCh (js-obj "value" fromC "type" "fmr"))

        )
      )
(def shaC (chan))
(defn resultToCh [chan digest]
      (def m (arraybtostring digest))
      (l/og :blockchain "to channel: %s" m)
      (go (>! chan m))
      )
(defn sha256 [x]
      (l/og :sha256 "sha256 %s" x)
      ;(l/og :blockchain "%s" (encode "sha256"))
      (.then (js/crypto.subtle.digest (js-obj "name" "SHA-256") (encode x))
             shaCallb
             )
      )
(defn sha256c [c x]
      ;(l/og :blockchain "%s" "sha256")
      ;(l/og :blockchain "%s" (encode "sha256"))
      (.then (js/crypto.subtle.digest (js-obj "name" "SHA-256") (encode x))
             c
             )
      )
(defn s256 [k]
      (l/og :blockchain "about to sha256 " k)
      (go
        (let [c (chan)]
             (.then (js/crypto.subtle.digest (js-obj "name" "SHA-256") (encode k)) #(put! c %))


             (def r (arraybtostring (<! c)))
             (l/og :s256 (+ "got from sha256 " k) r)
             r
             )
        )
      )
(defn shaCallb [digest] (do
                          (l/og :shaCallB "%s" "about to do hash2")
                          (def h (arraybtostring digest))
                          (l/og :shaCallBh)
                          (addTransactionToMemPool h)
                          (pub "crypto" h)
                          ;(go
                          ;  (>! app.main.cryptoCh h)
                          ;  )

                          ))