# Nifty GUI

As of Nifty 1.4.2 we're targeting a Maven central release. It's not released yet but here are the presumably details:

```XML
<groupId>com.github.nifty-gui</groupId>
<artifactId>nifty</artifactId>
<version>1.4.2-SNAPSHOT</version>
```

You'll still need to add both repositories to your pom.xml for now, at least:

```XML
  <repositories>
    <!-- only needed for snapshot builds starting with 1.4.2-SNAPSHOT -->
    <repository>
      <id>ossrh</id>
      <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>

    <!-- still needed for some other dependencies not yet available in central like jglfont -->
    <repository>
      <id>nifty-maven-repo.sourceforge.net</id>
      <url>http://nifty-gui.sourceforge.net/nifty-maven-repo</url>
    </repository>
  </repositories>
```



[![Stories in Ready](https://badge.waffle.io/void256/nifty-gui.png?label=ready&title=Ready)](http://waffle.io/void256/nifty-gui)

[![Throughput Graph](https://graphs.waffle.io/void256/nifty-gui/throughput.svg)](https://waffle.io/void256/nifty-gui/metrics) 
