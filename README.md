# TodoMVC: Clojure Spec

TodoMVC implemented in Clojure using Rum and Clojure Spec for generative testing.

## Live version
[https://todomvc-spec.netlify.app/](https://todomvc-spec.netlify.app/)

## Test
Run `n` generated tests.

```
bin/test <n>
```

## LOC
```
bin/loc
```

## Coverage
Measure test coverage.

```
N_TESTS=<n> bin/coverage
```

### Coverage report
Coverage report export for n=1...N tests.

```
bin/coverage_report <N>
```

## Mutation testing
Tests mutations in `mutations/` and writes report to stdout.

```
bin/evaluate
```

## Functional requirements
Summary of the functional requirements for TodoMVC.

1. When there are no todos, `#main` and `#footer` should be hidden.
2. New todos are entered at the input. Enter creates the todo, appends to list and clears input. The input should be focused on page load using `autoFocus` attribute.
3. All inputs are trimmed when saved and empty todos are not allowed.
4. Mark all as complete toggles all todo to the same state as itself and should be updated when the status of any todo changes.
5. Todo items have three interactions.
   1. Clicking checkbox toggles its state which is displayed by its parent container `li` element.
   2. Double clicking the label activates editing mode.
   3. Hovering over the todo item shows the remove button.
6. Todo item editing mode hides other interactions and should save the trimmed value on blur and enter.
7. Counter displays the number of todos in pluralized form.
8. Clear completed removes completed todos when clicked. Should be hidden when there are no completed todos.
9. App should persist state between sessions.
10. Routing should be used to navigate filters such as `#/completed`.

## Changelog
- 1.0.0 TodoMVC according to specification
- 0.1.0 Init
