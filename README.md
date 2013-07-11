# Crosstesting [![Build Status](https://travis-ci.org/swp-uebersetzerbau-ss13/crosstesting.png?branch=master)](https://travis-ci.org/swp-uebersetzerbau-ss13/crosstesting)

Crosstesting the [Javabite Compiler](https://github.com/swp-uebersetzerbau-ss13/javabite) 
and [FU Compiler](https://github.com/swp-uebersetzerbau-ss13/fuc).

# Instructions

## Cloning the project

Use recursive cloning, to get the submodules with the repo: 
`git clone git@github.com:swp-uebersetzerbau-ss13/crosstesting.git --recursive`

If already cloned, use `git submodule update --init --recursive` to get the submodules to 
update to the commited version.

## Building the compilers
`gradle :jbJars` to build the javabite jars in `javabite/bin` [1]

`gradle :fucJars` to build the fuc jars in `fuc/code/dist` [1]

## Executing the tests
`gradle :crosstest:test` [1]

# Results
Nicely formatted results of the crosstests can be found 
[here](http://swp-uebersetzerbau-ss13.github.io/crosstesting/results.html).
This results page is updated manually, it therefore does not necessarily reflect the current stage of the repository.

Always up-to-date results can be found on the [Travis CI Project Page](https://github.com/swp-uebersetzerbau-ss13/crosstesting).

##### Footnotes:
[1] If gradle is not installed, `./gradlew <task>` can be used instead
