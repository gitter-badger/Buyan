(ns pubsub
  (:require
    [cljs.core.async :refer [chan close! timeout put!]]
    [logger :as l]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go  ]]
                     [util :as a :refer [await sweet c debug ac]]
                   [servant.macros :refer [defservantfn]]))
(def proxychan (chan))
(def proxychan2 (chan 1))
(def sendReceiveCh (chan 1000))


(def statesCh (chan 1000))
(def states (js-obj))
(def receiveCH (chan 1000))
(defn get [] (go

                  (def a (<! proxychan2))
                  (l/og :getpubsub "peerjs" a)
                  (>! proxychan2  a)
                  (l/og :getpubsub "peerjs" a)
                  a
                  )
  )
(defn init [what] (go

                    (l/og :initpubsub "peerjs" what)
                     (>! proxychan2 what)

                     ))

(defn set [what]
  (go
    (<! proxychan2)
    (>! proxychan2 what)

  )

)

(def subs (js-obj))
(defn sub [typ fun]

  (if (aget subs typ)
  (aset subs typ (conj  (aget subs typ) fun))

  (aset subs typ  [fun])

    )
  )
(defn trig [typ msg]
  (-> "body"
      (js/$)
      (.trigger typ msg)

   )
  )
(defn pub [typ msg]
    (go   (l/og :pub "pubing " (+ typ " " msg))

      (if  (aget (aget  js/window "messages") typ )

        (trig typ msg)
        (>! proxychan (js-obj "typ" typ "msg" msg)))

        )

      )
(defn initpubsub []
(go

 (-> js/document
     (js/$)
     (.on "pubsub" (fn [ev m]
                     (go
                     ;(js-obj "typ" typ "msg" msg)
                     (>! proxychan m)
                     )))
  )

  (-> js/document
     (js/$)
     (.on "call" (fn [ev m](go
                     ;(js-obj "typ" typ "msg" msg)
                    (l/og :callfromevent ev  m)
                    (def argx (into [(aget m "typ" ) ]
                     (aget m "msg" )))
                   (l/og :callfromevent "argx=" argx)
                     (<! (apply pubsub.sia
                       argx
                       )
                     )
                     ))))


(loop []
(l/og :initpubsub "started loop" )

(def m (<! proxychan))
(l/og :initpubsub "about to deliver subbed %s" m)
;  (l/og :initpubsub (aget subs (aget m "typ")))

  (loop [col (aget subs (aget m "typ"))]
    (if col
    ;(l/og :initpubsub "trying to invoke" (first col))
    ((first col) (aget m "msg"))

    (def remainingsubs (rest col))

    (if (> (count remainingsubs) 0)
      (recur remainingsubs)
      )
    )
)
(recur )))
)
(defn makeMsg [typ m pchannel]
  (js-obj "typ" typ "msg" m)
  )
;(defn initpubsub []
 ;     (go
        ;(>!  sendReceiveCh (makeMsg "maker" 0))
;     )
;      )
(defn r [typ]

  (go
    (l/og :receive "about to recieve %s" typ)
    (>!  statesCh (js-obj "typ" 0))
   ; (l/og :receive "returned message no for the loop " m)
    (loop []
      (l/og :receive "about to recieve in loop")
      (def mtemp (<! sendReceiveCh))
      (l/og :receive "now looking at " mtemp)
      (if (== (aget mtemp "typ") typ)
        (do

          (>! statesCh m)
        (aget mtemp "msg")
          )
        (do


        (if (== (aget mtemp "typ") 0)
          0
          (recur )
        )
          )
      )
    )
   )
  )
(defn check[ typ v ret]
  (go
   (def m (js-obj "typ" typ "msg" v))
      (def mtemp (<! statesCh))
            (l/og :swp "checking  " mtemp)
  (if (== (aget mtemp "typ") typ)

          (if v
            (do
            (l/og :swp "found val  " mtemp)

            (l/og :swp "putting back   " m)
          (>! statesCh m)

          (<! (check typ v v))
            )
            (do

            (l/og :swp "found no val  " mtemp)
              (aset mtemp "typ" typ)
          (>! statesCh mtemp)

          (<! (check typ v (aget mtemp "msg")))
          )
          )
        ;;not found



        (if (== (aget mtemp "typ") 0)
          (if ret

            (do

            (l/og :swp "ending " v)
            ret

              )
            (if v
          (do
            (l/og :swp "got nul val is " v)
            (>! statesCh m)
            v
            )
              )
          )

          (do
            (>! statesCh mtemp)

            (l/og :swp "itteration  " ret)
          (<!(check typ v ret))
          )
        )

    )
   )
  )
(defn swp [typ v]


;;   (go
;;     (l/og :receive "about to recieve %s" typ)
;;     (>!  statesCh (js-obj "typ" 0))
;;    ; (l/og :receive "returned message no for the loop " m)
;;    (def m (js-obj "typ" typ "msg" v))
;;     (<! (check typ v undefined))
;;   )

  (if v
    (aset states typ v)
    (aget states typ)
    )
  )

(defn rr [& typ]

  (go
    (l/og :receive "about to recieve " typ)

    (l/og :receive "about to recieve " (/ (count typ) 2))

    (>!  sendReceiveCh (js-obj "typ" 0))
   ;;loop until all messages have been checked
   ;;similar to the way erlang process checks its mailbox
    (loop []
      (l/og :receive "about to recieve in loop")
      (def mtemp (<! sendReceiveCh))
      (if (== (aget mtemp "typ") 0)
        (do
          ;;if we looked at all return 0
          (l/og :receive "got null")
          0
        )


        (do
          (l/og :receive "now looking at " mtemp)
          ;; out of all messages we are waitin for
          ;; check if the one extracted from "mailbox" is among them
          ;;
          (def result (loop [cnt 0]

            (if (< cnt  (count typ) )
              (do
                (l/og :receive "checking " cnt)
                (l/og :receive "got " mtemp)

                (l/og :receive "looking for " (nth typ cnt ))

                (if (== (aget mtemp "typ") (nth typ cnt ))
                  (do
                    ;;yay we found one now execute the function associated with it
                    (l/og :receive "found " (nth typ cnt ))
                  (<! ((nth  typ (+ cnt 1)) (aget  mtemp "msg")))
                  )

                  (do

                  (recur (+ cnt 2)))
                )
              )
            )
          ))
        )
      )
   ))
    )

(defn rrs [& typ]

  (go
    (l/og :receive "about to recieve " typ)

    (l/og :receive "about to recieve " (/ (count typ) 2))

    ;;(>!  sendReceiveCh (js-obj "typ" 0))
   ;;loop until all messages have been checked
   ;;similar to the way erlang process checks its mailbox
    (loop []
      (l/og :receive "about to recieve in loop")
      (def mtemp (<! sendReceiveCh))
      (if (== (aget mtemp "typ") 0)
        (do
          ;;if we looked at all return 0
          (l/og :receive "got null")
          0
        )


        (do
          (l/og :receive "now looking at " mtemp)
          ;; out of all messages we are waitin for
          ;; check if the one extracted from "mailbox" is among them
          ;;
          (def result (loop [cnt 0]

            (if (< cnt  (count typ) )
              (do
                (l/og :receive "checking " cnt)
                (l/og :receive "got " mtemp)

                (l/og :receive "looking for " (nth typ cnt ))

                (if (== (aget mtemp "typ") (nth typ cnt ))
                  (do
                    ;;yay we found one now execute the function associated with it
                    (l/og :receive "found " (nth typ cnt ))
                    (def ret ((nth  typ (+ cnt 1)) (aget  mtemp "msg")))
                    (l/og :receive "return " ret)
                    (>! receiveCH ret)
                  )
                  (do

                  ;(>!  sendReceiveCh mtemp)
                  (recur (+ cnt 2)))
                )
              )
            )
          ))
        )
      )
   (recur)))
    )



(defn rrsa [rch sch   & typ]

  (go
         (if  (aget (aget  js/window "hook") typ )
           (do
        ((aget (aget  js/window "hook") typ ) msg)
             )

        (do
    (l/og :receive "about to recieve " typ)

    (l/og :receive "about to recieve " (/ (count typ) 2))

    ;;(>!  sendReceiveCh (js-obj "typ" 0))
   ;;loop until all messages have been checked
   ;;similar to the way erlang process checks its mailbox
    (loop []
      (l/og :receive "about to recieve in loop")
      (def mtemp (<! sch))
      (l/og :receive "about to recieve in loop" mtemp)
      (if (== (aget mtemp "typ") 0)
        (do
          ;;if we looked at all return 0
          (l/og :receive "got null")
          0
        )


        (do
          (l/og :receive "now looking at " mtemp)
          ;; out of all messages we are waitin for
          ;; check if the one extracted from "mailbox" is among them
          ;;
          (def result (loop [cnt 0]

            (if (< cnt  (count typ) )
              (do
                (l/og :receive "checking " cnt)
                (l/og :receive "got " mtemp)

                (l/og :receive "looking for " (nth typ cnt ))

                (if (== (aget mtemp "typ") (nth typ cnt ))
                  (do
                    ;;yay we found one now execute the function associated with it
                    (l/og :receive "found " (nth typ cnt ))
                    (l/og :receive "args " (aget  mtemp "msg"))
                    ;(l/og :receive "function " (nth  typ (+ cnt 1)))
                    (def ret (<! (apply (nth  typ (+ cnt 1)) (aget  mtemp "msg"))))
                    (l/og :receive1 "return " ret)
                    (>! rch ret)
                  )
                  (do

                  ;(>!  sendReceiveCh mtemp)
                  (recur (+ cnt 2)))
                )
              )
            )
          ))
        )
      )
   (recur)))
    )))
(defn arrsa [ sch   & typ]

  (go
    (l/og :receive "about to recieve " typ)

    (l/og :receive "about to recieve " (/ (count typ) 2))
       (if  (aget (aget  js/window "hook") typ )
           (do
        ((aget (aget  js/window "hook") typ ) msg)
             )

        (do
    ;;(>!  sendReceiveCh (js-obj "typ" 0))
   ;;loop until all messages have been checked
   ;;similar to the way erlang process checks its mailbox
    (loop []
      (l/og :receive "about to recieve in loop")
      (def mtemp (<! sch))
      (l/og :receive "about to recieve in loop" mtemp)
      (if (== (aget mtemp "typ") 0)
        (do
          ;;if we looked at all return 0
          (l/og :receive "got null")
          0
        )


        (do
          (l/og :receive "now looking at " mtemp)
          ;; out of all messages we are waitin for
          ;; check if the one extracted from "mailbox" is among them
          ;;
          (def result (loop [cnt 0]

            (if (< cnt  (count typ) )
              (do
                (l/og :receive "checking " cnt)
                (l/og :receive "got " mtemp)

                (l/og :receive "looking for " (nth typ cnt ))

                (if (== (aget mtemp "typ") (nth typ cnt ))
                  (do
                    ;;yay we found one now execute the function associated with it
                    (l/og :receive "found " (nth typ cnt ))
                    (l/og :receive "args " (aget  mtemp "msg"))
                    ;(l/og :receive "function " (nth  typ (+ cnt 1)))
                    (def ret  (apply (nth  typ (+ cnt 1)) (aget  mtemp "msg")))
                    (l/og :receive1 "return " ret)
                    ;(>! rch ret)
                  )
                  (do

                  ;(>!  sendReceiveCh mtemp)
                  (recur (+ cnt 2)))
                )
              )
            )
          ))
        )
      )
   (recur))))
    ))

(defn s [typ m]
  (go
        (l/og :send typ m)
        (>! sendReceiveCh (makeMsg typ m) )
       ; (<! receiveCH)
   )
  )
(defn si [typ m]
  (go
        (l/og :send typ m)
   (def pchannel (chan))
        (>! sendReceiveCh (makeMsg typ m pchannel) )
      (def n  (<! pchannel))
        (l/og :send "recieved" n)

   )
  )
(def order (array))
(defn sia [ & otherargs1]
  (go
   (def otherargs (into [] otherargs1))
     (l/og :sia otherargs)

   (def typ (first otherargs))
   (def mo (rest otherargs))
          (if  (aget (aget  js/window "preroutinghook") typ )
           (do
       (l/og :prehook mo)
     ;  (def m (js-obj "arr" (apply (aget (aget  js/window "preroutinghook") typ ) mo)))
       (l/og :afterhook m)
             )
            (def m mo)
)

        (l/og :invoke "mo= " mo)
        (l/og :invoke  typ m)
        (def pchannel (chan 1))
        (def sch (chan 1))

   (aset pchannel "typ" typ)
   (>! sch (js-obj "typ" typ "msg" m))
        (l/og :send "about to route" )
        (routing.routea pubsub/rrsa pchannel sch)
       (l/og :send "done routing" order)
             (if  (aget (aget  js/window "postroutinghook") typ )
           (do
               (<! (l/og :send "done routing2" ((aget (aget  js/window "postroutinghook") typ )
                                                (<! pchannel))))

             ;(def m ( msg))
             )

   (<! (l/og :send "done routing2" (<! pchannel)))
               )



        ;(l/og :send "function returned " typ)
    ;  (def n  (<! pchannel))
      ;  (l/og :send "recieved" n)

   )
  )
(defn asia [typ & m]
  (go
        (l/og :invoke  typ m)
        (def pchannel (chan 1))
        (def sch (chan 1))

   (aset pchannel "typ" typ)
   (>! sch (js-obj "typ" typ "msg" (if m  (into [] (.-arr m)) [])))
        (l/og :send "about to route" )
        (routing.routea pubsub/arrsa pchannel sch)
       (l/og :send "done routing" order)




        ;(l/og :send "function returned " typ)
    ;  (def n  (<! pchannel))
      ;  (l/og :send "recieved" n)

   )
  )
