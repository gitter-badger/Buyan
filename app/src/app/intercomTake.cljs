(ns intercomTake
  (:require
    [app.logger :as l]
    [app.database :as db]

    [app.blockchain :as blockchain]
    [cljs.core.async :refer [chan close! timeout put!]]

    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]])
  )

(defn takeInv [typ message]
      )
(defn takeData [typ message]
      )

(defn takeGetVersion [typ message]
      )
(defn takeVersion [typ message]
      )

(defn takeConn [typ message]
      )
