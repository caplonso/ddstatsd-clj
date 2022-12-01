(ns ddstatsd-clj.specs
  (:require [clojure.spec.alpha :as s]))

(s/def ::port int?)
(s/def ::host string?)

(s/def ::connection-map
  (s/keys :req-un [::host
                   ::port]))

(s/def ::metric-type #{"increment" "decrement" "gauge" "timing"})


(defn check-input
  [spec data]
  (s/valid? spec data))