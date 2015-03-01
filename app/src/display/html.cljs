 (ns display.html
   (:require

    [logger :as l]
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
(defn timer-component []
  (let [seconds-elapsed (atom 0)]
    (fn []

      (js/setTimeout fn[](do
                           (defn handler [response]
                             (swap! seconds-elapsed #(str response))
                            (.log js/console (str response)))
                          (GET "http://localhost:8000/peerjs/peers"
                                     {:handler handler
                                      :response-format :json})
                      ;(.log js/console r)
                       ) 1000)
      [:div
       "Seconds Elapsed: " @seconds-elapsed])))
 ;
 ;[:div
 ; [greeting "Hello world, it is now"]
 ; [clock]
 ; [color-input]]
 (defn simple-example [name desc pic extra]
       [proFile name desc pic extra])

 (defn  run [name desc pic extra ]
       (reagent/render-component (fn []
                                   [:div
                                   [:div
                                   [proFile name desc pic extra]
                                   ]
                                   [:div
                                    [timer-component]
                                   ]
                                    ]
                                    )
                                 (.-body js/document)))
 (defn ui[]
   (go
        (run "Taras Bulba" "zaparozie r0x" "i4c32d4308e1fe.jpg" "- zaparozie")
   )
   )
