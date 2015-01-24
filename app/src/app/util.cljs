(ns util)

;pub sub; send events to channel
(defn pub [ch event message] (go (>! ch message)))