(ns app.main
  (:require
    [app.intercom :as i]
    [communications :as comm]
    [app.logger :as l]
    [pubsub :refer [pub sub]]
    [cljs.core.async :refer [chan close! timeout put!]]
    [servant.core :as servant]
    [servant.worker :as worker])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]])
  )


(enable-console-print!)


;initial function for db



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
;(.on (js/$ js/document) "transaction" (partial pub transactionch))


;when someone connects to this user send that new connection to channel



;make two channels for connection. one for reading from conn, one for writing to it

(def empty-string "")

;(println id)
;keeps track of protocol and peers
;(initDBase dbase)

;(defn f [x] (println "fja") (println "x"))
;(sub "s1" f)
;(pub "s1" "asd")



(defn entryy []
      "main program entry point.
      It checks database to initialise it.
      enters loop waiting for messages and reacts to them"

      ;now that channels are setup

      (l/og :main "Hello wor 32 d rdaldad!")
      (l/og :conn "about to connect from heere")
      ;(.log js/console (nth peer 1))


      ;start submodules
      (pubsub/initpubsub)
      (comm/setupComm)
      ;intercom is protocol state machine
      ;this loop is for enabling p2p communcication
      (comm/startP2PCommLoop)

      ;what channels are listened on
      ;(mainLoop [connectionch hashmine transactionch cryptoCh])
      )
(set! (.-onload js/window) entryy)