(ns renderStrategy
  ( :require

    [html :as html]
    )
  )

(defn run [renderer]
      ;(cond
      ;  (== renderer :html) (html/run)
      ;  true
      ;)
      (html/run)
      )