(ns peerjs)

;data for peer connection
(def ^:dynamic peerParams (js-obj "host" "localhost" "port" 8000 "key" "peerjs" "debug" true))

;(def peer (connectTo "2"))
;peerjs object
