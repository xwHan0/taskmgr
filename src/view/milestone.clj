(ns view.milestone
  (:require
    [clojure.core.ex :refer :all]
    [view.util :as util]))

(defn- milestone-svg []
  [:svg {:width 1000 :height 400 :xmlns "http://www.w3.org/2000/svg" :xmlns:xlink "http://www.w3.org/1999/xlink"}
    [:defs
      [:linearGradient#orange_red {:x1 "0%" :y1 "0%" :x2 "100%" :y2 "0%"}
        [:stop {:offset "0%" :style "stop-color:rgb(255,255,0);stop-opacity:1"}]
        [:stop {:offset "100%" :style "stop-color:rgb(255,0,0);stop-opacity:1"}]]
      [:g#milestone
        [:path {:d "M0 0 L10 0 L5 20 Z" :fill "red"}]]]
    [:path {:d "M50 80 L850 80 L850 60 L880 100 L850 140 L850 120 L50 120 Z" :fill "url(#orange_red)" :stroke "black"}]
    [:text {:y 36}
      [:tspan {:x 125 :dy 0} "2017-12-01"]
      [:tspan {:x 145 :dy 20} "TR1"]]
    [:use {:xlink:href "#milestone" :x 150 :y 60}]
    [:text {:y 36}
      [:tspan {:x 325 :dy 0} "2017-12-01"]
      [:tspan {:x 345 :dy 20} "TR2"]]
    [:use {:xlink:href "#milestone" :x 350 :y 60}]
    [:text {:y 36}
      [:tspan {:x 625 :dy 0} "2017-12-01"]
      [:tspan {:x 645 :dy 20} "TR3"]]
    [:use {:xlink:href "#milestone" :x 650 :y 60}]
    ])

(defn page []
  (let [
        ]
    (util/page
      ;JS
      []
      ;CSS
      []
      ;Component
      [:h2 "Milestone"]
      (milestone-svg)
    )))
