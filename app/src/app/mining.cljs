(ns mining)

;channel that will receive results from mining
(def hashmine (chan))
;setting type on the channel object so it is possible to distinguish it from other channels
(set! (.-type hashmine) "workerch")

;dispatch to thread pool for mining
(defn mine [rootHash]
      (def chann (servant/servant-thread servant-channel servant/standard-message "none" "newjob" rootHash))

      (go

        (def v (<! chann))
        (l/og :mloop "recieved from mining" v)
        (>! hashmine (.parse js/JSON v))
        )
      )