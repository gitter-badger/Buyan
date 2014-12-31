(ns app.database
(:require
    [app.logger :as l]
    [cljs.core.async :refer [chan close! timeout put!]]
    
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   )
    )

  (defn g [ k]
     (l/og :db "getting from db " k)
     (go
      (let [c (chan)]
      
        (.then (.get app.main.dbase k) #(put! c %) #(put! c %))
        
        (def r (<! c))
(l/og :db (+ "got from db " k) r)
        r
      )
      )
  )
  ;(def g (partial getDB dbase))
  (defn p [ key v]
    (l/og :db  "putting from db " [key v])
    (.put app.main.dbase (js-obj "_id" key "val" v))
  )
  ;(def p (partial putDB ))