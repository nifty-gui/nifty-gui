# Nifty GUI

As of Nifty 1.4.2 we're targeting a Maven central release. It's not released yet but here are the presumably details:

```XML
<groupId>com.github.nifty-gui</groupId>
<artifactId>nifty</artifactId>
<version>1.4.2-SNAPSHOT</version>
```

To use SNAPSHOT builds of Nifty you'll still need to add the Sonatype snapshots repositories to your pom.xml. This step is not necessary after 1.4.2 has been released.

```XML
  <repositories>
    <!-- only needed for snapshot builds starting with 1.4.2-SNAPSHOT -->
    <repository>
      <id>ossrh</id>
      <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
  </repositories>
```



[![Stories in Ready](https://badge.waffle.io/void256/nifty-gui.png?label=ready&title=Ready)](http://waffle.io/void256/nifty-gui)

[![Throughput Graph](https://graphs.waffle.io/void256/nifty-gui/throughput.svg)](https://waffle.io/void256/nifty-gui/metrics) 
