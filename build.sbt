enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

name := "grpc-scala-microservice-kit"
packageSummary := "A starter kit for building microservices using gRPC and Scala"
organization := "mu.node"
version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"
scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "com.typesafe" % "config" % "1.3.1",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    // JSON Web Tokens
    "com.pauldijou" %% "jwt-core" % "0.8.1",
    // Dependency injection
    "com.softwaremill.macwire" %% "macros" % "2.2.4" % "provided"
  )
}

import com.typesafe.sbt.packager.docker._
dockerBaseImage := "java:openjdk-8-jre"
dockerRepository := Some("vyshane/grpc-scala-microservice-kit")

// Include tests in the package
mappings in Universal ++= NativePackagerHelper.directory("test")
mappings in Universal <+= (packageBin in Test) map { jar =>
  jar -> ("lib/" + jar.getName)
}

// gRPC and Protocol Buffers
import com.trueaccord.scalapb.{ScalaPbPlugin => PB}
libraryDependencies ++= Seq(
  "io.grpc" % "grpc-netty" % "1.0.0",
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % (PB.scalapbVersion in PB.protobufConfig).value,
  "io.netty" % "netty-tcnative-boringssl-static" % "1.1.33.Fork22"  // SSL support
)
PB.protobufSettings
PB.flatPackage in PB.protobufConfig := true
PB.runProtoc in PB.protobufConfig := (args => com.github.os72.protocjar.Protoc.runProtoc("-v300" +: args.toArray))

