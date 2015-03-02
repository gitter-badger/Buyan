 (ns display.html
   (:require
 ;[clojure.data.json :as json]
    [logger :as l]
    [pubsub :as ps ]
     [reagent.core :as reagent :refer [atom]]
 [ajax.core :refer [GET POST]]
    [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [util :as a :refer [await sweet c ]]
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

(defn transactions[]
    (let [seconds-elapsed (atom ["none r now"])]
    (fn []

     (defn handler [response]
                             (swap! seconds-elapsed #(do [response])))
      (ps/sub "transaction" handler)




      [:ul.unstyled
        (map peer @seconds-elapsed )]))

  )
(defn peer[peer]
  [:li
  [:a.list-item
  [:div.text
   {:on-click #(go

                 (c "connectTo" 0 peer)
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
(defn timer-component []
  (let [seconds-elapsed (atom ["none r now"])]
    (fn []

      (js/setTimeout (fn[](do
                           (defn handler [response]
                             (swap! seconds-elapsed #(do response))
                            ;(.log js/console (str response))

                             )
                          (GET "http://localhost:8000/peerjs/peers"
                                     {:handler handler
                                      :response-format :json})
                      ;(.log js/console r)
                       )) 10000)
      [:div
       [:div "peers"]
      [:ul.unstyled
        (map peer @seconds-elapsed )]])))
 ;
 ;[:div
 ; [greeting "Hello world, it is now"]
 ; [clock]
 ; [color-input]]
 (defn simple-example [name desc pic extra]
       [proFile name desc pic extra])

 (defn  run [name desc pic extra ]
       (reagent/render-component (fn []

                                   [:div.crow
                                      [:div.ws-3
                                       ;{:id "overlay"
                                        ;:style {:margin "50px"}
                                       ;}
                                     [connetorForm]
                                       ]
                                     [:div.ws-3
                                       [proFile name desc pic extra]
                                     ]
                                     [:div.ws-3
                                     [timer-component]

                                     ]
                                     [:div.ws-3
                                      [transactions]
                                      ]
                                    ]
                                    )
                                 (.-body js/document)))
 (defn ui[]
   (go
        (run "Taras Bulba" "zaparozie r0x" "i4c32d4308e1fe.jpg" "- zaparozie")
   )
   )
