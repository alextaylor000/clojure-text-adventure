(ns text-adventures.core
  (:gen-class))

; a scene:
;   - has a name;
;   - has text;
;   - has a vector of choices (next)
;
; the scene named `first` is presented;
; the user makes a choice;
; the scene list is searched for a scene with the name of `choice`;
; that scene is presented.
(def scenes
  { :introduction { :next [:door-1 :door-2] :text "You see two doors. Which do you choose?" }
    :door-1 { :next [] :text "You're dead!" }
    :door-2 { :next [] :text "You're alive!" }
   }
)

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
  [scene-name]
  (let [scene (get-scene scenes scene-name)]
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
  ([] (prompt-scene :introduction))
  ([scene-name]
    (present-scene scene-name)
    (let [input (keyword (get-input))
          scene (get scenes scene-name)]
      (if (valid-transition? scene input)
        (do
          (println (str "You chose " input))
          (prompt-scene input))
        (do
          (println "Invalid choice")
          (prompt-scene scene-name))))))

(defn -main
  [& args]
  (println "hi")
  (prompt-scene))

