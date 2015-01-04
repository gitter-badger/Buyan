(ns app.logger)
(enable-console-print!)
(def tagsOn [:inv :intercom :blockchainHeight :prevblk :blockknown :makeBlock :heightFromBlock])
(defn og [type format data] 
  (if (or (some  #{type} tagsOn) (some #{:all} tagsOn))
  (.log js/console (+ type " " format) data )
))
 