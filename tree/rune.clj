(ns tree.rune
  (:use arcadia.core)
  (:import [UnityEngine  GameObject 
            Vector3      Transform 
            AudioSource]))

(def ^Double volume-mod (/ 0.25 120.))
(def ^Double pitch-mod (/ 3. 120.))

(defcomponent RuneClj
  [^boolean seen         ^AudioSource shepard
   ^Transform playerPos  ^GameObject game
   ^Int64 minDistance    ^Double stare
   ^Double pitch         ^Double volume ]
  (Awake [this] (set! minDistance 13))
  (Start [this]
    (set! playerPos (-> "Player" (object-named) (.transform)))
    (set! game (object-named "Master"))
    (set! shepard (-> "Shepard" (object-named) (get-component "AudioSource"))))
  (Update [this]
    (let [in-distance? (<= (Vector3/Distance (-> this (.transform) (.position))
                                             (.position playerPos))
                           minDistance)
          howling? (> stare 0.)
          finished? (> stare 120.)]
      (when (not howling?) 
        (set! volume (.volume shepard))
        (set! pitch (.pitch shepard)))
      (when (not seen) 
        (when finished?
          (-> game (get-component "GameClj") (.Spotted (.name this)))
          (set! seen (boolean true)))
        (let [sign (cond 
                    in-distance? 1.
                    howling? -1.
                    :else 0.)
              λ-pitch (+ pitch (* sign pitch-mod))
              λ-volume (+ volume (* sign volume-mod))
              λ-stare (+ stare sign)]
          (set! (.stare this) λ-stare)
          (set! pitch λ-pitch)
          (set! volume λ-volume)
          (set! (.pitch shepard) λ-pitch)
          (set! (.volume shepard) λ-volume))))))
