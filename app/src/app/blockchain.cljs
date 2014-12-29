(ns app.blockchain
(:require
    [app.logger :as l]))
(enable-console-print!)
(defn makeBlock [] (js-obj "" 1))
(defn makeTransaction [] (js-obj "" 1))
(defn addTransactionToBlock [] (js-obj "" 1))
(defn encode [x] (.encode (js/TextEncoder. "utf-8") x))
(defn sha256 [x] 
    (l/og :blockchain "%s" "sha256")
 (.then (js/crypto.subtle.digest (js-obj name "SHA-256") (encode x) )
  (fn [digest]
    (l/og :blockchain "blockchain")
  )
 )
)
(defn merkleRoot [transactions] ())