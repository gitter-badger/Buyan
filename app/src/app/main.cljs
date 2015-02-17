(ns app.main
  (:require

    [logger :as l]
    [pubsub :as ps]
    [router ]
    [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [app.util :as a :refer [await]]
                   [servant.macros :refer [defservantfn]])
  )


(enable-console-print!)






;listen on global document for transactions and publish them to channel transactionch
;(.on (js/$ js/document) "transaction" ( fn [a1 a2 ]
                                        ;(pub "transaction" a2)
   ;                                     ) )

;(.on (js/$ js/document) "connectTo"  connectTo)
(defn dumpdb []
  (ps/s "dumpdb" "")
  )
(.on (js/$ js/document) "dumpdb"  dumpdb)
(defn cleandb []
  (ps/s "cleandb" "")
  )
(.on (js/$ js/document) "cleandb"  cleadb)
;when someone connects to this user send that new connection to channel

(defn replScratchFunction[]
 (ps/si "dumpdb")
  (go
   (<! (ps/si "hello" "a"))
  )
   (dumpdb)
  (cleandb)
  )

;make two channels for connection. one for reading from conn, one for writing to it

(def empty-string "")


;(.on (js/$ js/document) "setid"  setID)
(defn pri [x]
  (println x)
  3
  )
(defn entryy []
      "main program entry point.
      It checks database to initialise it.
      enters loop waiting for messages and reacts to them"

      ;now that channels are setup
  (router/route)
  (go

  ;  (ps/s "msg1" "text")
  ;  (ps/s "msg2" "text")
   ;(def a (<! (ps/rr "asd" pri "msg1" pri)))
    (l/og :main "received" a)
  )
)

;intercom is protocol state machine

;what channels are listened on
;(mainLoop [connectionch hashmine transactionch cryptoCh]))
(set! (.-onload js/window) entryy)
