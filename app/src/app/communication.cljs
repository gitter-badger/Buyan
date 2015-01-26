(ns communications

  (:require
        [app.intercom :as i]
        [app.logger :as l]
        [peerjs :refer [peerjs]]


    [pubsub :refer [pub sub]]
    [cljs.core.async :refer [chan close! timeout put!]]
   )
  (:require-macros [cljs.core.async.macros :as m :refer [go]]
                   [servant.macros :refer [defservantfn]])
  )
(def intercomMeta (js-obj
                    "id" 1
                    "knownPeers" []
                    "knownPeersChannels" []
                    "p2pchans" []
                    ))


(def peers [])
"


"

; channel to anounce new connectinos
(def connectionch (chan))
(defn broadcastNewBlock [blockk]
      (l/og :intercom "broadsacting new block" blockk)

      (l/og :intercom "broadsacting new block to " (.-knownPeersChannels intercomMeta))
      (go

        (doseq [peer (.-knownPeersChannels intercomMeta)]
               (l/og :intercom (+ "broadsacting new block to peer " peer " ") blockk)
               (def vectoR (array))
               (.push vectoR blockk)
               (>! peer (i/makeInv "block" vectoR)))
        )
      )

(defn connectTo [ev id]

      (l/og :connectTo  (first id) )
      (let [conn (.connect peerjs id)]
           (.on conn "open" (partial onOpen conn))

           ;(channelsFromConnection conn)

           ))
;when this user connects to someone just send connection to channel
(defn onOpen [conn]
      (l/og :conn "connection opened trying to send data trough")
      (l/og :conn conn)
      (set! (.-connType conn) "saltan")
      ;(.send conn "asd")

      ;if there is more th
      ;(if (< (.-length (.-knownPeers intercomMeta)) 0)
      ;
      ;  ;this loop is for enabling p2p communcication
      ;  (comm/startP2PCommLoop)
      ;  )
      ;(pub "saltanConnection" conn)
      ;
      ;
      (go
        (>! connectionch conn)
        )

      (l/og :conn "conn: " conn)
      )

(defn setIntercomState [conn state]
      (set! (.-intercomstate conn) state)
      )

(defn getIntercomState [conn]
      (.-intercomstate conn)
      )
(defn onConnection [conn]
      (l/og :conn "connection is opened now try to send something")
      (set! (.-connType conn) "tsaritsa")
      ;(.send conn "second sends something")
      ;(pub "tsaritsaConnection" conn)
      (go
        (>! connectionch conn)
        )
      )
;when peerjs sends data just send message to channel
(defn onData [read data]
      (l/og :conn "data recieved" data)
      ;(pub "peerdata" data)
      (go
        (>! read data)
        )

      )
(defn channelsFromConnection [conn]
      (def readc (chan 10))
      (def writec (chan 10))
      (set! (.-writec readc) writec)
      (set! (.-type readc) "readch")
      (set! (.-type writec) "writech")
      (set! (.-writec conn) writec)
      (set! (.-readc conn) readc)

      (set! (.-conn readc) conn)

      (set! (.-conn writec) conn)
      (.on conn "data" (partial onData readc))
      [readc writec]
      )



;this loop enables p2p communication
(defn startP2PCommLoop []
      ;listen on messages and send them where they need to be sent
      (go (loop [state [connectionch]]
                ;(>! (nth peer 1) "sending some data trough channel")
                (l/og :p2pCommLoop "new iteration with state")

                (l/og :p2pCommLoop "state " state)

                ;listen on channels from vector
                (def v (alts! state))

                ;get value
                (def vrecieved (nth v 0))
                ;get channel that received value
                (def ch2 (nth v 1))

                (cond

                  (== (nth v 1) connectionch) (def state (into [] (concat state (onNewConnection vrecieved))))
                  ;channel from some peer that recieves data from peer
                  (== (.-type ch2) "readch") (do
                                               (l/og :p2ploop "recieved from peer " vrecieved)
                                               (if (== (.-type vrecieved) "json")
                                                 (def vrecieved (.parse js/JSON (.-data vrecieved)))
                                                 )
                                               (set! (.-peer vrecieved) (.-writec ch2))
                                               (i/intercomstatemachine (getIntercomState (.-conn ch2)) vrecieved)
                                               ;;(i/onMessage (.-writec ch2) (.-type vrecieved) (.-data vrecieved))

                                               )
                  ;channel from some peer that recieves data to be sent to that peer(wrapper for peerjs send to peer)
                  (== (.-type ch2) "writech") (do
                                                ; println vrecieved
                                                (l/og :mloop "sending to peer " vrecieved)
                                                (l/og :mloop "connection being sent to " (.-conn ch2))

                                                (.send (.-conn ch2) vrecieved)
                                                )

                  ;recieves work from miners

                  ; recieves transactions)
                  )
                (recur [connectionch ] ))))
(defn onNewConnection [message]
      (def gconn message)

      (setIntercomState message "start")
      (l/og :mloop "got new connection" message)
      ;make async channels that we can use for reading and writing to send data to peers
      ;instead of using peerjs functions just send to async channel and p2p loop will read and send
      (def peerChannels (channelsFromConnection message))


      ;store peer ids in known peers array of intercom meta object
      (set! (.-knownPeers intercomMeta) (conj (.-knownPeers intercomMeta) (.-peer message)))


      (l/og :mloop "adding w channel to kpeers " (nth peerChannels 1))

      ;these are channels which one can use to send stuff to peers
      ;(nth peerChannels 1) is write chan
      ;(nth peerChannels 0) is read chan
      ;we put them in p2p2chans array of intercom meta to enable p2p loop with channels
      (set! (.-p2pchans intercomMeta) (concat (.-p2pchans intercomMeta)  peerChannels))

      ;this array to enable easier broadcast
      ;so we put write channels here to send stuff to those channels once we want to broadcast stuff
      (set! (.-knownPeersChannels intercomMeta) (conj (.-knownPeersChannels intercomMeta) (nth peerChannels 1)))

      (l/og :mloop "new state")
      ;(i/onMessage (nth peerChannels 1) "conn" vrecieved)

      ; (i/startIntercomLoop)
      peerChannels
      )
(defn onBlockMined [message]
      ; println vrecieved
      (l/og :mloop "recieved from worker " vrecieved)
      (def blockk (<! (makeBlock vrecieved)))


      ;(l/og :blockchain "just made new block " blockk)
      ;(<! (makeBlock vrecieved)
      ;(blockchain/addToChain blockk)
      ;TODO anounce to peers
      (l/og :inv "hash to get " (.-hash blockk))
      (<! (saveBlock dbase blockk))
      (def gotFromHash (<! (db/g (.-hash blockk))))
      ;(def gotFromHash )
      ;(<! (saveBlock dbase blockk))
      ;(l/og :inv "got from hash " gotFromHash)

      (broadcastNewBlock gotFromHash)
      )
(defn onTransaction [message]
      ; println vrecieved
      (l/og :mloop "recieved new transaction " vrecieved)
      ;put it in mempool
      ;send mempool to mining
      ;this might change
      (blockchain/sha256 vrecieved)
      ;(>! channel-1 vrecieved)
      ; (.send (.-conn ch2 ) vrecieved)
      )

(defn onCrypto [message]
      ; println vrecieved
      (l/og :mloop "recieved from crypto " message)
      (l/og :mloop "mempoll = " blockchain/memPool)
      (l/og :mloop (aget message "type"))
      (if (== (aget vrecieved "type") "fmr") (do
                                               (l/og :mloop "merkle root " message)
                                               (mine (aget message "value"))
                                               )
                                             )
      (if (> (count blockchain/memPool) 3)

        (l/og :mloop "calculating hash of transactions(not merkle root now) %s"
              (blockchain/merkleRoot blockchain/memPool)))

      )

(defn setupComm []
(sub "blockMined" onBlockMined)
(sub "crypto" onCrypto)
(sub "transaction" onTransaction)
(sub "newConnection" onNewConnection)
)