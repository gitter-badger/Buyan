(ns display.renderStrategy
  ( :require

    [display.html :as html]
    [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [util :as a :refer [await sweet c ]]
                   [servant.macros :refer [defservantfn]])
  )

(defn run [renderer]
  (go
      ;(cond
      ;  (== renderer :html) (html/run)
      ;  true
      ;)
      (html/ui)
      ))
