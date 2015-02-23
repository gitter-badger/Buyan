(ns util)

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
(defmacro c [ & args]
;;      (list '<!
;;                        (list
;;                         'apply
;;                         'ps/sia
;;                         (quote typ)
;;                          ;'quote typ
;;                         (quote wat)))

  (list '<!  `( ps/sia   ~@args))
  ;(def a (second (split-at 2 &form)))
  ;`(l/og :asd ~@args)
  ;`(<! (apply ps/sia unquote(typ) unquote(wat)))
  )
;; (defmacro cc [where & wat]
;;     (list '<!
;;                        (list
;;                         'apply
;;                         where
;;                         wat))
;;   )
(defmacro defnasd [nam arg & res]
     (list 'defn       nam
                       arg
                       (macroexpand
                        (list
                        'go
                        res
                        )))
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
