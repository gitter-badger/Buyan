(ns pubsub
  (:require
    [cljs.core.async :refer [chan close! timeout put!]]
    [app.logger :as l]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]]))
(def proxychan (chan))
(def proxychan2 (chan 1))
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
(defn initpubsub []
      (go
        (loop []
              (def m (<! proxychan))
              (l/og :sub "about to deliver subbed %s" m)
              ((aget subs (aget m "typ"))
                (aget m "msg"))
              (recur )))
      )