(ns ddstatsd-clj.network
  (:require [ddstatsd-clj.specs :as spec])
  (:import (java.net InetAddress DatagramPacket DatagramSocket)))


(defn- create-socket
  [] (DatagramSocket.))

(defn- create-package
  [destination data]
  (let [address (InetAddress/getByName (:host destination))
        port (:port destination)]
    (DatagramPacket. (.getBytes data) (.length data) address port)))

(defn ^:no-doc send-packet
  [destination data]
  {:pre (spec/check-input ::spec/connection-map destination)}
  (let [socket (create-socket)
        packet (create-package destination data)]
    (.send socket packet)))
