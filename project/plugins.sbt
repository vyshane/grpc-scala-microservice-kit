addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4")
addSbtPlugin("org.scoverage"    % "sbt-scoverage"       % "1.4.0")
addSbtPlugin("com.geirsson"     % "sbt-scalafmt"        % "0.4.1")

// gRPC and Protocol Buffers
libraryDependencies ++= {
  Seq("com.github.os72" % "protoc-jar" % "3.0.0.1")
}
addSbtPlugin("com.trueaccord.scalapb" % "sbt-scalapb" % "0.5.40")
