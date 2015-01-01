(ns app.main
  (:require
    [app.intercom :as i]
    [app.logger :as l]
    [app.database :as db]
    [app.blockchain :as blockchain]
    [cljs.core.async :refer [chan close! timeout put!]]
    [servant.core :as servant]
    [servant.worker :as worker])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]]) )


(enable-console-print!)


(def peers [])
(defn foo [] 
  (println "window loaded")

  ;(blockchain/sha256 "Nikola")
  (.log js/console "this runs in the browser")
  ;now to define how much threads will mine
  (def worker-count 2)
  ;mining script path
  (def worker-script "wrkr.js")
  ;this channel is for servant to know at which thread pool to dispatch
  (def servant-channel (servant/spawn-servants worker-count worker-script))

  ;channel that will receive results from mining
  (def hashmine (chan))
  ;setting type on the channel object so it is possible to distinguish it from other channels
  (set! (.-type hashmine) "workerch")
  ;instantiate tree js graphic lib
  (. js/console (log (THREE/Scene. )))
  ;data for peer connection 
  (def ^:dynamic peerParams (js-obj "host" "localhost" "port" 8000 "key" "peerjs" "debug" true))
  ;promt user for id that will be used as his peer id
  (def id ( js/prompt "enter id"))
  (l/og :main "user id %s "id)

  (def start (chan))
  ;channel to anounce new connectinos
  (def connectionch (chan))
  ;peerjs object
  (def peerjs (js/Peer. id peerParams ))

  (def onDatabaseChange (chan))
  (set! (.-type onDatabaseChange) "databaseChange")

  ;database instance
  (def dbase (js/PouchDB. "dbname"))
  (.enable (.-debug js/PouchDB) "*")

  (defn saveBlock [dbase blockR] 
    (l/og :dbase "saving " blockR)
    (.put dbase (js-obj "_id" "last" "val" blockR))
    ;todo save other info also
    (.put dbase (js-obj "_id" (.-hash blockR) "val" blockR))
    )
  ;initial function for db
  (defn initDBase [dbase]

    (let [c (chan)]
      (go 
        (.then (.get dbase "last") #(put! c %) #(put! c %))

        (def lastone (<! c))

        (l/og :db "about to init")
        (l/og :db "last one from database " lastone)
        (if (== (.-status lastone) 404)(do 
                                         (l/og :db "nothing in database")
                                         (def blck (<! (makeBlock (js-obj "root" "0" "nonce" "0"))))
                                         ;args to make blockheader version previous fmroot timestamp bits nonce txcount
                                         ;(def blockR (app.blockchain.makeBlockHeader "0" "0" "0" (.getTime ( js/Date.)) 0 "0" 0))
                                         ;(def stringified (.stringify js/JSON blockR))
                                         ;(l/og :blockchain "stringified initial" stringified)
                                         ;(db/p   (<! (blockchain/s256 stringified)) [])
                                         (saveBlock dbase blck)
                                         )
          (do
            (l/og :db "last one from database " lastone)

            )
          )
        ;(if last)
        ;(.put dbase (js-obj "_id" "height" "val" 1))
        )  )
    )    
  (initDBase dbase)
  (defn bHash [block]
    (go
      (def stringified (.stringify js/JSON (.-val lastt)))
      (l/og :blockchain "stringified" stringified)
      (def blockHash (<! (blockchain/s256 stringified)))
      blockHash
    )
  )
  (defn makeBlock [work]
    (go
      (def txs (<! (db/g "txs")))
      (def lastt (<! (db/g "last")))

      (def transactions (<! (db/g "txs")))
      ;version previous fmroot timestamp bits nonce txcount
      (def blockHeader (app.blockchain.makeBlockHeader "0" (.-hash lastt) (.-root work) (.getTime ( js/Date.)) (.-dificulty blockchain/blockhainInfo) (.-nonce work) (.-lenght transactions )))
      (l/og :db "block header " blockHeader)
      (l/og :db "transactions when saving block " transactions)
      (def blockk (js-obj "header" blockHeader "hash" (<! (bHash blockHeader)) "transactions" transactions))
      (l/og :db "newly made block " blockk)
      blockk
      )
    )
  ; (go
  ; (l/og :db "last entry with new func" (<! (gdb "last")) )
  ; )
  ;channel to recieve new transaction
  (def transactionch (chan))
  (set! (.-type transactionch) "transactionch")

  ;channel to recieve results from crypto functions
  (def cryptoCh (chan))
  (set! (.-type cryptoCh) "cryptoch")

  ;pub sub; send events to channel
  (defn pub [ch event message] (go (>! ch message)))  
  ;listen on global document for transactions and publish them to channel transactionch
  (.on (js/$ js/document) "transaction" (partial pub transactionch))

  ;when this user connects to someone just send connection to channel
  (defn onOpen [ conn]
    (l/og :conn  "connection opened trying to send data trough")
    (l/og :conn  conn)
    (set! (.-connType conn) "saltan")
    (.send conn "asd")
    (go
      (>! connectionch conn)
      )

    (l/og :conn "conn: " conn)
    )
;when peerjs sends data just send message to channel
(defn onData [read data]
  (l/og :conn "data recieved" data)
  (go
    (>! read data)
    )

  )
;when someone connects to this user send that new connection to channel
(defn onConnection [conn]
  (l/og :conn "connection is opened now try to send something")
  (set! (.-connType conn) "tsaritsa")
  ;(.send conn "second sends something")
  (go
    (>! connectionch conn)
    )
  )

(.on peerjs "connection" onConnection )
;make two channels for connection. one for reading from conn, one for writing to it
(defn channelsFromConnection [conn]
  (def readc (chan 10))
  (def writec (chan 10))
  (set! (.-writec readc) writec)
  (set! (.-type readc) "readch")
  (set! (.-type writec) "writech")
  (set! (.-writec  conn) writec)
  (set! (.-readc  conn) readc)

  (set! (.-conn readc) conn)

  (set! (.-conn writec) conn)
  (.on conn "data" (partial onData readc))
  [readc writec]
  )
(defn connectTo [id]
  (let [conn (.connect peerjs id)]
    (.on conn "open" (partial onOpen conn))

    ;(channelsFromConnection conn)

    ))
(def empty-string "")

(println id)
;(if (== id "2")
; (println "id = 2"))
;keeps track of protocol and peers
(def intercomMeta (js-obj 
                    "id" 1
                    "knownPeers" []
                    ))

;dispatch to thread pool for mining
(defn mine [rootHash]
  (def chann (servant/servant-thread servant-channel servant/standard-message "none" "newjob" rootHash ))

  (go

    (def v (<! chann))
    (l/og :mloop "recieved from mining" v)
    (>! hashmine (.parse js/JSON v) )
    )
  )
;now that channels are setup
(do
  (l/og :conn "about to connect from heere")
  (def peer (connectTo "2"))
  ;(.log js/console (nth peer 1))


  (defn lp [statea] 
    (def gconn 1)
    (def state statea)
    ;listen on messages and send them where they need to be sent
    (go (loop [state2 statea]
          ;(>! (nth peer 1) "sending some data trough channel")
          (l/og :mloop "new iteration with state")

          (l/og :mloop state)
          ;listen on channels from vector
          (def v (alts! state ))
          ;get value
          (def vrecieved (nth v 0))
          ;get channel that received value
          (def ch2 (nth v 1))

          (cond 
            ;channel that gets new connections
            (== (nth v 1) connectionch) (do
                                          (def gconn vrecieved)

                                          (l/og :mloop "got new connection" vrecieved)
                                          (def peerChannels  (channelsFromConnection vrecieved) )
                                          ;add channels for reading and writing to this new connection to the vector of channels we listen
                                          (def state (into [] (concat state  peerChannels )))
                                          (set! (.-knownPeers intercomMeta) (conj (.-knownPeers intercomMeta)  (.-peer vrecieved)) )
                                          (l/og :mloop  "new state")
                                          (i/onMessage "version" "message")

                                          (l/og :mloop  state)
                                          ;if this user initiated connection he will send version first
                                          (if (== (.-connType vrecieved) "saltan")
                                            (do 
                                              (l/og :mloop  "saltan here")
                                              (>! (nth peerChannels 1) (js-obj "type" "versionSaltan" "data" (.-id intercomMeta)))
                                              )
                                            (do 

                                              (l/og :mloop  "tsaritsa here")
                                              )
                                            )
                                          )
            ;channel from some peer that recieves data from peer
            (== (.-type ch2) "readch") (do 
                                         (l/og :mloop  "recieved from peer " vrecieved)
                                         (if (== (.-type vrecieved) "versionSaltan") 
                                           (do
                                             (l/og :mloop  (.-data vrecieved))
                                             (l/og :mloop  "-------------------------")
                                             (l/og :mloop  (.-knownPeers intercomMeta))
                                             (def conn (.-peer (.-conn ch2)))
                                             (l/og :mloop  (.-conn ch2))
                                             ;filter out from knownPeers that user that is about to recieve knownPeers list
                                             (def filtrd (remove    #{conn}     (.-knownPeers intercomMeta)))
                                             (set! (.-knownPeers intercomMeta) filtrd)
                                             (l/og :mloop  (into-array filtrd))
                                             (l/og :mloop  (.-knownPeers intercomMeta))
                                             (>! (.-writec  ch2) (js-obj "type" "versionTsaritsa" "data" (.-id intercomMeta)))
                                             (>! (.-writec  ch2) (.stringify js/JSON (js-obj "type" "peerinfo" "data" (into-array (.-knownPeers intercomMeta) ))))
                                             )
                                           )


                                         )
            ;channel from some peer that recieves data to be sent to that peer(wrapper for peerjs send to peer)
            (== (.-type ch2) "writech") (do
                                          ; println vrecieved
                                          (l/og :mloop  "sending to peer " vrecieved)
                                          (l/og :mloop  (.-conn ch2))

                                          (.send (.-conn ch2 ) vrecieved)
                                          )
            ;recieves work from miners
            (== (.-type ch2) "workerch") (do 
                                           ; println vrecieved
                                           (l/og :mloop  "recieved from worker " vrecieved)
                                           (def blockk (makeBlock vrecieved))
                                           (l/og :blockchain "just made new block " blockk)
                                           ;(blockchain/addToChain blockk)
                                           ;TODO anounce to peers

                                           ; (.send (.-conn ch2 ) vrecieved)
                                           )
            ;recieves results from browser crypto functions
            (== (.-type ch2) "cryptoch") (do 
                                           ; println vrecieved
                                           (l/og :mloop  "recieved from crypto " vrecieved)
                                           (l/og :mloop "mempoll = " blockchain/memPool)
                                           (l/og :mloop (aget vrecieved "type"))
                                           (if (== (aget vrecieved "type") "fmr") (do
                                                                                    (l/og :mloop "merkle root " vrecieved )
                                                                                    (mine (aget vrecieved "value"))
                                                                                    )
                                             )
                                           (if (> (count blockchain/memPool) 3)

                                             (l/og :mloop "calculating hash of transactions(not merkle root now) %s" (blockchain/merkleRoot blockchain/memPool)))
                                           ; (.send (.-conn ch2 ) vrecieved)
                                           )
            ; recieves transactions
            (== (.-type ch2) "transactionch") (do
                                                ; println vrecieved
                                                (l/og :mloop  "recieved new transaction " vrecieved)
                                                ;put it in mempool
                                                ;send mempool to mining
                                                ;this might change
                                                (blockchain/sha256 vrecieved)
                                                ;(>! channel-1 vrecieved)
                                                ; (.send (.-conn ch2 ) vrecieved)
                                                )
            )

(recur (do)))
)
)
;what channels are listened on
(lp [connectionch hashmine transactionch cryptoCh])
)
(l/og :main  "Hello wor 32 d rdaldad!")
;(def myWorker (js/Worker. "hamiyoca/miner.js"))
;(def ^:dynamic o (js-obj "foo" 1 "bar" 2))
;(. myWorker (postMessage o))

)
(set! (.-onload js/window) foo)
; (let [v (alts!  peers )]
;    (println "recieved some data" v)
;    (.send (.-conn (nth v 1)) (nth v 0))
; )

;(println
;"The value from the first call is "
;(<! channel-1))             ;(conj state (<! connectionch))

;(println "peers" )
;(.log js/console 
;      peers)
; (println (alts!  peers ))
; (println "Killing webworkers")
; (servant/kill-servants servant-channel worker-count)

