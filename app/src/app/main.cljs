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
)
;intercom is protocol state machine

;what channels are listened on
;(mainLoop [connectionch hashmine transactionch cryptoCh]))
(set! (.-onload js/window) entryy)
