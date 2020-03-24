![Github Build Status](https://github.com/OlegYch/sbt-cached-ci/workflows/Continuous%20Integration/badge.svg)
[![Travis Build Status](https://travis-ci.com/OlegYch/sbt-cached-ci.svg?branch=master)](https://travis-ci.com/OlegYch/sbt-cached-ci)
# sbt-cached-ci

Incremental sbt builds for CI environments.

The plugin introduces a new task `cachedCiTest` which calls `testQuick` and calls `clean` once a day. 

## Usage

Supported sbt version - 1.x.

0. In `./project/plugins.sbt` add:
    ```
    addSbtPlugin("org.olegych" %% "sbt-cached-ci" % latest_version)
    ```
0. Configure your CI to cache current directory and call `cachedCiTest` task, see samples at [.travis.yml](.travis.yml) or [.github/workflows/test.yml](.github/workflows/test.yml) 
 
### Testing

