lazy val core = (project in file(".")).settings(commonSettings)

lazy val commonSettings = Seq(
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.13.4",
  name := "tutu",
  organization := "com.github.kuno_maichi",
  scalacOptions ++= Seq("-encoding", "utf8", "-unchecked", "-deprecation", "-feature", "-language:implicitConversions", "-language:existentials"),
  javacOptions ++= Seq("-Xlint:unchecked", "-source", "11"),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.9" % "test",
    "org.javafp" % "parsecj" % "0.6"
  ),
  mainClass := Some("tutu.tools.Runner"),
  assembly / assemblyJarName := "tutu.jar"
)

console / initialCommands += {
  Iterator(
    "tutu.tools._",
  ).map("import "+).mkString("\n")
}

pomExtra := (
  <url>https://github.com/kuno-maichi/tutu</url>
  <licenses>
    <license>
      <name>The MIT License</name>
      <url>http://www.opensource.org/licenses/MIT</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:kuno-maichi/tutu.git</url>
    <connection>scm:git:git@github.com:kuno-maichi/tutu.git</connection>
  </scm>
  <developers>
    <developer>
      <id>kuno-maichi</id>
      <name>Maichi Kuno</name>
      <url>https://github.com/kuno-maichi</url>
    </developer>
  </developers>
)

publishTo := {
  val v = version.value
  val nexus = "https://oss.sonatype.org/"
  if (v.endsWith("-SNAPSHOT"))
    Some("snapshots" at nexus+"content/repositories/snapshots")
  else
    Some("releases" at nexus+"service/local/staging/deploy/maven2")
}

credentials ++= {
  val sonatype = ("Sonatype Nexus Repository Manager", "oss.sonatype.org")
  def loadMavenCredentials(file: java.io.File) : Seq[Credentials] = {
    xml.XML.loadFile(file) \ "servers" \ "server" map (s => {
      val host = (s \ "id").text
      val realm = if (host == sonatype._2) sonatype._1 else "Unknown"
      Credentials(realm, host, (s \ "username").text, (s \ "password").text)
    })
  }
  val ivyCredentials   = Path.userHome / ".ivy2" / ".credentials"
  val mavenCredentials = Path.userHome / ".m2"   / "settings.xml"
  (ivyCredentials.asFile, mavenCredentials.asFile) match {
    case (ivy, _) if ivy.canRead => Credentials(ivy) :: Nil
    case (_, mvn) if mvn.canRead => loadMavenCredentials(mvn)
    case _ => Nil
  }
}
