(ns app.logger)
(enable-console-print!)
;(def tagsOn [:dbname :db :all])
(def tagsOn [:intercom :mloop])
(defn og [type format data] 
  (if (or (some  #{type} tagsOn) (some #{:all} tagsOn))
  (.log js/console (+ type " " format) data )
  ))
