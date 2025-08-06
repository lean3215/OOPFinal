ThisBuild / scalaVersion := "3.3.1"

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "21.0.0-R32",
  "org.scalikejdbc" %% "scalikejdbc" % "4.3.2",
  "org.scalikejdbc" %% "scalikejdbc-config" % "4.3.2",
  "com.h2database" % "h2" % "2.2.224"
)