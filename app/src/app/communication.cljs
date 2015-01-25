(ns communication)
(def intercomMeta (js-obj
                    "id" 1
                    "knownPeers" []
                    "knownPeersChannels" []
                    ))


(def peers [])


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
(defn connectTo [id]
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
      (go
        (>! connectionch conn)
        )

      (l/og :conn "conn: " conn)
      )
;when peerjs sends data just send message to channel
(defn onData [read data]
      (l/og :conn "data recieved" data)
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
(defn onConnection [conn]
      (l/og :conn "connection is opened now try to send something")
      (set! (.-connType conn) "tsaritsa")
      ;(.send conn "second sends something")
      (go
        (>! connectionch conn)
        )
      )
(defn mainLoop [statea]
      (def gconn 1)
      (def state statea)
      ;listen on messages and send them where they need to be sent
      (go (loop [state2 statea]
                ;(>! (nth peer 1) "sending some data trough channel")
                (l/og :mloop "new iteration with state")

                (l/og :mloop "state " state)
                ;listen on channels from vector

                (def v (alts! state))
                ;get value
                (def vrecieved (nth v 0))
                ;get channel that received value
                (def ch2 (nth v 1))

                (cond
                  ;channel that gets new connections
                  (== (nth v 1) connectionch) (do
                                                (def gconn vrecieved)

                                                (l/og :mloop "got new connection" vrecieved)
                                                (def peerChannels (channelsFromConnection vrecieved))
                                                ;add channels for reading and writing to this new connection to the vector of channels we listen
                                                (def state (into [] (concat state peerChannels)))
                                                (set! (.-knownPeers intercomMeta) (conj (.-knownPeers intercomMeta) (.-peer vrecieved)))
                                                (l/og :mloop "adding w channel to kpeers " (nth peerChannels 1))
                                                (set! (.-knownPeersChannels intercomMeta) (conj (.-knownPeersChannels intercomMeta) (nth peerChannels 1)))
                                                (l/og :mloop "new state")
                                                (i/onMessage (nth peerChannels 1) "conn" vrecieved)

                                                )
                  ;channel from some peer that recieves data from peer
                  (== (.-type ch2) "readch") (do
                                               (l/og :mloop "recieved from peer " vrecieved)
                                               (if (== (.-type vrecieved) "json")
                                                 (def vrecieved (.parse js/JSON (.-data vrecieved)))
                                                 )
                                               (set! (.-peer vrecieved) (.-writec ch2))
                                               (i/onMessage (.-writec ch2) (.-type vrecieved) (.-data vrecieved))

                                               )
                  ;channel from some peer that recieves data to be sent to that peer(wrapper for peerjs send to peer)
                  (== (.-type ch2) "writech") (do
                                                ; println vrecieved
                                                (l/og :mloop "sending to peer " vrecieved)
                                                (l/og :mloop "connection being sent to " (.-conn ch2))

                                                (.send (.-conn ch2) vrecieved)
                                                )
                  ;recieves work from miners
                  (== (.-type ch2) "workerch") (do
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

                                                 ; (.send (.-conn ch2 ) vrecieved)
                                                 )
                  ;recieves results from browser crypto functions
                  (== (.-type ch2) "cryptoch") (do
                                                 ; println vrecieved
                                                 (l/og :mloop "recieved from crypto " vrecieved)
                                                 (l/og :mloop "mempoll = " blockchain/memPool)
                                                 (l/og :mloop (aget vrecieved "type"))
                                                 (if (== (aget vrecieved "type") "fmr") (do
                                                                                          (l/og :mloop "merkle root " vrecieved)
                                                                                          (mine (aget vrecieved "value"))
                                                                                          )
                                                                                        )
                                                 (if (> (count blockchain/memPool) 3)

                                                   (l/og :mloop "calculating hash of transactions(not merkle root now) %s" (blockchain/merkleRoot blockchain/memPool)))

                                                 )
                  ; recieves transactions
                  (== (.-type ch2) "transactionch") (do
                                                      ; println vrecieved
                                                      (l/og :mloop "recieved new transaction " vrecieved)
                                                      ;put it in mempool
                                                      ;send mempool to mining
                                                      ;this might change
                                                      (blockchain/sha256 vrecieved)
                                                      ;(>! channel-1 vrecieved)
                                                      ; (.send (.-conn ch2 ) vrecieved)
                                                      )
                  )

                (recur (do)))
          )
      )

