(ns intercomMake
  (:require

 [pubsub :as ps :refer [sia]]

    [cljs.core.async :refer [chan close! timeout put!]]

    )
  (:require-macros
   [app.util :as a :refer [await sweet]]

   [cljs.core.async.macros :as m :refer [go]]
                   )

  )
(defn makeInv [typ message]
      ;"this function will make inv message "
      ;(def v (.stringify js/JSON (js-obj "type" "inv" "data" (js-obj "type" typ "vector" message))))
      ;(set! (.-type v) "json")
      (js-obj "type" "json" "data" (.stringify js/JSON (js-obj "type" "inv" "data" (js-obj "type" typ "vector" message))))
      )
(defn makeData [typ message]

      )
(defn makeGetVersion [typ message]

      )
(defn makeVersion [message]

      (js-obj "type" "version" "data" message)
      )
(defn makeConn [conn]
      (l/og :makeConn "make conn " conn)
      (js-obj "type" "conn" "data" conn)
      )
(defn makeGetBlock [hash]

      (l/og :makeGetBlock "about to make block " hash)
      (go
        (def gtBlock (js-obj "count" 0 "blocks" (array) hash_stop 0))
        ;get block height
        (def heightForBlock (<! (app.blockchain/blockchainHeight 1)))

        (loop [cnt heightForBlock blocksPushed 0]
              (l/og :makeGetBlock (+ "new loop " cnt " ") blocksPushed)
              ;get block with the cnt number
              (def blockg (<! (db/g (+ "b" cnt))))

              (l/og :makeGetBlock "curr block ")
              ;
              (set! (.-count gtBlock) cnt)
              (.push (.-blocks gtBlock) blockg)
              ;for first 10 step is 1
              ; for others step is *2
              (if (< 0 cnt)
                (recur (if (< blocksPushed 10)
                         (- cnt 1)
                         (quot cnt 2)
                         ) (+ blocksPushed 1)
                       ))
              )
        ;return
        gtBlock
        )
      )
