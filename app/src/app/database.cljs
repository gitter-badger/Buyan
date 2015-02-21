(ns database
  (:require

    [app.logger :as l]
 [app.pubsub :as ps ]
    [cljs.core.async :refer [chan close! timeout put!]]

             ;[pubsub :as pubsub]
)
     (:require-macros
      [app.util :as a :refer [await sweet  c]]
 [cljs.core.async.macros :as m :refer [go]]
      [cemerick.cljs.test
                       :as tt
                       :refer (is deftest with-test run-tests testing test-var)] )
   )
(def states (js-obj))
(defn cleandb[]
  (m/cleandb)
  )
(defn dumpdb[]
  (go
  (c "mdumpdb")
   )
;  (c "mdumpdb" )
  )
(def onDatabaseChange (chan))
(set! (.-type onDatabaseChange) "databaseChange")

(defn update [k f]
      (l/og :dbupdate "getting from db " k)
  )
;(def p (partial putDB ))
(defn g[k]
      (go
        (<! (m/g k))
        )
      )
(defn update[k v]
      (go
        (<!  (m/update k v))
        )
      )
(defn p[k v]
      (go
        (<! (m/p k v))
        )
      )
(defn ps[k v]
      (go
        (<! (m/p k v))
        )
      )
(defn reg [typ v]

(go
;;   (go
;;     (l/og :receive "about to recieve %s" typ)
;;     (>!  statesCh (js-obj "typ" 0))
;;    ; (l/og :receive "returned message no for the loop " m)
;;    (def m (js-obj "typ" typ "msg" v))
;;     (<! (check typ v undefined))
;;   )
;;(c "log" :reg typ v)
  (l/og :reg typ v)
  (if v
    (aset states typ v)
    (if
    (aget states typ)

    (aget states typ)
      0
      )
    )
   )
   )

  ;initial function for db

;
;promt user for id that will be used as his peer id
;(def id (js/prompt "enter id"))
;(l/og :main "user id %s " id)
(defn initDBase [x]
  (l/og :initdbwraper "wrapper"  x)
  (m/initDBase x)
  )
(defn connectTo [ev id]
(go
      (l/og :connectTo "" (first id))
      (l/og :connectTo "" (<! ( get)) )

      (l/og :connectTo "" (<! ( get)) )
      (let [conn (.connect (<! ( get)) id)]
           (.on conn "open" (partial comm/onOpen conn))

           ;(channelsFromConnection conn)
           )
           ))


