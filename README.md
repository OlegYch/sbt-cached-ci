![Github Build Status](https://github.com/OlegYch/sbt-cached-ci/workflows/Continuous%20Integration/badge.svg)
[![Travis Build Status](https://travis-ci.com/OlegYch/sbt-cached-ci.svg?branch=master)](https://travis-ci.com/OlegYch/sbt-cached-ci)
[![CircleCI Build Status](https://circleci.com/gh/OlegYch/sbt-cached-ci.svg?style=svg)](https://circleci.com/gh/OlegYch/sbt-cached-ci)
[ ![Latest Version](https://api.bintray.com/packages/olegych/sbt-plugins/sbt-cached-ci/images/download.svg) ](https://bintray.com/olegych/sbt-plugins/sbt-cached-ci/_latestVersion)

# sbt-cached-ci

Incremental sbt builds for CI environments.

The plugin introduces a new task `cachedCiTest` which calls `testQuick` and calls `clean` once a day. 

## Usage

Supported sbt version - 1.x.

1. In `./project/plugins.sbt` add:
    ```
    addSbtPlugin("io.github.olegych" %% "sbt-cached-ci" % latest_version)
    ```
1. Configure your CI to cache current directory (preserving full timestamp) and call `cachedCiTest` task, see samples at [.travis.yml](.travis.yml), [.github/workflows/test.yml](.github/workflows/test.yml) or [.circleci/config.yml](.circleci/config.yml) 
 
Note that Github support relies on forked cache action (pending [https://github.com/actions/cache/pull/233](https://github.com/actions/cache/pull/233)).

CircleCI currently does not support proper caching, please vote here [https://ideas.circleci.com/ideas/CCI-I-1453](https://ideas.circleci.com/ideas/CCI-I-1453). 

Period between full test runs can be configured with `cachedCiTestFullPeriod` setting.

`cachedCiTestQuick` configures what is executed on every build.

`cachedCiTestFull` configures what is executed every `cachedCiTestFullPeriod`.