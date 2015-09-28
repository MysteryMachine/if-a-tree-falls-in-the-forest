(ns tree.change-level
  (:use arcadia.core)
  (:import [UnityEngine  Application 
            Input        KeyCode
            GameObject]))

(def levels {:menu 0 :level 1})

(defprotocol ChangeLevelEvents
  (LoadMenu ^void [this])
  (LoadLevel ^void [this])
  (FadeOut ^void [this]))

(defcomponent ChangeLevelClj
  [^bool mainMenu]
  (Update [this]
    (if (Input/GetKeyDown KeyCode/Escape)
      (if mainMenu
        (Application/Quit)
        (-> (GameObject/Find "Main Menu")
            (get-component "Animator")
            (.SetTrigger "mainMenu")))))
  ChangeLevelEvents
  (LoadMenu [this] (Application/LoadLevel (:menu levels)))
  (LoadLevel [this] (Application/LoadLevel (:level levels)))
  (FadeOut [this] (-> this 
                      (get-component "Animator")
                      (.SetTrigger "fadeOut")))) 
