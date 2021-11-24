# /^\|.*/ && $2 ~ "Namespace" { print "namespace,n_tests,forms,lines" }
# /^\|.*/ && $2 ~ "todomvc.*" { gsub(",","."); print $2 "," n_tests "," $4 "," $6 }
/^\|.*/ && $2 ~ "ALL" { gsub(",","."); print n_tests "," $5 "," $7 }
