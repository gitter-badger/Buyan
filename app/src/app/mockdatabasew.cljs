(ns mockdatabasew
  
  (:require
    [logger :as l]
    [mockdatabase :as m]
    [pubsub :refer [pub sub]]
    [cljs.core.async :refer [chan close! timeout put!]]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   )
  )
(def mdbase ()
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
