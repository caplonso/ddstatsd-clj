(ns ddstatsd-clj.core
  (:require [ddstatsd-clj.network :refer [send-packet]]
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

(defn send-metric
  [connection-map metric-name type value]
  {:pre [(spec/check-input ::spec/metric-type type)
         (spec/check-input ::spec/connection-map connection-map)] }
  (send-packet {:host "localhost" :port 8125} (format "%s:%d|%s" metric-name value (get-type-token type))))