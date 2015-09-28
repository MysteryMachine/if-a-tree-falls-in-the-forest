(ns tree.shepard
  (:use arcadia.core)
  (:import [UnityEngine  GameObject 
            AudioSource  Animator
            Application]))

(defprotocol ShepardEvents
  (Attack [this])
  (Activate [this])
  (BackToStart [this]))

(defcomponent ShepardClj
  [^AudioSource shepard  ^Animator monster
   ^boolean active       ^Double volume
   ^Double pitch]
  (Start [this]
    (set! (.volume this) (* 4. 0.018))
    (set! (.pitch this) 1.)
    (set! shepard (-> "Shepard" (object-named) (get-component "AudioSource")))
    (set! monster (-> "Monster" (object-named) (get-component "Animator"))))
  (Update [this]
    (when active
      (set! volume (+ volume (/ 0.3 360.)))
      (set! pitch (+ pitch (/ 4. 360.)))
      (when (< pitch 4.) (set! (.pitch shepard) pitch))
      (when (< volume 0.3) (set! (.volume shepard) volume))))
  ShepardEvents 
  (Activate [this] (set! active (boolean true)))
  (Attack [this] (.SetTrigger monster "attack"))
  (BackToStart [this] (Application/LoadLevel 0)))
