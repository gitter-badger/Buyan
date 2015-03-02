(ns routing

   (:require
    [mockdatabasew :as db]

    [intercomMake :as intercomMake]
    [logger :as l]

    [mining   ]
    [intercomTake :as it]
    [blockchain :as blockchain]
    [communications]
    [display.renderStrategy ]
    [crypt :as crypto]
    [cljs.core.async :refer [chan close! timeout put!]]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]])
  )

(defn routea [f rch sch ]

       (f
                  rch
                  sch
        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  "cleandb" db/cleandb
                  "connectTo" communications/connectTo
                  "dumpdb" db/dumpdb
                  ;"g" db/g
                  "g" db/reg
                  "db" db/reg
                  "initdb" db/initDBase
                  "onDatabaseChange" db/onDatabaseChange
                  "p" db/reg
                  ;"p" db/p
                  "s" db/s
                  "update" db/update
                  "database" db/reg



        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  "ui" display.renderStrategy/run


        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

                  "addToChain" blockchain/addToChain
                  "addTransactionToBlock" blockchain/addTransactionToBlock
                  "blockKnown?" blockchain/blockKnown?
                  "blockchainHeight" blockchain/blockchainHeight
                  "heightFromBlock" blockchain/heightFromBlock
                  "last?" blockchain/last?
                  "log2" blockchain/log2
                  "makeBlock" blockchain/makeBlock
                  "makeBlockHeader" blockchain/makeBlockHeader
                  "makeTransaction" blockchain/makeTransaction
                  "popA" blockchain/popA
                  "prevblk" blockchain/prevblk
                  "removeTransactionFromMemPool" blockchain/removeTransactionFromMemPool
                  "saveBlock" blockchain/saveBlock

        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


                  "broadcastNewBlock" communications/broadcastNewBlock
                  "channelsFromConnection" communications/channelsFromConnection
                  "cryptoCh" communications/cryptoCh
                  "initial" communications/initial
                  "intercomMeta" communications/intercomMeta
                  "onBlockMined" communications/onBlockMined
                  "onConnection" communications/onConnection
                  "onCrypto" communications/onCrypto
                  "onData" communications/onData
                  "onNewConnection" communications/onNewConnection
                  "onOpen" communications/onOpen
                  "onTransaction" communications/onTransaction
                  "sendmsg" communications/sendmsg
                  "setID" communications/setID
                  "setupComm" communications/setupComm
                  "startP2PCommLoop" communications/startP2PCommLoop

        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  "arraybtostring" crypto/arraybtostring
                  "bHash" crypto/bHash
                  "encode" crypto/encode
                  "hash" crypto/s256
                  "merkleRoot" crypto/merkleRoot
                  "resultToCh" crypto/resultToCh
                  "s256" crypto/s256
                  "sha256" crypto/sha256
                  "sha256c" crypto/sha256c
                  "shaCallb" crypto/shaCallb

        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  "getIntercomState" intercom/getIntercomState
                  "intercomstatemachine" intercom/intercomstatemachine
                  "is?" intercom/is?
                  "onMessage" intercom/onMessage
                  "setIntercomState" intercom/setIntercomState
                  "tostateu" intercom/tostateu
                  "typeof?" intercom/typeof?

        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

                  "makeConn" intercomMake/makeConn
                  "makeData" intercomMake/makeData
                  "makeGetBlock" intercomMake/makeGetBlock
                  "makeInv" intercomMake/makeGetVersion
                  "makeInv" intercomMake/makeInv
                  "makeVersion" intercomMake/makeVersion
        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  "sendm" intercomTake/sendm
                  "takeConn" intercomTake/takeConn
                  "takeData" intercomTake/takeData
                  "takeGetBlocks" intercomTake/takeGetBlocks
                  "takeGetVersion" intercomTake/takeGetVersion
                  "takeInv" intercomTake/takeInv
        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  "log" l/og
        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  "mine" mining/mine
        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                  "mg" mockdatabase/g
                  "mp" mockdatabase/p
                  "mps" mockdatabase/ps
                  "mupdate" mockdatabase/update
        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

                  "mcleandb" mockdatabasew/cleandb
                  "mdumpdb" mockdatabasew/dumpdb
                  "mg" mockdatabasew/g
                  "minitDBase" mockdatabasew/initDBase
                  "mp" mockdatabasew/p
                  "mps" mockdatabasew/ps
                  "mupdate" mockdatabasew/update
        ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

                  ))
