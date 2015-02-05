(ns app.util)

;pub sub; send events to channel
;(defn pub [ch event message] (go (>! ch message)))
(defmacro await [fja]

            `(<! (quote ~fja))
          )

;
;(macroexpand (list 'go
;                   '(.log js/console "asdas")
;                   '(def c (chan))
;                   ;;needs fix
;
;                   '(>! c (<! fja))
;                   '(<! c)
;
;                   ))