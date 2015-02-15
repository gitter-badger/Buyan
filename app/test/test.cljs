
 (ns testt
   (:require [cemerick.cljs.test :as t]
    [intercom :as i]
    [communications :as comm]
    [logger :as l]
    [crypto ]
    [html :as ht]
    [peerjs :refer [ peerParams]]
    [blockchain :refer [makeBlockHeader]]
    [database :refer [g p ps ]]
    [pubsub :refer [pub sub get set init]]
     [reagent.core :as reagent :refer [atom]]
    [cljs.core.async :refer [chan close! timeout put!]]

             ;[pubsub :as pubsub]
)
     (:require-macros [cemerick.cljs.test
                       :as tt
                       :refer (is deftest with-test run-tests testing test-var)] )
   )
(deftest initdbase
  ;check for root in database
  ;;clean db
  ;;initdb
  ;;get root
  ;;check if root
  )
(deftest makeBlockHeader
  ;;make header
  ;;check if it returns properly
  )
(deftest makeBlockBlock
  ;;make header
  ;;make block
  ;;check if block returned properly
  )
(deftest saveblock
  ;check for root in database
  ;;clean db
  ;;initdb
  ;;make block
  ;;save block
  ;;check block
  )
(deftest blockheight
  ;;add block
  ;;check height
  )
(deftest sha256
  ;;hash something
  ;;check hash
  )
(deftest transaction-making
  ;;feed input
  ;;make transaction
  )
(deftest merkleroot
  ;;make transactions
  ;;get fake merkleroot
  ;;check hash
  )
(deftest transaction-in-block
  ;;make transactions
  ;;add them to block
  ;;check block
  )
(deftest mineblock
  ;;feed block into miner
  ;;check mined
  )
(deftest broadcastblock
  ;;
  ;;broadcast send
  )
(deftest blocksyncEmptyBase
  ;;mock empty dbase
  ;;block recieved when database empty
  ;;send get them from root
  )
(deftest getblocks
  ;;get blockchain history *2 increments etc
  )
(deftest blocksyncLowerBase
  ;; mock database of lower height
  ;; block recieved when database is of lower height
  ;; send history
  )
(deftest sendblocks
  ;; send from last known block
  )
(deftest blocksyncHigherBaseHasBlockInDB

  )
(deftest blocksyncHigherBaseDoesNotHaveBlockInDB

  )
(deftest blocksyncForkBase

  )
 (deftest somewhat-less-wat
          ;(.log js/console js/PouchDB)
          (is (= "{}[]" (+ {} []))))

 (deftest javascript-allows-div0
          (is (= js/Infinity (/ 1 0) (/ (int 1) (int 0)))))

 (with-test
   (defn pennies->dollar-string
         [pennies]
         {:pre [(integer? pennies)]}
         (str "$" (int (/ pennies 100)) "." (mod pennies 100)))
   (testing "assertions are nice"
            (is (thrown-with-msg? js/Error #"integer?" (pennies->dollar-string 564.2)))))
