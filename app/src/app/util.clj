(ns app.util)

;pub sub; send events to channel
;(defn pub [ch event message] (go (>! ch message)))
(defmacro await [fja]

          (macroexpand '( go
            (.log js/console "asdas")
            (def c (chan))
            ;;needs fix
            (  ~fja)
            (>! c 1)
            (<! c)

          )))