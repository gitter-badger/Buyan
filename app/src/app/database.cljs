(ns database
  (:require
    [logger :as l]
    [crypto ]

    [mockdatabasew :as m]
    [cljs.core.async :refer [chan close! timeout put!]]

    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   )

  )

;(def p (partial putDB ))
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
