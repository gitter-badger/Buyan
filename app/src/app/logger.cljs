(ns app.logger)
(enable-console-print!)
(def tagsOn [:height])
(defn og [type format data] 
  (if (or (some  #{type} tagsOn) (some #{:all} tagsOn))
  (.log js/console (+ type " " format) data )
))
