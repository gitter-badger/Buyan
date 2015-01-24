(ns servantWorkers)
;now to define how much threads will mine
(def worker-count 2)
;mining script path
(def worker-script "wrkr.js")
;this channel is for servant to know at which thread pool to dispatch
(def servant-channel (servant/spawn-servants worker-count worker-script))