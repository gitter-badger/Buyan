(ns app.logger)
(enable-console-print!)
(def tagsOn [:mloop])
(defn og [type format data] 
  (if (or (some  #{type} tagsOn) (some #{:all} tagsOn))
  (.log js/console format data )
  ))