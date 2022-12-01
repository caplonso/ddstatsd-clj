(ns ddstatsd-clj.core
  (:require [clojure.string :as str]
            [ddstatsd-clj.network :refer [send-packet]]
            [ddstatsd-clj.specs :as spec])
  (:gen-class))


(defn- get-type-token
  [type-name]
  (case  type-name
      "increment" "c"
      "decrement" "c"
      "timing" "ms"
      "gauge" "g"
      "default"))


(defn- format-tags
  [tags]
  (str/join "," (map (fn [[key value]] (str/join ":" [(name key) value])) tags)))

(defn- format-message
  [metric-name type value & [tags]]
  (if-not tags
    (format "%s:%d|%s" metric-name value (get-type-token type))
    (format "%s:%d|%s|#%s" metric-name value (get-type-token type) (format-tags tags))))

(defn send-metric
  [connection-map metric-name type value & [tags]]
  {:pre [(spec/check-input ::spec/metric-type type)
         (spec/check-input ::spec/connection-map connection-map)
         (when tags
           (spec/check-input ::spec/tags tags))]}
    (send-packet connection-map (format-message metric-name type value tags)))
