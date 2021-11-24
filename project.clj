(defproject todomvc "0.0.1-SNAPSHOT"
  :description "TodoMVC: Clojure Spec"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.773"]
                 [expound "0.8.9"]
                 [orchestra "2021.01.01-1"]
                 [org.clojure/tools.namespace "1.1.0"]
                 [rum "0.12.6"]
                 [com.gfredericks/test.chuck "0.2.13"]]
  :plugins [[lein-cloverage "1.2.2"]]
  :repl-options {:init-ns todomvc.core}
  ;; We need to disable lein retest and test selectors
  ;; https://github.com/technomancy/leiningen/issues/2524
  :monkeypatch-clojure-test false
  ;; Cljs config
  :profiles {:dev {:dependencies [[org.clojure/test.check "1.1.0"]
                                  [com.bhauman/figwheel-main "0.2.12"]
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]
                                  [org.clojure/data.csv "1.0.0"]]}}
  :resource-paths ["resources" "target"]
  :source-paths ["src"]
  :aliases {"fig" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:min" ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
            "mutation-test" ["run" "-m" "todomvc.mutations/test!"]
            "coverage" ["cloverage"
                        "-e" "todomvc.(query|events|db|spec|storage|env|utils|query|main|test_utils|mutations)"]})
