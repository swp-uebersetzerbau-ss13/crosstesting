# Crosstesting [![Build Status](https://travis-ci.org/swp-uebersetzerbau-ss13/crosstesting.png?branch=master)](https://travis-ci.org/swp-uebersetzerbau-ss13/crosstesting)

The cross tests test for the interchangeability of the
[javabite](https://github.com/swp-uebersetzerbau-ss13/javabite) and the
[fuc](https://github.com/swp-uebersetzerbau-ss13/fuc) modules.

The cross tests can be found in
[crosstesting/crosstest](https://github.com/swp-uebersetzerbau-ss13/crosstesting/tree/master/crosstest).
They are parameterized over the Cartesian product of all 32 module combinations,
so every example programme is tested against every possible combination of javabite
and fuc modules. The cross tests compile the example programme and assert, that the
the compilation either produces expected errors or compiles through, producing
some kind of target code. The resulting target code is then executed and the
result of the execution is checked against the expected output
(`print` statements) and exit code (`return` statement). The runtime tests
hereby provide limited correctness testing (no proof of correctness of course!).

The example programmes are centralized in
[`ExampleProgs.java`](https://github.com/swp-uebersetzerbau-ss13/common/blob/master/interfaces/src/swp_compiler_ss13/common/test/ExampleProgs.java).
The example programmes comprise of the 15 examples from
[`common/examples`](https://github.com/swp-uebersetzerbau-ss13/common/tree/master/examples)
(loaded from there) plus 9 additional test programmes. In conjunction with the 32
module combinations this results in 768 test cases.
The main test logic is centralized in [AbstractCrosstest.java](crosstest/src/test/java/swp_compiler_ss13/crosstest/AbstractCrosstest.java),
while all the example programmes are implemented as test classes inheriting from [AbstractCrosstest.java](crosstest/src/test/java/swp_compiler_ss13/crosstest/AbstractCrosstest.java).

Every commit pushed to the remote master branch is tested with [Travis CI](https://github.com/travis-ci/travis-ci).
Before running the tests, LLVM is installed, allowing the execution of the the runtime tests.
The current build status of the master branch is displayed at the top.
The testing can be seen in action an the Travis CI [status page for crosstesting](https://travis-ci.org/swp-uebersetzerbau-ss13/crosstesting).

# Instructions

## Cloning the project

Use recursive cloning, to get the submodules with the repo:
`git clone git@github.com:swp-uebersetzerbau-ss13/crosstesting.git --recursive`

If already cloned, use `git submodule update --init --recursive` to get the
submodules to update to the commited version.

## Building the compilers
`gradle :jbJars` to build the javabite jars in `javabite/bin` [1]

`gradle :fucJars` to build the fuc jars in `fuc/code/dist` [1]

## Executing the tests
`gradle :crosstest:test` [1]

# Results
Nicely formatted results of the crosstests can be found
[here](http://swp-uebersetzerbau-ss13.github.io/crosstesting/results.html).
This results page is updated manually, it therefore does not necessarily
reflect the current stage of the repository.

Always up-to-date results can be found on the [Travis CI Project Page](https://github.com/swp-uebersetzerbau-ss13/crosstesting).

##### Footnotes:
[1] If gradle is not installed, `./gradlew <task>` can be used instead
