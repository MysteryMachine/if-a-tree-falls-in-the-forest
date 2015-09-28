(ns tree.game
  (:use arcadia.core)
  (:import [UnityEngine  Application
            GameObject   Animator
            Mathf        Cursor
            CursorLockMode]))

(defprotocol GameEvents
  (Run [this])
  (Spotted [this rune]))

(defcomponent GameClj
  [^bool owlSpotted     ^bool deerSpotted
   ^bool teethSpotted   ^bool coyoteSpotted
   ^bool running        ^Animator flash
   ^Animator run        ^Double shepardVolume
   ^Double shepardTime  ^GameObject animalSeen
   ^AudioSource shepard]

  (Start [this]
    (set! flash (-> (object-named "Flash") (get-component "Animator")))
    (set! shepard (-> (object-named "Shepard") (get-component "AudioSource")))
    (set! shepardVolume (.volume shepard))
    (set! run (-> (object-named "Run") (get-component "Animator")))
    (set! Cursor/visible false)
    (set! Cursor/lockState CursorLockMode/Locked))
  (FixedUpdate [this]
    (when (or (not running) (> shepardTime 0))
      (when (> shepardTime 0) (set! shepardTime (dec shepardTime)))
      (when (= shepardTime 210) (GameObject/Destroy animalSeen))
      (when (< shepardTime 210)
        (set! (.volume shepard) (Mathf/Lerp shepardVolume (.volume shepard) (/ shepardTime 120.)))
        (set! (.pitch shepard) (Mathf/Lerp 1. (.pitch shepard) (/ shepardTime 120.))))
      (when (and coyoteSpotted deerSpotted owlSpotted teethSpotted) (Run this))))
  GameEvents
  (Run [this]
    (.SetTrigger run "running")
    (set! running (boolean true)))
  (Spotted [this rune]
    (set! animalSeen (object-named rune))
    (case rune
      "Deer"   (set! deerSpotted (boolean true))
      "Coyote" (set! coyoteSpotted (boolean true))
      "Owl"    (set! owlSpotted (boolean true))
      "Teeth"  (set! teethSpotted (boolean true)))
    (.SetTrigger flash "flash")
    (set! shepardVolume (+ shepardVolume 0.018))
    (set! shepardTime 240.)))
 
