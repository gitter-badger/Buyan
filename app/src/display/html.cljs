 (ns display.html
   (:require
 ;[clojure.data.json :as json]
    [logger :as l]
    [pubsub :as ps ]
   [conf]
     [reagent.core :as reagent :refer [atom]]
 [ajax.core :refer [GET POST]]
    [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [util :as a :refer [await sweet c ac]]
                   [servant.macros :refer [defservantfn]])
  )

 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

 (def timer (atom (js/Date.)))
 (def time-color (atom "#f34"))

 (defn update-time [time]
       ;; Update the time every 1/10 second to be accurate...
       (js/setTimeout #(reset! time (js/Date.)) 100))

 (defn greeting [message]
       [:h1 message])
(defn proFile [name desc pic extra]
      [:div
       {:style {:width "200px"}
        }
[:ul.unstyled
[:li
 [:a.list-item
 [:div.pull-left
[:div.circle
 [:img {:src pic :style {:width "40px" :height "40px"}}]
 ]

  ]
 [:div.text
  [:div desc
  [:br]
  [:span.small name
   [:span.text-grey extra]
   ]
  ]]

 ]
 ]]
       ]
      )

 (defn clock []
       (update-time timer)
       (let [time-str (-> @timer .toTimeString (clojure.string/split " ") first)]
            [:div.example-clock
             {:style {:color @time-color}}
             time-str]))

 (defn color-input []
       [:div.color-input
        "Time color: "
        [:input {:type "text"
                 :value @time-color
                 :on-change #(reset! time-color (-> % .-target .-value))}]])
 ;
 ;[:div.inputs
 ; {:style {:margin "50px"}}
 ; [:div.list-group
 ;  [:div.list-group-item
 ;   [:div.row-picture
 ;    [:img.circle
 ;     { :src pic
 ;      }
 ;     ]
 ;    ]
 ;   [:div.row-content
 ;    [:h4.list-group-item-heading
 ;     name]
 ;    [:p.list-group-item-text
 ;     desc
 ;
 ;     ]
 ;    ]
 ;   ]
 ;  ]]




(defn connetorForm []

   [:div
    {:class "inputs"}
    [:input.form-control {:placeholder "label" :type "text"}]
    [:button {:on-click #(.trigger (js/$ js/document) "cleandb")} "cleand"]
    [:input.form-control {:placeholder "label" :type "text" :id "id"}]
    [:button {:on-click #(.trigger (js/$ js/document) "setid" (.val (js/$ "#id")))} "setid"]
    [:br]

    [:input.form-control {:placeholder "label" :type "text"}]
    [:button {:on-click #(.trigger (js/$ js/document) "connectTo" (.val (js/$ "#idt")))} "connect"]
    [:button {:on-click #(.trigger (js/$ js/document) "flushdb" 0)} "flushdb"]
    [:button {:on-click #(.trigger (js/$ js/document) "dumpdb" 0)} "dumpdb"]
    [:br]
    [:button {:on-click #(.trigger (js/$ js/document) "transaction" (.getTime (js/Date.)))} "transact"]
    ]



  )
(defn connection[peer]
  [:li
  [:a.list-item
  [:div.text
   ;{:on-click #(go

                ; (c "connectTo" 0 peer)
    ;            )}
   [:span
   (.-peer peer)
    ]
   [:br]
   [:span.text-grey.small
     (->
       peer
       .-peerConnection
       .-remoteDescription
       .-sdp
       (.split  " ")

       (aget 5)
        (.split "\n")
        (aget 0)
     )
  ;(.-sdp (.-remoteDescription (.-peerConnection peer)))
    ]
   ]
  ]
   ]
   )
(defn messag[m]


  (proFile (.-id m) (.-data m) )
   )
(defn messages[]
    (let [state (atom []) ]
           (defn handler [response]
       (.log js/console "message " response)
                             (swap! state #(conj @state  response)))
      (ps/sub "peermessage" handler)
    (fn []



      [:span
       [:div "messages"]
      [:ul.unstyled
        (map messag @state )]
       ]
      ;[messag]
      ))
)
(defn connected[]
    (let [state (atom []) ]
    (fn []

     (defn handler [response]
                             (swap! state #(conj @state  response)))
      (ps/sub "peer" handler)
      [:span
       [:div "connections made"]
      [:ul.unstyled
        (map connection @state )]
       ]
      ;[messag]
      ))

  )
(defn transactions[]
    (let [seconds-elapsed (atom ["none r now"])]
    (fn []

     (defn handler [response]
                             (swap! seconds-elapsed #(do [response])))
      (ps/sub "transaction" handler)




      [:ul.unstyled
        (map peer @seconds-elapsed )]))

  )
(defn peerr[peer]



  (ps/pub "newpeer" peer)
   [:li
  [:a.list-item
  [:div.text
   {:on-click #(go

                 (c "connectTo"  peer)
                )}
   [:span
   peer
    ]
   [:br]
   [:span.text-grey.small

    ]
   ]
  ]
   ]
  )
(defn tbox[]

  (let [seconds-elapsed (atom "text")]
    (fn []


                           (defn save [ev]
                             (go
                             (swap! seconds-elapsed #(.val (js/$. "#inputbx")))
                             (.log js/console (str @seconds-elapsed))
                              (ps/pub "peermessage" (js-obj "id" "me" "data" @seconds-elapsed))
                             (ac "broadcast" (str @seconds-elapsed))
                             )
                           )

      [:div
       [:div "entr messag"]
      [:input {
               :id "inputbx"
               :on-key-up  #(case (.-which %)
                                      13 (save)
                                      27 (stop)
                                      nil)
               }

        ]])))
(defn peers []
  (let [seconds-elapsed (atom ["none r now"])]
          (js/setTimeout (fn[](do
                           (defn handler [response]

                             (swap! seconds-elapsed #(do response))
                            ;(.log js/console (str response))

                             )
                          (GET (str "http://" conf.signalingd "/prokletdajepapa/peers")
                                     {:handler handler
                                      :response-format :json})
                      ;(.log js/console r)
                       )) 1000)
    (fn []
      (js/setTimeout (fn[](do
                           (defn handler [response]
                             (swap! seconds-elapsed #(do response))
                            ;(.log js/console (str response))

                             )
                          (GET (str "http://" conf.signalingd "/prokletdajepapa/peers")
                                     {:handler handler
                                      :response-format :json})
                      ;(.log js/console r)
                       )) conf.interval)

      [:div
       [:div "peers"]
      [:ul.unstyled
        (map peerr @seconds-elapsed )]])))
 ;
 ;[:div
 ; [greeting "Hello world, it is now"]
 ; [clock]
 ; [color-input]]
;[:div.ws-3
;[proFile name desc pic extra]
;]
 (defn simple-example [name desc pic extra]
       [proFile name desc pic extra])

 (defn  run [name desc pic extra ]
       (reagent/render-component (fn []
                                   [:div.container.fluid
                                    [:div.ws-2
                                       [:div.crow

                                        [:div.ws-12
                                          [connected]
                                        ]
                                        [:div.ws-12
                                         [peers]
                                       ]

                                       ]
                                     ]
                                   [:div.col-md-8.container.fluid


                                    [:div.col-md-8
                                     [:div.container
                                     [:div.col-md-6
                                       ;{:id "overlay"
                                        ;:style {:margin "50px"}
                                       ;}
                                     ;  [connetorForm]
                                     ]

                                        [:div.col-md-6
                                          [messages]
                                        ]
                                 ]

                                    ]

                                 ;          [:div.ws-12

                                   ;       [:div
                                ;      [transactions]
                                ;     ]
                               ;

                                ;     ]

                                    ]
                                      [:div.emitter
                                     [tbox]
                                    ]
                                  ]


                                    )
                                 (.-body js/document)))
 (defn ui[]
   (go
        (run "Taras Bulba" "zaparozie r0x" "i4c32d4308e1fe.jpg" "- zaparozie")
   )
   )
