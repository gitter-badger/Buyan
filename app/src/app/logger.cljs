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
(def tags [:merkleRoot :dbupdate])
(defn og [type format data]
      (if (or (some #{type} tagsOn) (some #{:all} tagsOn))
        (.log js/console (+ type " " format) data)

        )
  data)
;database instance
;(.enable (.-debug js/PouchDB) "*")
