(ns routing

   (:require
    [database :as db]

    [intercomMake :as im]
    [logger :as l]

    [mining :refer [mine]]
    [intercomTake :as it]
    [blockchain :as blockchain]
    [crypt0 :refer [sha256] :as crypto]
    [cljs.core.async :refer [chan close! timeout put!]]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]])
  )

(defn routea [a]

  (go


       (pubsub/rrsa
                  a
                  "dumpdb" #(db/dumpdb)
                  "cleandb" #(db/cleandb)
                  "hash" #(crypto/s256)
                  "blockchainHeight" #(blockchain/blockchainHeight)
                  "prevblk" #(blockchain/prevblk)
                  "makeGetBlock" #(im/makeGetBlock)
                  "g" #(db/g)
                  "p" #(db/p)
                  "addToChain" #(blockchain/addToChain)
                  "handleInvBlock" #(it/handleInvBlock)
                  "log" #(l/og)
                  "mine" #(mining/mine)
                  ))
      (l/og :route "received" a)



  )
