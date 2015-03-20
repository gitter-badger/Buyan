(ns interop
  (:require
    [cljs.core.async :refer [chan close! timeout put!]]
    [logger :as l]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go  ]]
                     [util :as a :refer [await sweet c debug ac]]
                   [servant.macros :refer [defservantfn]]))

(defn jsinterop [fja & argxToF]
  (go
   (def c (chan))
    (.then (fja argxToF)  (fn [& argx]

                  (>! c argx)
                  ))
    (<! c)
   )
)
