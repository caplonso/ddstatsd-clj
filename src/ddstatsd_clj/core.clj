(ns ddstatsd-clj.core
  (:require [ddstatsd-clj.network :refer [send-packet]]
            [ddstatsd-clj.specs :refer [check-input ::metric-type]])
  (:gen-class))


(defn- get-type-token
  [type-name]
  (cond (or (= type-name "increment") (= type-name "decrement"))  "c"
        (= type-name "timing") "ms"
        (= type-name "gauge") "g"
        :else nil))


(defn send-metric
  [metric-name type value]
  {:pre (check-input ::metric-type type)}
  (send-packet {:host "localhost" :port 8125} (format "%s:%d|%s" metric-name value (get-type-token type))))
