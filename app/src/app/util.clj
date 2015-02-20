(ns app.util)

;pub sub; send events to channel
;(defn pub [ch event message] (go (>! ch message)))
(defmacro await [&fja]

            ;`(<! (quote ~fja))
          )
(defmacro receiv [&fja]

            ;`(<! (quote ~fja))
          )
;; (defmacro sweet [where wat]
;;     (macroexpand (list '<!
;;                        (list
;;                         'ps/sia
;;                         where wat)))
;;   )
(defmacro sweet [ & wat]
    (macroexpand (list '<!
                       (list
                        'apply
                        'ps/sia
                         wat)))
  )
(defmacro c [where wat]
    (macroexpand (list '<!
                       (list
                        'ps/sia
                        where wat)))
  )
(defmacro cc [where & wat]
    (list '<!
                       (list
                        'apply
                        where
                        wat))
  )
(defmacro defn [nam arg & res]
     (list 'defn
                       arg
                       (macroexpand
                        (list
                        'go
                        res
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
