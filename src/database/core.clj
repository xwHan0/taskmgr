(ns database.core
  (:require [clojure.java.jdbc :refer :all]))

(def db
  {
    :classname   "org.sqlite.JDBC"
    :subprotocol "sqlite"
    :subname     "resources/database/tmgr.sqlite"
  })  

(defn test []
 (let [rst (insert! db :tasks {:cid 1 :pid 1 :status 0 :due 10 :title "Hello"})]
  rst))

(defn test2 []
  (delete! db :tasks ["id=?" 1] ))