(ns app.blockchain
(:require
    [app.logger :as l]
    [cljs.core.async :refer [chan close! timeout put!]]
    
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   )
    )
(enable-console-print!)
(def memPool (array))
(defn addTransactionToMemPool [x] 
(.push memPool x)
(l/og :blockchain "new memPool after adding " memPool))
(defn removeTransactionFromMemPool [x] (.splice memPool (.indexOf memPool x))

 (l/og :blockchain "new memPool after remove " memPool)
)

(defn makeBlock [version previous fmroot timestamp bits nonce txcount] 
  (js-obj 
    "version" 1
    "previous" previous
    "merkleRoot" fmroot
    "timestamp" timestamp
    "bits" bits
    "nonce" nonce
    "txcount" txcount       
))
(defn makeTransaction [] (js-obj "" 1))
(defn addTransactionToBlock [] (js-obj "" 1))
(defn encode [x] (.encode (js/TextEncoder. "utf-8") x))
    (defn arraybtostring [buff]
      (js/arrayBToString buff)
      
    )
(defn shaCallb [digest] (do
(l/og :blockchain "%s"  "about to do hash2")
    (def h (arraybtostring digest))
      (l/og :blockchain  h )
   (addTransactionToMemPool h)
   (go
   (>! app.main.cryptoCh h )
  )))
  (def shaC (chan))
(defn resultToCh [ chan digest]
  (def m (arraybtostring digest))
  (l/og :blockchain "to channel: %s" m)
 (go (>! chan m))
)
(defn sha256 [x] 
    ;(l/og :blockchain "%s" "sha256")
    ;(l/og :blockchain "%s" (encode "sha256"))
 (.then (js/crypto.subtle.digest (js-obj "name" "SHA-256") (encode x) )
  shaCallb
 )
)
(defn sha256c [c x] 
    ;(l/og :blockchain "%s" "sha256")
    ;(l/og :blockchain "%s" (encode "sha256"))
 (.then (js/crypto.subtle.digest (js-obj "name" "SHA-256") (encode x) )
  c
 )
)
(defn popA [a]
 (.splice a 0 1)
)
    (defn log2 [n]
    (/ (Math/log n) (Math/log 2)))
(defn merkleRoot [transactions]  
  ;note this is fake merkle root but does the trick it hashes chain of transactions 
  ;for now it will be here due to simplicity and bcz I could not find math functions in clojurescript easily
  ;merkleRoot is more efficient at getting any transaction in block because you need to supply only path to the root to verify
  ;while here it varies depending whether transaction searched is last one or first one
  (def lastt (js/Object.))
  (go
  (def originl (.-length transactions))
  (def tx (.shift transactions))
  (def next (.shift transactions))
  (sha256c (partial resultToCh shaC) (+ tx next))
  (loop [  next (.shift  transactions ) cnt (.-length transactions)  txs   transactions ]
  (l/og :blockchain "count  %s" cnt)

    (def tx (<! shaC))
  (sha256c (partial resultToCh shaC) (+ tx next))
  (if (== 0 cnt)
 (go
 ;(def lastt 1)
 ;(l/og :blockchain "got last" lastt)
; lastt
 )
  (recur  (.shift txs) (.-length txs)   txs )))
  
(def fromC (<! shaC))
(l/og :blockchain "from ch " fromC)
  (set! (.-type fromC) "fmr")

 (>! app.main.cryptoCh (js-obj "value" fromC "type" "fmr")) 

    )
)
