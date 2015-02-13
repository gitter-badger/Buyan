(ns app.main
  (:require

    [pubsub :refer [pub sub get set init]]
     [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [app.util :as a :refer [await]]
                   [servant.macros :refer [defservantfn]])
  )


(enable-console-print!)


<<<<<<< HEAD
=======
;initial function for db


;
;promt user for id that will be used as his peer id
;(def id (js/prompt "enter id"))
;(l/og :main "user id %s " id)

(def start (chan))
>>>>>>> 58c0351e16738b5aa3397015782f8900d832c865


;listen on global document for transactions and publish them to channel transactionch
(.on (js/$ js/document) "transaction" ( fn [a1 a2 ] (pub "transaction" a2)) )

(.on (js/$ js/document) "connectTo"  connectTo)


;when someone connects to this user send that new connection to channel



;make two channels for connection. one for reading from conn, one for writing to it

(def empty-string "")


(.on (js/$ js/document) "setid"  setID)

(defn entryy []
      "main program entry point.
      It checks database to initialise it.
      enters loop waiting for messages and reacts to them"

      ;now that channels are setup

      (l/og :conn "about to connect from heere")
      ;(.log js/console (nth peer 1))
<<<<<<< HEAD
)
=======
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
                 ;  (<! ( initDBase))
                 ))


        ;start submodules
        (pubsub/initpubsub)
        ;register all pubsub subscriptions
        (comm/setupComm)
        (comm/startP2PCommLoop)
        (ht/run "Taras Bulba" "zaparozie r0x" "i4c32d4308e1fe.jpg" "- zaparozie")

        ))
>>>>>>> 58c0351e16738b5aa3397015782f8900d832c865
;intercom is protocol state machine

;what channels are listened on
;(mainLoop [connectionch hashmine transactionch cryptoCh]))
(set! (.-onload js/window) entryy)
