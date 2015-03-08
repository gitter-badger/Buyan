(ns mp

    (:require

    [pubsub :as ps ]


    [logger :as l]
    [cljs.core.async :refer [chan close! timeout put!]]

    )
  (:require-macros
   [util :as a :refer [await sweet c ac]]

   [cljs.core.async.macros :as m :refer [go]]
                   )
  )

(defn m[message]

(js-obj "data" (.-data message) "id"
   (-> message
       .-peer
       .-conn
       .-peerConnection
       .-remoteDescription
       .-sdp
       (.split  " ")

       (aget 5)
       (.split "\n")
       (aget 0)
       )



        )
  )
