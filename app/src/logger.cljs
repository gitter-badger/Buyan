(ns logger

  (:require
    [cljs.core.async :refer [chan close! timeout put!]]
)
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                      )
  )

(enable-console-print!)
(def tagsOn [
             :all
             :inv
             :intercom
             :blockchainHeight
             :prevblk
             :blockknown
             :makeBlock
             :heightFromBlock
             :saveBlock
             :dbput
             :makeGetBlock
             :getBlocks
             ])
(def tagsO1 [
             :invoke
             ])
(def tagsOFF [
           :receive
             ])
(def tags [:merkleRoot :dbupdate])
(defn og [type format data]
  (go
      (if (and (or (some #{type} tagsOn) (some #{:all} tagsOn)) (not (some #{type} tagsOFF)))
        (.log js/console (+ type " " format) data)

        )
  data))
;database instance
;(.enable (.-debug js/PouchDB) "*")
