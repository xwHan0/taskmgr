(ns view.milestone
  (:require
    [clojure.core.ex :refer :all]
    [clj-time.core :as t]
    [clj-time.format :as tf]
    [database.core :as db]
    [view.util :as util]))

(def task-format (tf/formatter "yyyy-MM-dd"))

(defn- parse-date [date] (tf/parse task-format date))

(defn- in-days [start finish]
  (let [sec (t/in-minutes (t/interval start finish))]
    (/ sec 60 24)))

(defn- milestone-x [date start due size offset]
  (let [
    dates (in-days start date)
    len (* (/ dates due) size)
  ]
    (+ offset len)))

(defn- sym-tr []
  [:g#tr
    [:path {:d "M0 0 L10 0 L5 20 Z" :fill "red"}]])

(defn- sym-dcp []
  (let []
    [:g#dcp
      [:line {:x1 20 :y1 0 :x2 20 :y2 150  }]
      [:path {:d "M20 150 L40 170 L30 170 L30 210 L10 210 L10 170 L0 170 Z"}]]))

(defn- svg-tr [{:keys [x date title]}]
  [:g 
    [:text {:y 36}
      [:tspan {:x (- x 25) :dy 0} date]
      [:tspan {:x (- x 5) :dy 20} title]]
    [:use {:xlink:href "#tr" :x x :y 60}]])

(defn- svg-dcp [{:keys [x date title]}]
  [:g 
    [:text {:y 245}
      [:tspan {:x (- x 25) :dy 0} date]
      [:tspan {:x (- x 5) :dy 20} title]]
    [:use {:xlink:href "#dcp" :x x :y 10}]])

(defn- milestone-svg [milestones]
  (let [
    st (parse-date (-> milestones first :finish))
    ed (parse-date (-> milestones last :finish))
    dueTask (in-days st ed)
    milestones (-> milestones butlast)
  ] 
    [:svg {:width 1100 :height 400 :xmlns "http://www.w3.org/2000/svg" :xmlns:xlink "http://www.w3.org/1999/xlink"}
      [:defs
        [:linearGradient#orange_red {:x1 "0%" :y1 "0%" :x2 "100%" :y2 "0%"}
          [:stop {:offset "0%" :style "stop-color:rgb(255,255,0);stop-opacity:1"}]
          [:stop {:offset "100%" :style "stop-color:rgb(255,100,0);stop-opacity:1"}]]
        (sym-tr)
        (sym-dcp)]
      [:path {:d "M50 80 L350 80 L1050 80 L1050 60 L1080 100 L1050 140 L1050 120 L350 120 L50 120 Z" :fill "url(#orange_red)" :stroke "black"}]
      (for [{:keys [due title type]} milestones]
        (let [mx (milestone-x (parse-date due) st dueTask 1000 50)
              content {:x mx :date due :title title}]
          (cond (= type "tr") (svg-tr content)
                (= type "dcp") (svg-dcp content))))
      ]))

(defn page [tid]
  (let [
    milestones (db/read-milestones tid)
  ]
    (util/page
      ;JS
      []
      ;CSS
      ["css/milestone.css"]
      ;Component
      [:h2 "Milestone"]
      ; "<?xml-stylesheet href='css/milestone.css' type='text/css'?>"
      (milestone-svg milestones)
    )))
