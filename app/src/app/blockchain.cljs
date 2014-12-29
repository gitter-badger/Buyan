(ns app.blockchain
(:require
    [app.logger :as l]))
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
(defn shaCallb [digest]
    (l/og :blockchain "%s" "blockchain")
  )
(defn sha256 [x] 
    (l/og :blockchain "%s" "sha256")
    (l/og :blockchain "%s" (encode "sha256"))
 (.then (js/crypto.subtle.digest (js-obj "name" "SHA-256") (encode x) )
  shaCallb
 )
)
(defn merkleRoot [transactions] ())