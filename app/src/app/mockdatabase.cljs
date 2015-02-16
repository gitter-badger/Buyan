(ns mockdatabase
  (:require [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [app.util :as a :refer [await]]
                   [servant.macros :refer [defservantfn]])
  )
(def mdbase (js-obj))
(defn g[kie]
  (aget mdbase kie)
  )
(defn p[kie v]
  (aset mdbase kie v)
  )
(defn ps[kie v]
  (aset mdbase kie v)
  )
(defn update[kie v]
  (aset mdbase kie v)
  )


(defn initDBase [x]
      (def dbase mdbase)
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
                 ; (makeBlockHeader 0 0 0 0 0 0 0)
                 (def blck (js-obj "header"
                                  0


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
