(ns pubsub
  (:require [cljs.core.async :refer [chan close! timeout put!]])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]]))
(def proxychan (chan))
(def subs (js-obj))
(defn sub [typ fun] (aset subs typ fun))
(defn pub [typ msg] (go (>! proxychan (js-obj "typ" typ "msg" msg))))
(defn initpubsub [] (go (loop [] (def m (<! proxychan)) ((aget subs (aget m "typ")) (aget m "msg"))(recur ))))