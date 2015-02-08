(ns app.main
  (:require
    [intercom :as i]
    [communications :as comm]
    [logger :as l]
    [pouchDB :refer [dbase]]
    [crypto ]
    [peerjs :refer [ peerParams]]
    [blockchain :refer [makeBlockHeader]]
    [database :refer [g p ps ]]
    [pubsub :refer [pub sub get set init]]

    [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [app.util :as a :refer [await]]
                   [servant.macros :refer [defservantfn]])
  )


(enable-console-print!)


;initial function for db

(defn initDBase [dbase]

      (let [c (chan)]
           (go
             ;(.then (.get dbase "last") #(put! c %) #(put! c %))

             (def lastone (<! (g "last")))

             (l/og :initDBase "about to init")
             (l/og :initDBase "last one from database " lastone)
             (if lastone
               (do
                 (l/og :initDBase "last one from database is " lastone)

                 )
               (do
                 (l/og :initDBase "nothing in database")
                 (<! (ps "height" 0))
                 (def blck (js-obj "header"
                                   (makeBlockHeader 0 0 0 0 0 0 0)

                                   "hash" (<! (crypto/bHash 0)) "transactions" []))
                 ;args to make blockheader version previous fmroot timestamp bits nonce txcount
                 ;(def blockR (app.blockchain.makeBlockHeader "0" "0" "0" (.getTime ( js/Date.)) 0 "0" 0))
                 ;(def stringified (.stringify js/JSON blockR))
                 ;(l/og :blockchain "stringified initial" stringified)
                 ;(db/p   (<! (blockchain/s256 stringified)) [])

                 ;(saveBlock dbase blck)


                 (l/og :initDBase "saving " blck)

                 (set! (.-heightFromRoot (.-header blck)) 0)
                 ;(p "last" #(blck))
                 (<! (ps "last" blck))
                 ;todo save other info also
                 ;(.put dbase (js-obj "_id" (.-hash blockR) "val" blockR))
                 ;(.put dbase (js-obj "_id" (.-hash blockR) "val" blockR))
                 (<! (ps (.-hash blck) blck))
                 (<! (ps (+ "b" 0) blck))
                 )


               )
             ;(if last)
             ;(.put dbase (js-obj "_id" "height" "val" 1))
             )1)

      )
;
;promt user for id that will be used as his peer id
;(def id (js/prompt "enter id"))
;(l/og :main "user id %s " id)

(def start (chan))


;channel to recieve new transaction
(def transactionch (chan))
(set! (.-type transactionch) "transactionch")

;channel to recieve results from crypto functions
(def cryptoCh (chan))
(set! (.-type cryptoCh) "cryptoch")

;listen on global document for transactions and publish them to channel transactionch
(.on (js/$ js/document) "transaction" ( fn [a1 a2 ] (pub "transaction" a2)) )

(defn connectTo [ev id]
(go
      (l/og :connectTo (first id))
      (l/og :connectTo (<! ( get)) )

      (l/og :connectTo (<! ( get)) )
      (let [conn (.connect (<! ( get)) id)]
           (.on conn "open" (partial comm/onOpen conn))

           ;(channelsFromConnection conn)
           )
           ))
(.on (js/$ js/document) "connectTo"  connectTo)


;when someone connects to this user send that new connection to channel



;make two channels for connection. one for reading from conn, one for writing to it

(def empty-string "")

;(println id)
;keeps track of protocol and peers
;(initDBase dbase)

;(defn f [x] (println "fja") (println "x"))
;(sub "s1" f)
;(pub "s1" "asd")

;this here is for debugging
;(.enable (.-debug js/PouchDB) "*")
(defn setID [ev id ]
      (println id)
      (go
        (p "lid" id)
        (.log js/console  (<! (g "lid")))


          (def peerjs (js/Peer. id   peerParams))
          (init peerjs)
          (.on peerjs "connection" comm/onConnection)

        )

      ;(.on peerjs "connection" onConnection)
      )
(.on (js/$ js/document) "setid"  setID)


(defn entryy []
      "main program entry point.
      It checks database to initialise it.
      enters loop waiting for messages and reacts to them"

      ;now that channels are setup

      (l/og :main "Hello wor 32 d rdaldad!")
      (l/og :conn "about to connect from heere")
      ;(.log js/console (nth peer 1))
      (go
        (def id (<! (g "lid")))
        (l/og :entryy "got id %s " id)
        (if id (do

                 (.log js/console id)
                 (.val  (js/$ "#id") id)
                 (def peerjs (js/Peer. id   peerParams))
                  (init peerjs)
                 (.on peerjs "connection" comm/onConnection)
                 )
               (do
                 (<! ( initDBase))
              ))


      ;start submodules
      (pubsub/initpubsub)
      ;register all pubsub subscriptions
      (comm/setupComm)
      (comm/startP2PCommLoop)

        ))
      ;intercom is protocol state machine

      ;what channels are listened on
      ;(mainLoop [connectionch hashmine transactionch cryptoCh]))
(set! (.-onload js/window) entryy)