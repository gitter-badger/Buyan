(ns app.blockchain
(:require
    [app.logger :as l]
    [app.database :as db]
    
    [app.intercom :as i]
    [cljs.core.async :refer [chan close! timeout put!]]
     
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
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

(defn makeBlockHeader [version previous fmroot timestamp bits nonce txcount] 
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
(defn encode [x] (.encode (js/TextEncoder. "utf-8") x))
    (defn arraybtostring [buff]
      (js/arrayBToString buff)
    )
;get previous block
(defn prevblk [blockk]
  (l/og :prevblk "about to get prev blk " blockk)
  (.-previous (.-header blockk))
 )
;blockk parameter can be either block with field hash
;or the hash 
;function first tries to find field hash to query and then uses parameter if field is not there
(defn blockKnown? [blockk]
  (go
  (l/og :blockknown "block known? " blockk)
  (def res (if (.-hash blockk)
    (do
    (if (<! (db/g (.-hash blockk) ))
      true
      false
    ))
    (do
      (if (<! (db/g blockk))
        true
        false
      )
    )
  ))
  res
  )

)
;is this block last in blockchain
(defn last? [blockk]

(go
  (l/og :blockchain "block known? " blockk)
  (def lastt(<! (db/g "last")))

  (def res (if (.-hash blockk)
    (do
    (if (== (.-hash (<! (db/g (.-hash blockk)) )) (.-hash block))
      true
      false
    ))
    (do
      (if (== (.-hash (<! (db/g  blockk) )) (.-hash block))
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
    (if (<  bchainHeight
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
(defn blockchainHeight [x]
(go
  (def hght (<!(db/g "height")) )
  (l/og :blockchainHeight "blockchain height " hght)
  (if x 
       
    (<!(db/update "height" (fn [v] 
    (l/og :height "prev height " v)
    (l/og :height "to add  " x)
        (l/og :height "after addition  " (+ v x))
    (+ v x)

    )))
   
   hght
  )
)
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
  (defn s256 [ k]
     (l/og :blockchain "about to sha256 " k)
     (go
      (let [c (chan)]
       (.then (js/crypto.subtle.digest (js-obj "name" "SHA-256") (encode k) ) #(put! c %))
        
        
        (def r (arraybtostring (<! c)))
(l/og :blockchain (+ "got from sha256 " k) r)
        r
      )
      )
  )
(defn popA [a]
 (.splice a 0 1)
)
    (defn log2 [n]
    (/ (Math/log n) (Math/log 2)))

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
  (loop [  next (.shift  transactions ) cnt (.-length transactions)  txs   transactions ]
  (l/og :blockchain "count  %s" cnt)

    (def tx (<! shaC))
  (sha256c (partial resultToCh shaC) (+ tx next))
  (if (== 0 cnt)
 (go

 )
  (recur  (.shift txs) (.-length txs)   txs )))
  
(def fromC (<! shaC))
(l/og :blockchain "from ch " fromC)
  (set! (.-type fromC) "fmr")

 (>! app.main.cryptoCh (js-obj "value" fromC "type" "fmr"))

    )
)
 ;(def lastt 1)
 ;(l/og :blockchain "got last" lastt)
; lastt