 (ns crypto)
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