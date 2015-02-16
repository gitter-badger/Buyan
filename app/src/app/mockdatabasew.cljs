(ns mockdatabasew

  (:require
    [logger :as l]
    [mockdatabase :as m ]
    [pubsub :refer [pub sub]]
    [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [app.util :as a :refer [await]]
                   [servant.macros :refer [defservantfn]])
  )
(defn cleandb[]
  (l/og :cleandb "cleandb " m/mdbase)
  (set! m/mdbase (js-obj))
  )
(defn dumpdb[]
  (l/og :dumpdb "dbase " m/mdbase)
  )
(defn g[k]
  (go
   (m/g k)
    )
  )
(defn update[k v]
  (go
     (m/update k v)
    )
  )
(defn p[k v]
  (go
     (m/p k v)
    )
  )
(defn initDBase [x]
  (m/initDBase x)
  )
