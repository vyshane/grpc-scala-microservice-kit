/*
 * Project metadata
 */
name := "echod"
version := "1.0.0-SNAPSHOT"
description := "A starter kit for building microservices using gRPC and Scala"
organization := "mu.node"
organizationHomepage := Some(url("http://node.mu"))

/*
 * Docker image build
 */
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)
import com.typesafe.sbt.packager.docker._
maintainer in Docker := "Vy-Shane Xie <shane@node.mu>"
dockerBaseImage := "openjdk:8-jre-alpine"
dockerRepository := Some("vyshane")
dockerUpdateLatest := true

/*
 * Compiler
 */
scalaVersion := "2.11.8"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

/*
 * Dependencies
 */
libraryDependencies ++= Seq(
  // Configuration
  "com.typesafe" % "config" % "1.3.1",
  // Logging
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  // Testing
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  // Dependency injection
//  "com.softwaremill.macwire" %% "macros" % "2.2.4" % "provided",
  // JSON Web Tokens, JSON parsing
  "com.pauldijou" %% "jwt-core" % "0.9.0",
  "com.typesafe.play" %% "play-json" % "2.5.8"
)
// gRPC and Protocol Buffers
libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty" % "1.0.1",
  "io.grpc" % "grpc-stub" % "1.0.1",
  "io.grpc" % "grpc-auth" % "1.0.1",
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.5.43",
  "io.netty" % "netty-tcnative-boringssl-static" % "1.1.33.Fork19",  // SSL support
  "javassist" % "javassist" % "3.12.1.GA"  // Improves Netty performance
)

PB.targets in Compile := Seq(
  scalapb.gen(grpc = true, flatPackage = true) -> (sourceManaged in Compile).value
)

/*
 * Code coverage via scoverage
 */
coverageMinimum := 90
coverageFailOnMinimum := true
coverageOutputCobertura := false
coverageOutputHTML := true
coverageOutputXML := false

/*
 * Code formatting
 */
scalafmtConfig := Some(file(".scalafmt.conf"))
