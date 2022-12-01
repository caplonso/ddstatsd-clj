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
  "
    Sends the metric value to dogstatsD.

    **Params**
   | name | description |
   | :---- | :---------- |
   | connection-map | Map containing the host and port which the metric will be sent. |
   | metric-name | The name of metric, kebab case is converted to snake case in the dd metric panel |
   | type | Metric type, allowed values: `increment`, `decrement`, `timing`, `gauge` |
   | value | Integer value for the metric. |
   | tags | Map containing the tags for the metric |

   Example:
   ```clojure
   (def connection-map {:host \"localhost\" :port 8125})

   ;; Metric without any tag
   (send-metric connection-map \"my_super_metric.count\" \"increment\" 1)

   ;; Metric with some tag
   (send-metric connection-map \"my_another_super_metric.count\" \"increment\" 10 {:env \"production\"})
   ```
  "
  [connection-map metric-name type value & [tags]]
  {:pre [(spec/check-input ::spec/metric-type type)
         (spec/check-input ::spec/connection-map connection-map)
         (when tags
           (spec/check-input ::spec/tags tags))]}
    (send-packet connection-map (format-message metric-name type value tags)))
