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

(defn makeBlock [] (js-obj "" 1))
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
  ;note this is fake merkle root but does the trick 

  (def originl (.-length transactions))
  (def tx (.shift transactions))
  (def next (.shift transactions))
  (sha256c (partial resultToCh shaC) (+ tx next))
  (loop [cnt (.-length transactions)   next (.shift  transactions ) txs   transactions ]
  (l/og :blockchain "count  %s" cnt)
  (go
    (def tx (<! shaC))
    )
  (sha256c (partial resultToCh shaC) (+ tx next))
  (if (== 0 cnt)
  0
  (recur (.-length txs)  (.shift txs)   txs )))

;  (loop [cnt (/ originl 2)   ]
;  (l/og :blockchain "countt %s " cnt )
;  (go
;    (def tx (<! shaC))
;    (def next (<! shaC))
;  );

;  (sha256c (partial resultToCh shaC) (+ tx next))
;  (if (== 1 cnt)
;  0
;  (recur (/ cnt 2) )))


)
; 2^