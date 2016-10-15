addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4")
addSbtPlugin("org.scoverage"    % "sbt-scoverage"       % "1.4.0")
addSbtPlugin("com.geirsson"     % "sbt-scalafmt"        % "0.4.1")

// gRPC and Protocol Buffers
addSbtPlugin("com.thesamet" % "sbt-protoc" % "0.99.1")
libraryDependencies += "com.trueaccord.scalapb" %% "compilerplugin" % "0.5.43"
