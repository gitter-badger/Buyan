(ns intercomMake
  (:require
    [app.logger :as l]
    [app.database :as db]

    [app.blockchain :as blockchain]

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
(defn makeVersion [typ message]

      )
(defn makeConn [conn]
      (l/og :makeConn "make conn " conn)
      (js-obj "type" "conn" "data" conn)
      )
(defn makeGetBlock [hash]
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