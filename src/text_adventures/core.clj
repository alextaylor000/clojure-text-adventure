(ns text-adventures.core
  (:gen-class))

(def scenes
  [{:name "a" :next ["b"] :text "first scene"}
   {:name "b" :next ["c"] :text "second scene"}
   {:name "c" :next ["d e"] :text "third scene"}
   {:name "d" :next [] :text "fourth-1 scene"}
   {:name "e" :next [] :text "fourth-2 scene"}])
; a scene:
;   - has a name;
;   - has text;
;   - has a vector of choices (next)
;
; the scene named `first` is presented;
; the user makes a choice;
; the scene list is searched for a scene with the name of `choice`;
; that scene is presented.

(defn get-scene
  [name]
  (first
    (filter #(= name (get % :name)) scenes)))

(defn present-scene
  [scene-name]
  (let [scene (get-scene scene-name)]
    (println (get scene :text))
    (println "")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "hi")
  (loop
    [x "a"]
    (when
      (not= "quit" x)
      (recur
        (do
          (print "> ")
          (flush)
          (present-scene x)
          (read-line)))))
  (println "bye"))

