(defproject think "0.0.1"
  :description "4Clojure problems, other doodling with clojure."
  :dependencies [
		[org.clojure/clojure "1.6.0"]
                [org.clojure/tools.trace "0.7.8"]

		]
  :main think.core
  :repl-options {
                 :init (do (use 'clojure.tools.trace))})
