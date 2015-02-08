(ns communications

  (:require
    [intercom :as i]
    [intercomMake :as im]
    [database :as db]
    [logger :as l]
    [blockchain :as b]
    [peerjs :refer [peerjs]]
    [crypto :refer [sha256]]

    [mining :refer [mine]]


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
               (>! peer (im/makeInv "block" vectoR)))
        )
      )



;this one will send message to peer
(defn sendmsg [peer type msg]

      (l/og :sendmsg (+ "sending " type) msg)

      (l/og :sendmsg "peer " peer)
      (go
        (>! peer (js-obj "type" type "msg" msg))
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
      (def stated [connectionch])
      (go (loop []
                ;(>! (nth peer 1) "sending some data trough channel")
                (l/og :p2pCommLoop "new iteration with state")

                (l/og :p2pCommLoop "state " stated)

                ;listen on channels from vector
                (def v (alts! stated))
                (l/og :p2ploop "got from state" v)
                ;get value
                (def vrecieved (nth v 0))
                ;get channel that received value
                (def ch2 (nth v 1))

                (cond

                  (== (nth v 1) connectionch) (do (def stated (into [] (concat stated (onNewConnection vrecieved))))
                                                  (def stat (i/getIntercomState vrecieved))

                                                  (l/og :p2ploop "intercom state" stat)
                                                  (i/intercomstatemachine vrecieved (im/makeConn vrecieved)))
                  ;channel from some peer that recieves data from peer
                  (== (.-type ch2) "readch") (do
                                               (l/og :p2ploop "recieved from peer " vrecieved)
                                               (if (== (.-type vrecieved) "json")
                                                 (def vrecieved (.parse js/JSON (.-data vrecieved)))
                                                 )
                                               (set! (.-peer vrecieved) (.-writec ch2))
                                               (i/intercomstatemachine  (.-conn ch2) vrecieved)
                                               ;;(i/onMessage (.-writec ch2) (.-type vrecieved) (.-data vrecieved))

                                               )
                  ;channel from some peer that recieves data to be sent to that peer(wrapper for peerjs send to peer)
                  (== (.-type ch2) "writech") (do
                                                ; println vrecieved
                                                (l/og :p2ploop "sending to peer " vrecieved)
                                                (l/og :p2ploop "connection being sent to " (.-conn ch2))

                                                (.send (.-conn ch2) vrecieved)
                                                )

                  ;recieves work from miners

                  ; recieves transactions)
                  )
                (recur))))
(defn onNewConnection [message]
      (def gconn message)

      (intercom/setIntercomState message "start")
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
      (set! (.-p2pchans intercomMeta) (concat (.-p2pchans intercomMeta) peerChannels))

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
      (go
      (l/og :onBlockMined "recieved from worker " message)
      (def blockk (<! (b/makeBlock message)))


      (l/og :onBlockMined  "just made new block " blockk)
      ;(<! (makeBlock vrecieved)
      ;(b/addToChain blockk)
      ;TODO anounce to peers
      (l/og :message "hash to get " (.-hash blockk))
      (<! (b/saveBlock app.pouchDB.dbase blockk))
      (def gotFromHash (<! (db/g (.-hash blockk))))
      ;(def gotFromHash )
      ;(<! (saveBlock dbase blockk))
      (l/og :inv "got from hash " gotFromHash)

      (broadcastNewBlock gotFromHash)
      ))
(defn onTransaction [message]
      ; println vrecieved
      (l/og :onTransaction "recieved new transaction " message)
      ;put it in mempool
      ;send mempool to mining
      ;this might change
      (sha256 message)
      ;(>! channel-1 vrecieved)
      ; (.send (.-conn ch2 ) vrecieved)
      )

(defn onCrypto [message]
      ; println vrecieved
      (l/og :onCrypto "recieved from crypto " message)
      (l/og :onCrypto "mempoll = " b/memPool)
      (b/addTransactionToMemPool message)
      (l/og :onCrypto (aget message "type"))
      (if (== (aget message "type") "fmr") (do
                                               (l/og :onCrypto "merkle root " message)
                                               (mine (aget message "value"))
                                               )
                                             )
      (if (> (count b/memPool) 3)
        ;fake merkle root(it works just it is not merkle root)
        (go
        (def fmroot (<! (crypto/merkleRoot b/memPool)))

        (l/og :onCrypto "calculating hash of transactions %s"
              fmroot)
        (mine fmroot)
        ))

      )

;this function sets up in parts internal message passing mechanism between modules
(defn setupComm []
      (sub "blockMined" onBlockMined)
      (sub "crypto" onCrypto)
      (sub "transaction" onTransaction)
      (sub "newConnection" onNewConnection)
      )