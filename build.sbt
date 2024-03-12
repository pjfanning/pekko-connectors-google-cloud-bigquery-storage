ThisBuild / version := "1.0.0-SNAPSHOT"

val scala213Version = "2.13.13"
ThisBuild / scalaVersion := scala213Version
ThisBuild / crossScalaVersions := Seq(scala213Version, "2.12.19", "3.3.3")

ThisBuild / resolvers += Resolver.ApacheMavenSnapshotsRepo

val ArrowVersion = "15.0.1"
val AvroVersion = "1.11.3"
val LogbackVersion = "1.3.14"
val PekkoVersion = "1.0.2"
val PekkoConnectorsVersion = "1.0.2"
val PekkoHttpVersion = "1.0.1"

lazy val root = (project in file("."))
  .settings(
    name := "pekko-connectors-google-cloud-bigquery-storage",
    libraryDependencies ++= Seq(
      "com.google.api.grpc" % "proto-google-cloud-bigquerystorage-v1" % "2.47.0" % "protobuf-src",
      "org.apache.avro" % "avro" % AvroVersion % "provided",
      "org.apache.arrow" % "arrow-vector" % ArrowVersion % "provided",
      "io.grpc" % "grpc-auth" % org.apache.pekko.grpc.gen.BuildInfo.grpcVersion,
      "com.google.protobuf" % "protobuf-java" % "3.25.3" % Runtime,
      "org.apache.pekko" %% "pekko-discovery" % PekkoVersion,
      "org.apache.pekko" %% "pekko-http-spray-json" % PekkoHttpVersion,
      "org.apache.pekko" %% "pekko-http-core" % PekkoHttpVersion,
      "org.apache.pekko" %% "pekko-http" % PekkoHttpVersion,
      "org.apache.pekko" %% "pekko-parsing" % PekkoHttpVersion,
      "org.apache.pekko" %% "pekko-connectors-google-common" % PekkoConnectorsVersion,
      "org.apache.arrow" % "arrow-memory-netty" % ArrowVersion % Test,
      "ch.qos.logback" % "logback-classic" % LogbackVersion % Test,
      "org.mockito" % "mockito-core" % "4.11.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.18" % Test,
      "org.scalatestplus" %% "mockito-4-11" % "3.2.17.0" % Test,
      "com.novocode" % "junit-interface" % "0.11" % Test,
      "junit" % "junit" % "4.13.2" % Test
    ),
    pekkoGrpcCodeGeneratorSettings ~= { _.filterNot(_ == "flat_package") },
    pekkoGrpcCodeGeneratorSettings += "server_power_apis",
    // FIXME only generate the server for the tests again
    pekkoGrpcGeneratedSources := Seq(PekkoGrpc.Client, PekkoGrpc.Server),
    // Test / pekkoGrpcGeneratedSources := Seq(PekkoGrpc.Server),
    pekkoGrpcGeneratedLanguages := Seq(PekkoGrpc.Scala, PekkoGrpc.Java),
    Compile / scalacOptions ++= Seq(
      "-Wconf:src=.+/pekko-grpc/main/.+:s",
      "-Wconf:src=.+/pekko-grpc/test/.+:s"),
    compile / javacOptions := (compile / javacOptions).value.filterNot(_ == "-Xlint:deprecation")
  ).enablePlugins(PekkoGrpcPlugin)
