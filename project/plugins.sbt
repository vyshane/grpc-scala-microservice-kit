addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4")

// gRPC and Protocol Buffers
libraryDependencies ++= {
  Seq("com.github.os72" % "protoc-jar" % "3.0.0.1")
}
addSbtPlugin("com.trueaccord.scalapb" % "sbt-scalapb" % "0.5.40")

