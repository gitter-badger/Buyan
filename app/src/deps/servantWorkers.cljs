(ns servantWorkers
  ( :require


    [servant.core :as servant]
    [servant.worker :as worker]
    )
  (:require-macros
                   [servant.macros :refer [defservantfn]])
  )
;now to define how much threads will mine
(def worker-count 2)
;mining script path
(def worker-script "assets/javascripts/wrkr.js")
;this channel is for servant to know at which thread pool to dispatch
(def servant-channel (servant/spawn-servants worker-count worker-script))
