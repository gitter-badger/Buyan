(ns pubsub
  (:require
    [cljs.core.async :refer [chan close! timeout put!]]

    [logger :as l]
    )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
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

(defn set [what] (go (<! proxychan2)
                          (>! proxychan2 what)

                          )

  )

(def subs (js-obj))
(defn sub [typ fun] (aset subs typ fun))
(defn pub [typ msg]
      (l/og :pub "pubing " (+ typ " " msg))
      (go (>! proxychan (js-obj "typ" typ "msg" msg)))
      )

(defn makeMsg [typ m pchannel]
  (js-obj "typ" typ "msg" m)
  )
(defn initpubsub []
      (go
        ;(>!  sendReceiveCh (makeMsg "maker" 0))
     )
      )
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
    )

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

(defn sia [typ & m]
  (go
        (def pchannel (chan 1))
        (def sch (chan 1))
        (>! sch (js-obj "typ" typ "msg" (if m  (into [] (.-arr m)) [])))
        (routing.routea pchannel sch)
        (l/og :send "function returned " typ)
      (def n  (<! pchannel))
        (l/og :send "recieved" n)
n
   )
  )
