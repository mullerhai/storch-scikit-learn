import sbt.*
import Keys.*
import sbt.Def.settings

import scala.collection.Seq
//import scala.collection.immutable.Seq

//lazy val root = project
//  .enablePlugins(NoPublishPlugin)
//  .in(file("."))
////  .aggregate(core, vision, examples, docs)
//  .settings(
//    javaCppVersion := (ThisBuild / javaCppVersion).value,
////    csrCacheDirectory := file("D:\\coursier"),
//  )

ThisBuild / tlBaseVersion := "0.1.1" // your current series x.y
//ThisBuild / CoursierCache := file("D:\\coursier")
ThisBuild / organization := "io.github.mullerhai" //"dev.storch"
ThisBuild / organizationName := "storch.dev"
ThisBuild / startYear := Some(2024)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  // your GitHub handle and name
  tlGitHubDev("mullerhai", "mullerhai")
)
ThisBuild / version := "0.1.1"

ThisBuild / scalaVersion := "3.6.4"
//
ThisBuild / tlSonatypeUseLegacyHost := false

import xerial.sbt.Sonatype.sonatypeCentralHost
ThisBuild / sonatypeCredentialHost := sonatypeCentralHost

import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommandAndRemaining("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges,
)


lazy val root = (project in file("."))
  .settings(
    name :=  "storch-scikit-learn"
  )

//ThisBuild / tlSitePublishBranch := Some("main")

ThisBuild / apiURL := Some(new URL("https://storch.dev/api/"))
ThisBuild / tlSonatypeUseLegacyHost := false

// publish website from this branch
ThisBuild / tlSitePublishBranch := Some("main")
ThisBuild / homepage := Some(new URL("https://storch.dev/api/"))
ThisBuild / scmInfo := Some( ScmInfo( url( "https://github.com/mullerhai/storch-scikit-learn" ), "scm:git:https://github.com/mullerhai/storch-scikit-learn.git" ) )
//ThisBuild / scmInfo := Some(new ScmInfo("https://github.com/mullerhai/storch-k8s.git"))
//val scrImageVersion = "4.3.0" //4.0.34
//libraryDependencies +=   "dev.storch" % "core_3" % "0.2.6-1.15.1"

// https://mvnrepository.com/artifact/io.circe/circe-core
libraryDependencies += "io.circe" %% "circe-core" % "0.15.0-M1"
// https://mvnrepository.com/artifact/io.circe/circe-core
libraryDependencies += "io.circe" %% "circe-parser" % "0.15.0-M1"
// https://mvnrepository.com/artifact/org.scalanlp/breeze
libraryDependencies += "org.scalanlp" %% "breeze" % "2.1.0"
// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-collection-compat
libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.13.0"
// https://mvnrepository.com/artifact/org.typelevel/simulacrum-scalafix-annotations
libraryDependencies += "org.typelevel" %% "simulacrum-scalafix-annotations" % "0.5.4"
// https://mvnrepository.com/artifact/com.twitter/algebird-core
libraryDependencies += ("com.twitter" %% "algebird-core" % "0.13.10").cross(CrossVersion.for3Use2_13) exclude("org.scala-lang.modules","scala-collection-compat_2.13") exclude("org.typelevel","algebra_2.13")exclude("org.typelevel","cats-kernel_2.13")
//libraryDependencies += "io.circe" %% "circe-core" % circeVersion,
//libraryDependencies += "io.circe" %% "circe-parser" % circeVersion
// https://mvnrepository.com/artifact/org.scalacheck/scalacheck
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.18.1" % Test
excludeDependencies += "org.typelevel"%"algebra_3"
excludeDependencies += "org.typelevel"%"cats-kernel_3"
//excludeDependencies += "org.typelevel"%"algebra"
//excludeDependencies += "org.typelevel"%"cats-kernel"
libraryDependencies += ("org.typelevel" % "cats-kernel" % "2.13.0").cross(CrossVersion.for3Use2_13)
// https://mvnrepository.com/artifact/org.typelevel/algebra
libraryDependencies += ("org.typelevel" % "algebra" % "2.13.0").cross(CrossVersion.for3Use2_13)

//exclude("org.scala-lang.modules","scala-collection-compat_2.13")
ThisBuild  / assemblyMergeStrategy := {
  case v if v.contains("module-info.class")   => MergeStrategy.discard
  case v if v.contains("UnusedStub")          => MergeStrategy.first
  case v if v.contains("aopalliance")         => MergeStrategy.first
  case v if v.contains("inject")              => MergeStrategy.first
  case v if v.contains("jline")               => MergeStrategy.discard
  case v if v.contains("scala-asm")           => MergeStrategy.discard
  case v if v.contains("asm")                 => MergeStrategy.discard
  case v if v.contains("scala-compiler")      => MergeStrategy.deduplicate
  case v if v.contains("reflect-config.json") => MergeStrategy.discard
  case v if v.contains("jni-config.json")     => MergeStrategy.discard
  case v if v.contains("git.properties")      => MergeStrategy.discard
  case v if v.contains("reflect.properties")      => MergeStrategy.discard
  case v if v.contains("compiler.properties")      => MergeStrategy.discard
  case v if v.contains("scala-collection-compat.properties")      => MergeStrategy.discard
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}