(ns mmlpots.core
  (:require [net.cgrand.enlive-html :as enlive]))

(defn parse-gpx [gpx-file]
  (let [gpx-str (slurp gpx-file)]
    (enlive/xml-resource (java.io.StringReader. gpx-str))))

(defn track-points [gpx]
  (enlive/select gpx [:trkpt]))

(defn parse-double [^String dbl]
  (Double/parseDouble dbl))

(defn latlon [trkpt]
  (let [{lat :lat lon :lon} (:attrs trkpt)]
    (mapv parse-double [lat lon])))

(defn elevation [trkpt]
  (-> (enlive/select trkpt [:ele enlive/content])
      first
      parse-double))

(defn parse-track-point [trkpt]
  ((juxt latlon elevation) trkpt))

(defn pts []
  (->> "test.gpx"
       parse-gpx
       track-points
       (map parse-track-point)))

(defn distance [[lat1 lon1] [lat2 lon2]]
  (let [deg-fn #(* 111111.0 %) ; 1 deg is approximately [111111m * cos(lat), 111111m]
        [y1 y2] (map (fn [lon] (* (Math/cos (Math/toRadians lat1)) (deg-fn lon))) [lon1 lon2])
        [x1 x2] (map deg-fn [lat1 lat2])
        diffsqr #(let [d (- %1 %2)] (* d d))]
    (Math/sqrt (+ (diffsqr x1 x2) (diffsqr y1 y2)))))

(defn diff-array [track-points]
  (let [p21 (partition 2 1 track-points)]
    (map (fn [[[pos1 ele1] [pos2 ele2]]]
           [(distance pos1 pos2) (- ele2 ele1)]) p21)))

(defn transpose [matrix]
  (apply map list matrix))

(defn solve []
  (let [[distances elevations] (mapv double-array (-> (pts) diff-array transpose))]
    (potts.Elevation/solve distances elevations)))

(defn -main [& args]
  )