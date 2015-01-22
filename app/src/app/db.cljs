(ns app.database
  (:require
    [app.logger :as l]
    [cljs.core.async :refer [chan close! timeout put!]]

    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   )
  )
(defn update [k f]
      (l/og :dbupdate "getting from db " k)
      (go
        (let [c (chan)]
             (defn sf [err v]
                   (l/og :dbupdate "about to update " v)
                   (l/og :dbupdate "about to update err " err)

                   (if v
                     (do (l/og :dbupdate "rev " (.-_rev v))
                         (.put app.main.dbase (js-obj "val" (f (.-val v))) k (.-_rev v) #(do)))
                     (.put app.main.dbase (js-obj "val" (f v)) k #(do))

                     )
                   )
             (.get app.main.dbase k sf)
             ;#(.put app.main.dbase (js-obj "_id" key "val" (f  false) )))


             ;
             ; (.put app.main.dbase (js-obj "_id" key "val" (f (.-val r)) "_rev" (.-rev r)))
             (def r (<! (g k)))

             (l/og :db (+ "got from db " k) r)
             r
             ; (<! c)
             )

        )
      )
(defn g [k]
      (l/og :dbget "getting from db " k)
      (go
        (let [c (chan)]

             (.then (.get app.main.dbase k) #(put! c %) #(put! c false))

             (def r (<! c))
             (l/og :dbget (+ "got from db " k) r)
             (if r (.-val r) r)
             ; (<! c)
             )

        )
      )
;(def g (partial getDB dbase))
(defn ps [key v]
      (go
        (def c (chan))
        (l/og :dbput "putting from db " [key v])
        (.put app.main.dbase (js-obj "_id" key "val" v) #(put! c 1))

        (<! c)
        (l/og :dbput "just done put s ")
        1
        )
      )
(defn p [key v]

      (l/og :dbput "putting from db " [key v])
      (.put app.main.dbase (js-obj "_id" key "val" v))

      )
;(def p (partial putDB ))