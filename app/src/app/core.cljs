(ns app.core
  (:require
    [cljs.core.async :refer [chan close! timeout put!]]
    [servant.core :as servant]
    [servant.worker :as worker])
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]]) )


(enable-console-print!)
(def peers [])
(defn foo [] 
  (println "window loaded")


  (.log js/console "this runs in the browser")

  (def worker-count 2)
  (def worker-script "wrkr.js")
  (def servant-channel (servant/spawn-servants worker-count worker-script))

  (def channel-1 (servant/servant-thread servant-channel servant/standard-message "none" "newjob" "some root" ))
  (set! (.-type channel-1) "workerch")
  (enable-console-print!)
  (. js/console (log (THREE/Scene. )))
  (def ^:dynamic peerParams (js-obj "host" "localhost" "port" 8000 "key" "peerjs" "debug" true))
  (def id ( js/prompt "enter id"))
  (println id)
  (def start (chan))
  (def connectionch (chan))
  (def peerjs (js/Peer. id peerParams ))

  (defn onOpen [ conn]
    (println "connection opened trying to send data trough")
    (.log js/console conn)
    (set! (.-connType conn) "saltan")
    (.send conn "asd")
    (go
      (>! connectionch conn)
      )

    (println "conn: " conn)
    )

  (defn onData [read data]
    (println "data recieved" data)
    (go
      (>! read data)
      )

    )
  (defn onConnection [conn]
    (println "connection is opened now try to send something")
    (set! (.-connType conn) "tsaritsa")
    ;(.send conn "second sends something")
    (go
      (>! connectionch conn)
      )
    )

  (.on peerjs "connection" onConnection )
  (defn channelsFromConnection [conn]
    (def readc (chan 10))
    (def writec (chan 10))
    (set! (.-writec readc) writec)
    (set! (.-type readc) "readch")
    (set! (.-type writec) "writech")
    (set! (.-writec  conn) writec)
    (set! (.-readc  conn) readc)

    (set! (.-conn readc) conn)

    (set! (.-conn writec) conn)
    (.on conn "data" (partial onData readc))
    [readc writec]
    )
  (defn connectTo [id]
    (let [conn (.connect peerjs id)]
      (.on conn "open" (partial onOpen conn))

      ;(channelsFromConnection conn)

      ))
  (def empty-string "")
  (println id)
  ;(if (== id "2")
  ; (println "id = 2"))
  (def intercomMeta (js-obj 
   "id" 1
   "knownPeers" []
   ))
  (do
                    (println "about to connect from heere")
                    (def peer (connectTo "2"))
                    ;(.log js/console (nth peer 1))
                  

                    (defn lp [statea] 
                       (def gconn 1)
                          (def state statea)
                      (go (loop [state2 statea]
                               ;(>! (nth peer 1) "sending some data trough channel")
                                 (println "new iteration with state")
                                                                
                                                               (.log js/console state)
                               (def v (alts! state ))
                               (def vrecieved (nth v 0))
                               (def ch2 (nth v 1))

                               (cond 
                                 (== (nth v 1) connectionch) (do
                                                        (def gconn vrecieved)

                                                               (println "got new connection" vrecieved)
                                                               (def peerChannels  (channelsFromConnection vrecieved) )
                                                               (def state (into [] (concat state  peerChannels )))
                                                               (set! (.-knownPeers intercomMeta) (conj (.-knownPeers intercomMeta)  (.-peer vrecieved)) )
                                                               (println "new state")
                                                                
                                                               (.log js/console state)

                                                               (if (== (.-connType vrecieved) "saltan")
                                                                  (do 
                                                                    (println "saltan here")
                                                                      (>! (nth peerChannels 1) (js-obj "type" "versionSaltan" "data" (.-id intercomMeta)))
                                                                    )
                                                                  (do 
                                                                  
                                                                    (println "tsaritsa here")
                                                                    )
                                                                )
                                                               )
                                 (== (.-type ch2) "readch") (do 
                                                              (println "recieved from peer " vrecieved)
                                                              (if (== (.-type vrecieved) "versionSaltan") 
                                                                (do
                                                                (println (.-data vrecieved))
                                                                  (.log js/console "-------------------------")
                                                                  (.log js/console (.-knownPeers intercomMeta))
                                                                  (def conn (.-peer (.-conn ch2)))
                                                                  (.log js/console (.-conn ch2))
                                                                  (def filtrd (remove    #{conn}     (.-knownPeers intercomMeta)))
                                                                  (set! (.-knownPeers intercomMeta) filtrd)
                                                                  (.log js/console (into-array filtrd))
                                                                  (.log js/console (.-knownPeers intercomMeta))
                                                                  (>! (.-writec  ch2) (js-obj "type" "versionTsaritsa" "data" (.-id intercomMeta)))
                                                                  (>! (.-writec  ch2) (.stringify js/JSON (js-obj "type" "peerinfo" "data" (into-array (.-knownPeers intercomMeta) ))))
                                                                )
                                                              )
                                                              
                                                              
                                                              )
                                 (== (.-type ch2) "writech") (do
                                                             ; println vrecieved
                                                             (println "sending to peer " vrecieved)
                                                             (.log js/console (.-conn ch2))
                                                          
                                                              (.send (.-conn ch2 ) vrecieved)
                                                              )
                                 (== (.-type ch2) "workerch") (do
                                                             ; println vrecieved
                                                             (println "recieved from worker " vrecieved)
                                                             ; (.send (.-conn ch2 ) vrecieved)
                                                              )
                                 
                                 )
                           
                               (recur (do)))
                               )
                      )
                    (lp [connectionch channel-1])
                    )
  (println "Hello wor 32 d rdaldad!")
  ;(def myWorker (js/Worker. "hamiyoca/miner.js"))
  ;(def ^:dynamic o (js-obj "foo" 1 "bar" 2))
  ;(. myWorker (postMessage o))

)
(set! (.-onload js/window) foo)
; (let [v (alts!  peers )]
;    (println "recieved some data" v)
;    (.send (.-conn (nth v 1)) (nth v 0))
; )

;(println
;"The value from the first call is "
;(<! channel-1))             ;(conj state (<! connectionch))

;(println "peers" )
;(.log js/console 
;      peers)
; (println (alts!  peers ))
   ; (println "Killing webworkers")
                              ; (servant/kill-servants servant-channel worker-count)