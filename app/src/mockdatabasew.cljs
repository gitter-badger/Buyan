(ns mockdatabasew

  (:require
    [pubsub :as ps ]
    [logger :as l]
    [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
[util :as a :refer [await sweet c ]]
                       [servant.macros :refer [defservantfn]])
  )

(def states (js/Object.))
;; (defn g[kie]
;;   (aget mdbase kie)
;;   )
;; (defn p[kie v]
;;   (aset mdbase kie v)
;;   )
;; (defn ps[kie v]
;;   (aset mdbase kie v)
;;   )
;; (defn update[kie v]
;;   (aset mdbase kie v)
;;   )

(defn cleandb[]

  (l/og :cleandb "cleandb " mdbase)
  (set! states (js-obj))
  (go 1))
(defn dumpdb[]
 (go


  (l/og :dumpdb "dbase " states)
  1
)
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
(defn ps[k v]
  (go
     (m/ps k v)
    )
  )
(defn reg [typ v]


;;   (go
;;     (l/og :receive "about to recieve %s" typ)
;;     (>!  statesCh (js-obj "typ" 0))
;;    ; (l/og :receive "returned message no for the loop " m)
;;    (def m (js-obj "typ" typ "msg" v))
;;     (<! (check typ v undefined))
;;   )
  (.log js/console states)
  (l/og :reg typ v)
(go
  (if v
    (do
      (aset states typ v)
    v)
    (if
      (aget states typ)
    (aget states typ)

      0
      ))
)  )
(defn fixture1[]
  (go
   (def fwork (js-obj "root" "somehash"   "nonce" "somenonce"  "newhash" "found"))
    (l/og :main "0="
  (c "initdb" ))

    (l/og :main "0="
 (c "dumpdb" ))

    (def blck1 (js-obj "header" 0 "hash" (c "hash" 1) "transactions" []))
    (c "saveBlock" blck1 )
    (l/og :main "0="
               (c "dumpdb" ))
                 (def blck2 (js-obj "header" 0 "hash" (c "hash" 2) "transactions" []))
                 (def blck3 (js-obj "header" 0 "hash" (c "hash" 3) "transactions" []))
    (c "saveBlock" blck2 )
    (c "saveBlock" blck3 )
    (l/og :main "0="
               (c "dumpdb" ))
   )
 )
(defn initDBase [x]
  (go
  (l/og :initdbwraper2 "wrapper"  x)
     ; (def dbase  m/mdbase)
      (let [cc (chan)]

             ;(.then (.get dbase "last") #(put! c %) #(put! c %))

             (def lastone (c "db" "last"))

             (l/og :initDBase "about to init")
             (l/og :initDBase "last one from database " lastone)
             (if false
               (do
                 (l/og :initDBase "last one from database is " lastone)

                 )
               (do
                 (l/og :initDBase "nothing in database")
                 (c "db" "height" 0)
                 ; (makeBlockHeader 0 0 0 0 0 0 0)
                 (def blck (js-obj "header"
                                  0


                                   "hash" (c "hash" 0) "transactions" []))
                 ;args to make blockheader version previous fmroot timestamp bits nonce txcount
                 ;(def blockR (app.blockchain.makeBlockHeader "0" "0" "0" (.getTime ( js/Date.)) 0 "0" 0))
                 ;(def stringified (.stringify js/JSON blockR))
                 ;(l/og :blockchain "stringified initial" stringified)
                 ;(db/p   (<! (blockchain/s256 stringified)) [])

                 ;(saveBlock dbase blck)


                 (l/og :initDBase "saving " blck)

                 (set! (.-heightFromRoot (.-header blck)) 0)
                 ;(p "last" #(blck))
                 (c "db" "last" blck)
                 ;todo save other info also
                 ;(.put dbase (js-obj "_id" (.-hash blockR) "val" blockR))
                 ;(.put dbase (js-obj "_id" (.-hash blockR) "val" blockR))
                 (c "db" (.-hash blck) blck)
                 (c "db" (+ "b" 0) blck)

                 )


               )
             ;(if last)
             ;(.put dbase (js-obj "_id" "height" "val" 1))
             )1)

      )
