(ns intercomMake
  (:require
    [app.logger :as l]
    [app.database :as db]

    [app.blockchain :as blockchain]
    [cljs.core.async :refer [chan close! timeout put!]]

    )

  (:require-macros [cljs.core.async.macros :as m :refer [go]])
  )
(defn makeInv [typ message]

      )