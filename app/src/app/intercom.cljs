(ns app.intercom
(:require
    [app.logger :as l]))
(enable-console-print!)
(def intercomState "start")

(defn onMessage [type data] (l/og :intercom "message %s in intercom" data ))
