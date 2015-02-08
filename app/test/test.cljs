
 (ns testt
   (:require [cemerick.cljs.test :as t]
             [pubsub :as pubsub]
)
     (:require-macros [cemerick.cljs.test
                       :as tt
                       :refer (is deftest with-test run-tests testing test-var)] )
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