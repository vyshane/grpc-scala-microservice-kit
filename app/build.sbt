/*
 * Project metadata
 */
name := "echod"
version := "1.0-SNAPSHOT"
description := "A starter kit for building microservices using gRPC and Scala"
organization := "mu.node"
organizationHomepage := Some(url("http://node.mu"))

/*
 * Docker image build
 */
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
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
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}
libraryDependencies ++= Seq(
  "io.grpc"                % "grpc-netty"                      % "1.0.0",
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc"           % (PB.scalapbVersion in PB.protobufConfig).value,
  "io.netty"               % "netty-tcnative-boringssl-static" % "1.1.33.Fork22" // SSL support
)
PB.protobufSettings
PB.flatPackage in PB.protobufConfig := true
PB.runProtoc in PB.protobufConfig := (args => com.github.os72.protocjar.Protoc.runProtoc("-v300" +: args.toArray))

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
