(ns peerjs)

;data for peer connection
(def ^:dynamic peerParams (js-obj "host" "localhost" "port" 8000 "key" "peerjs" "debug" true))

;(def peer (connectTo "2"))
;peerjs object
(defn setID [ev id ]
      (def peerjs (js/Peer. (nth id 2)  p/peerParams))
      (.on peerjs "connection" onConnection)
      )
