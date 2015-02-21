(ns logger)
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
(def tagsOFF [
           ;  :receive
             ])
(def tags [:merkleRoot :dbupdate])
(defn og [type format data]
      (if (and (or (some #{type} tagsOn) (some #{:all} tagsOn)) (not (some #{type} tagsOFF)))
        (.log js/console (+ type " " format) data)

        )
  data)
;database instance
;(.enable (.-debug js/PouchDB) "*")
