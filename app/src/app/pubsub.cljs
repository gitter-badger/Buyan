(ns pubsub
  (:require
    [cljs.core.async :refer [chan close! timeout put!]]
    [logger :as l]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]]))
(def proxychan (chan))
(def proxychan2 (chan 1))
(def sendReceiveCh (chan 1000))
(defn get [] (go

                  (def a (<! proxychan2))
                  (l/og :getpubsub "peerjs" a)
                  (>! proxychan2  a)
                  (l/og :getpubsub "peerjs" a)
                  a
                  )
  )
(defn init [what] (go

                    (l/og :initpubsub "peerjs" what)
                     (>! proxychan2 what)

                     ))

(defn set [what] (go (<! proxychan2)
                          (>! proxychan2 what)

                          )

  )

(def subs (js-obj))
(defn sub [typ fun] (aset subs typ fun))
(defn pub [typ msg]
      (l/og :pub "pubing " (+ typ " " msg))
      (go (>! proxychan (js-obj "typ" typ "msg" msg)))
      )

(defn makeMsg [typ m]
  (js-obj "typ" typ "msg" m)
  )
(defn initpubsub []
      (go
        ;(>!  sendReceiveCh (makeMsg "maker" 0))
     )
      )
(defn r [typ]

  (go
    (l/og :receive "about to recieve %s" typ)
    (>!  sendReceiveCh (js-obj "typ" 0))
   ; (l/og :receive "returned message no for the loop " m)
    (loop []
      (l/og :receive "about to recieve in loop")
      (def mtemp (<! sendReceiveCh))
      (l/og :receive "now looking at " mtemp)
      (if (== (aget mtemp "typ") typ)
        (aget mtemp "msg")

        (if (== (aget mtemp "typ") 0)
          0
          (recur )
        )
      )
    )
   )
  )
(defn s [typ m]
  (go
        (l/og :send typ m)
        (>! sendReceiveCh (makeMsg typ m) )
   )
  )
