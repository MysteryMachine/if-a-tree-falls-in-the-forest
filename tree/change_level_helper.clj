(ns tree.change-level-helper
  (:use arcadia.core)
  (:import [UnityEngine GameObject]))

(defprotocol ChangeLevelHelperEvents
  (Activate [this]))

(defcomponent ChangeLevelHelperClj
  [^GameObject controller]
  ChangeLevelHelperEvents
  (Activate [this] (.SetActive controller true)))

