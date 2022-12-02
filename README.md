# ddstatd-clj


**Latest version**<br>
[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.caplonso/ddstatsd-clj.svg)](https://clojars.org/org.clojars.caplonso/ddstatsd-clj)


A Clojure library designed to send custom metrics to datadog through the DogStatsD agent.

## Usage

Sends the metric value to dogstatsD using UPD, the main function is `ddstatsd_clj.core/send-metric`.

| name             | type     | description                                                                      |
|------------------|----------|----------------------------------------------------------------------------------|
| `connection-map` | `map`    | Map containing the host and port which the metric will be sent.                  |
| `metric-name`    | `string` | The name of metric, kebab case is converted to snake case in the dd metric panel |
| `type`           | `string` | Metric type, allowed values: `increment`, `decrement`, `timing`, `gauge`         |
| `value`          | `int`    | Integer value for the metric.                                                    |
| `tags`           | `map`    | Map containing the tags for the metric                                           |

Examples:
```clojure
(def connection-map {:host "localhost" :port 8125})

;; Metric without any tag
(send-metric connection-map "my_super_metric.count" "increment" 1)

;; Metric with some tag
(send-metric connection-map "my_another_super_metric.count" "increment" 10 {:env "production"})
   ```