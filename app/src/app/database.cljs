(ns database
  (:require
    [logger :as l]

    [mockdatabasew :as m]
    [cljs.core.async :refer [chan close! timeout put!]]

             ;[pubsub :as pubsub]
)
     (:require-macros [cemerick.cljs.test
                       :as tt
                       :refer (is deftest with-test run-tests testing test-var)] )
   )
(defn cleandb[]
  (m/cleandb)
  )
(defn dumpdb[]
  (m/dumpdb)
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


