(ns app.main
  (:require
    [communications]
    [logger :as l]
    [pubsub :as ps :refer [sia swp]]
    [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [util :as a :refer [await sweet c ac]]
                   [servant.macros :refer [defservantfn]])
  )


(enable-console-print!)






;listen on global document for transactions and publish them to channel transactionch
(.on (js/$ js/document) "transaction" ( fn [a1 a2 ]
                                       (ps/pub "transaction" a2)
                                      ) )
(defn connectTo [ev id]
(go
    (c "connectTo" ev id)
           ))
(.on (js/$ js/document) "connectTo"  connectTo)

(defn dumpdb [](go (c "dumpdb" )))
(.on (js/$ js/document) "dumpdb"  dumpdb)

(defn cleandb [x y](go (c "cleandb" )))
(.on (js/$ js/document) "flushdb"  cleandb)

(.on (js/$ js/document) "setid"  (fn [x y](go (ac "setID" x y)) ))

;when someone connects to this user send that new connection to channel

(defn replScratchFunction[]
 (ps/si "dumpdb")
  (go
   (<! (ps/sia "hash" "s"))
  )
  (c "makeBlock" "wadsdsad")
   (dumpdb)
  (cleandb)
  )

;make two channels for connection. one for reading from conn, one for writing to it

(def empty-string "")


(defn pri [x]
  (println x)
  3
  )
(defn entryy []
      "main program entry point.
      It checks database to initialise it.
      enters loop waiting for messages and reacts to them"

      ;now that channels are setup
 ; (router/route)

   (aset  js/window "messages" (js-obj))
  (aset  js/window "hook" (js-obj))
  (aset  js/window "preroutinghook" (js-obj))
  (aset  js/window "postroutinghook" (js-obj))
  (go
   (-> js/document
       js/$
       (.trigger "setid")
       )
    (c "db" "connections" (array))
   (pubsub.initpubsub)
  (communications/startP2PCommLoop)
  ;  (ps/s "msg1" "text")
  ;  (ps/s "msg2" "text")
   ;(def a (<! (ps/rr "asd" pri "msg1" pri)))

  (c "ui" "html")
    ;(l/og :main "0=" (c "database" "s"))
  ;  (l/og :main "s1="  (c "database" "s" 1))
  ;  (l/og :main "1=" (c "database"  "s"))
  ;  (l/og :main "s2="  (c "database"  "s" 2))
  ;  (l/og :main "2=" (c "database"  "s"))
  ; (c "dumpdb")
  )
)

;intercom is protocol state machine

;what channels are listened on
;(mainLoop [connectionch hashmine transactionch cryptoCh]))
(set! (.-onload js/window) entryy)
