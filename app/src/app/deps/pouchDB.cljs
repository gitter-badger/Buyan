(ns pouchDB)
(def dbase (js/PouchDB. "dbname"))
;PouchDB.destroy('dbname', function(err, info) { });