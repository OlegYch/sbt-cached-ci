[![Latest sbt2 Release](https://maven-badges.sml.io/sonatype-central/io.github.olegych/sbt-cached-ci_sbt2_3/badge.svg?subject=sbt2&color=blue)](https://central.sonatype.com/artifact/io.github.olegych/sbt-cached-ci_sbt2_3)
[![Latest sbt1 Release](https://maven-badges.sml.io/sonatype-central/io.github.olegych/sbt-cached-ci_2.12_1.0/badge.svg?subject=sbt1&color=blue)](https://central.sonatype.com/artifact/io.github.olegych/sbt-cached-ci_2.12_1.0)

[![Github Build Status](https://github.com/OlegYch/sbt-cached-ci/workflows/CI/badge.svg)](https://github.com/OlegYch/sbt-cached-ci/actions/workflows/test.yml)
[![CircleCI Build Status](https://circleci.com/gh/OlegYch/sbt-cached-ci.svg?style=svg)](https://circleci.com/gh/OlegYch/sbt-cached-ci)

# sbt-cached-ci

Incremental sbt builds for CI environments.

The plugin introduces a new task `cachedCiTest` which calls either `testQuick` or `clean;testFull` depending on `cachedCiTestFullPeriod` setting. 

## Usage

Supported sbt versions - 1.5.8+, 2.x.

1. In `./project/plugins.sbt` add:
    ```
    addSbtPlugin("io.github.olegych" % "sbt-cached-ci" % latest_version)
    ```
1. Configure your CI to cache current directory (preserving full timestamp) and call `cachedCiTest` task, see samples at [.travis.yml](.travis.yml), [.github/workflows/test.yml](.github/workflows/test.yml) or [.circleci/config.yml](.circleci/config.yml) 
 
Period between full test runs can be configured with `cachedCiTestFullPeriod` setting.

`cachedCiTestQuick` configures what is executed on every build.

`cachedCiTestFull` configures what is executed every `cachedCiTestFullPeriod`.

If your subprojects use `crossScalaVersions`, enable aggregation like `cachedCiTest / aggregate := true`, or better yet - migrate to https://github.com/sbt/sbt-projectmatrix