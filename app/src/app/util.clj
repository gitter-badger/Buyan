(ns app.util)

;pub sub; send events to channel
;(defn pub [ch event message] (go (>! ch message)))
(defmacro await [&fja]

            ;`(<! (quote ~fja))
          )
(defmacro receiv [&fja]

            ;`(<! (quote ~fja))
          )
(defmacro sweet [where wat]
    (macroexpand (list '<!
                       (list
                        'ps/sia
                        where wat)))
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
