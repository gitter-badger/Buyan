(ns mockdatabase)
(def mdbase (js-obj))
(defn g[kie] 
  (aget mdbase kie)
  )
(defn p[kie v]
  (aset mdbase kie v)
  )
(defn ps[kie v]
  (aset mdbase kie v)
  )
(defn update[kie v]
  (aset mdbase kie v)
  )

