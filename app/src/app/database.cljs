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
