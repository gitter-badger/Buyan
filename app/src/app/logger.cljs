(ns app.logger)
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
(defn og [type format &data]
      (if (or (some #{type} tagsOn) (some #{:all} tagsOn))
        (.log js/console (+ type " " format) data)

        ))
;database instance
;(.enable (.-debug js/PouchDB) "*")