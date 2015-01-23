 (ns mining)
 ;dispatch to thread pool for mining
 (defn mine [rootHash]
       (def chann (servant/servant-thread servant-channel servant/standard-message "none" "newjob" rootHash))

       (go

         (def v (<! chann))
         (l/og :mloop "recieved from mining" v)
         (>! hashmine (.parse js/JSON v))
         )
       )