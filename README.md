![Github Build Status](https://github.com/OlegYch/sbt-cached-ci/workflows/Continuous%20Integration/badge.svg)
[![Travis Build Status](https://travis-ci.com/OlegYch/sbt-cached-ci.svg?branch=master)](https://travis-ci.com/OlegYch/sbt-cached-ci)
[![CircleCI Build Status](https://circleci.com/gh/OlegYch/sbt-cached-ci.svg?style=svg)](https://circleci.com/gh/OlegYch/sbt-cached-ci)


# sbt-cached-ci

Incremental sbt builds for CI environments.

The plugin introduces a new task `cachedCiTest` which calls `testQuick` and calls `clean` once a day. 

## Usage

Supported sbt version - 1.x.

1. In `./project/plugins.sbt` add:
    ```
    addSbtPlugin("org.olegych" %% "sbt-cached-ci" % latest_version) //see latest version in tags
    ```
1. Configure your CI to cache current directory and call `cachedCiTest` task, see samples at [.travis.yml](.travis.yml), [.github/workflows/test.yml](.github/workflows/test.yml) or [.circleci/config.yml](.circleci/config.yml) 
 

Period between full test runs can be configured with `cachedCiTestFullPeriod` setting.

`cachedCiTestQuick` configures what is executed on every build.

`cachedCiTestFull` configures what is executed every `cachedCiTestFullPeriod`.