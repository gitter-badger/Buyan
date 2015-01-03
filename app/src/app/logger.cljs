(ns app.logger)
(enable-console-print!)
(def tagsOn [:blockchain])
(defn og [type format data] 
  (if (or (some  #{type} tagsOn) (some #{:all} tagsOn))
  (.log js/console (+ type " " format) data )
))
