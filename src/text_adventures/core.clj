(ns text-adventures.core
  (:gen-class)
  (:require [yaml.core :as yaml]))

; TODO: when should one use `nil` in Clojure, vs. an empty collection? Is it common
; to raise exceptions?
(defn get-scene
  "Gets a scene `name` from `scenes`"
  [scenes name]
  (get scenes name))

; TODO: is there a more elegant way to do this? I want to return a boolean here;
; I have to check for "not empty" right now. is there a shortcut?
(defn valid-transition?
  [scene next-name]
  (not (empty? (filter #(= next-name %) (get-in scene [:next]))))) ; this could be `get` but I want to remember how to use `get-in`

(defn next-choices
  "Return the next possible scene names from `scene`."
  [scene]
  (get scene :next))

(defn next-scene
  "Transitions from scene `original` to scene `next`.
  Returns nil when transition is invalid.

  Usage: (next-scene :first-scene :second-scene)"
  [scenes original next]
  (if-let [next-scene (valid-transition? (get-scene scenes original) next)]
    (get-scene scenes next)))

; here be side-effects
(defn present-choices
  [scene]
  (doseq [choice (get scene :next)]
    (println (str "• " (name choice))))
  (println ""))

(defn present-scene
  [scenes scene-name]
  (let [scene (get-scene scenes (keyword scene-name))]
    (println (get scene :text))
    (println "")
    (present-choices scene)))

; Stolen from Brave Clojure
(defn get-input
  "Waits for user to enter text and hit enter, then cleans the input"
  ([] (get-input ""))
  ([default]
     (let [input (clojure.string/trim (read-line))]
       (if (empty? input)
         default
         (clojure.string/lower-case input)))))

(defn prompt-scene
  ([scenes] (prompt-scene scenes :introduction))
  ([scenes scene-name]
    (present-scene scenes scene-name)
    (let [input (get-input)
          scene (get scenes scene-name)]
      (if (valid-transition? scene input)
        (do
          (println (str "You chose " input))
          (prompt-scene scenes input))
        (do
          (println (str "Invalid choice: " input))
          (prompt-scene scenes scene-name))))))

(defn load-scenes-from-yaml
  [file]
  (get (yaml/from-file file true) :scenes)) ; `true` keywordizes the YAML keys

(defn -main
  [& args]
  ; TODO: we pass `scenes` through most of the functions from here on out. I *think*
  ; this is idiomatic because we're not relying on any global variables, but I
  ; wonder if there's a better way to do something like this?
  (prompt-scene (load-scenes-from-yaml (first args))))

