(ns view.util
  (:require [hiccup.page :refer :all]))

(defn page
  "把componments填入页面主框架，并调用html函数形成页面。"
  [jss css & componments]
  (html5
    [:head
      (for [j jss]
        [:script {:type "text/javascript" :src j}])
      (for [c css]
        [:link {:rel "stylesheet" :type "text/css" :href c}])
      ]
     (apply vector :body componments)))
