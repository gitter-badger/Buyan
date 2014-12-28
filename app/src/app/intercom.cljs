(ns app.intercom)
(enable-console-print!)
(def intercomState "start")

(defn onMessage [type data] (.log js/console "message in intercom\n===============" ))
