(ns database
  (:require
    [logger :as l]
    [crypto ]

    [pouchDB :refer [dbase]]
    [cljs.core.async :refer [chan close! timeout put!]]

    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   )

  )



;initial function for db

(defn initDBase [dbase]

      (let [c (chan)]
           (go
             ;(.then (.get dbase "last") #(put! c %) #(put! c %))

             (def lastone (<! (g "last")))

             (l/og :initDBase "about to init")
             (l/og :initDBase "last one from database " lastone)
             (if lastone
               (do
                 (l/og :initDBase "last one from database is " lastone)

                 )
               (do
                 (l/og :initDBase "nothing in database")
                 (<! (ps "height" 0))
                 (def blck (js-obj "header"
                                   (makeBlockHeader 0 0 0 0 0 0 0)

                                   "hash" (<! (crypto/bHash 0)) "transactions" []))
                 ;args to make blockheader version previous fmroot timestamp bits nonce txcount
                 ;(def blockR (app.blockchain.makeBlockHeader "0" "0" "0" (.getTime ( js/Date.)) 0 "0" 0))
                 ;(def stringified (.stringify js/JSON blockR))
                 ;(l/og :blockchain "stringified initial" stringified)
                 ;(db/p   (<! (blockchain/s256 stringified)) [])

                 ;(saveBlock dbase blck)


                 (l/og :initDBase "saving " blck)

                 (set! (.-heightFromRoot (.-header blck)) 0)
                 ;(p "last" #(blck))
                 (<! (ps "last" blck))
                 ;todo save other info also
                 ;(.put dbase (js-obj "_id" (.-hash blockR) "val" blockR))
                 ;(.put dbase (js-obj "_id" (.-hash blockR) "val" blockR))
                 (<! (ps (.-hash blck) blck))
                 (<! (ps (+ "b" 0) blck))
                 )


               )
             ;(if last)
             ;(.put dbase (js-obj "_id" "height" "val" 1))
             )1)

      )
;
;promt user for id that will be used as his peer id
;(def id (js/prompt "enter id"))
;(l/og :main "user id %s " id)

(defn connectTo [ev id]
(go
      (l/og :connectTo (first id))
      (l/og :connectTo (<! ( get)) )

      (l/og :connectTo (<! ( get)) )
      (let [conn (.connect (<! ( get)) id)]
           (.on conn "open" (partial comm/onOpen conn))

           ;(channelsFromConnection conn)
           )
           ))


(def onDatabaseChange (chan))
(set! (.-type onDatabaseChange) "databaseChange")

(defn update [k f]
      (l/og :dbupdate "getting from db " k)
      (go
        (let [c (chan)]
             (defn sf [err v]
                   (l/og :dbupdate "about to update " v)
                   (l/og :dbupdate "about to update err " err)

                   (if v
                     (do (l/og :dbupdate "rev " (.-_rev v))
                         (.put dbase (js-obj "val" (f (.-val v))) k (.-_rev v) #(do)))
                     (.put dbase (js-obj "val" (f v)) k #(do))

                     )
                   )
             (.get dbase k sf)
             ;#(.put app.main.dbase (js-obj "_id" key "val" (f  false) )))


             ;
             ; (.put app.main.dbase (js-obj "_id" key "val" (f (.-val r)) "_rev" (.-rev r)))
             (def r (<! (g k)))

             (l/og :db (+ "got from db " k) r)
             (if r (.-val r) r)
             r
             ; (<! c)
             )

        )
      )
(defn g [k]
      (l/og :dbget "getting from db " k)
      (go
        (let [c (chan)]

             (.then (.get dbase k) #(put! c %) #(put! c false))

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
        (.put dbase (js-obj "_id" key "val" v) #(put! c 1))

        (<! c)
        (l/og :dbput "just done put s ")
        1
        )
      )
(defn p [key v]

      (l/og :dbput "putting from db " [key v])
      (.put dbase (js-obj "_id" key "val" v))

      )
;(def p (partial putDB ))
