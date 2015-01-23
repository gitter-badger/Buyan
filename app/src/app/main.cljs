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
                   [servant.macros :refer [defservantfn]]))


(enable-console-print!)

(def peers [])

;initial function for db


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
(. js/console (log (THREE/Scene.)))
;data for peer connection
(def ^:dynamic peerParams (js-obj "host" "localhost" "port" 8000 "key" "peerjs" "debug" true))
;promt user for id that will be used as his peer id
;(def id (js/prompt "enter id"))
(l/og :main "user id %s " id)

(def start (chan))
; channel to anounce new connectinos
(def connectionch (chan))
;peerjs object
(def peerjs (js/Peer. id peerParams))

(def onDatabaseChange (chan))
(set! (.-type onDatabaseChange) "databaseChange")

;database instance
(def dbase (js/PouchDB. "dbname"))
;(.enable (.-debug js/PouchDB) "*")


(initDBase dbase)




; (go
; (l/og :db "last entry with new func" (<! (gdb "last")) )
; )
;channel to recieve new transaction
(def transactionch (chan))
(set! (.-type transactionch) "transactionch")

;channel to recieve results from crypto functions
(def cryptoCh (chan))
(set! (.-type cryptoCh) "cryptoch")

;listen on global document for transactions and publish them to channel transactionch
(.on (js/$ js/document) "transaction" (partial pub transactionch))


;when someone connects to this user send that new connection to channel


(.on peerjs "connection" onConnection)
;make two channels for connection. one for reading from conn, one for writing to it

(def empty-string "")

(println id)
;(if (== id "2")
; (println "id = 2"))
;keeps track of protocol and peers





(defn foo []
      "main program entry point.
      It checks database to initialise it.
      enters loop waiting for messages and reacts to them"

      ;now that channels are setup
      (do
        (l/og :conn "about to connect from heere")
        (def peer (connectTo "2"))
        ;(.log js/console (nth peer 1))



        ;what channels are listened on
        (lp [connectionch hashmine transactionch cryptoCh])
        )
      (l/og :main "Hello wor 32 d rdaldad!")
      ;(def myWorker (js/Worker. "hamiyoca/miner.js"))
      ;(def ^:dynamic o (js-obj "foo" 1 "bar" 2))
      ;(. myWorker (postMessage o))

      )
(set! (.-onload js/window) foo)
