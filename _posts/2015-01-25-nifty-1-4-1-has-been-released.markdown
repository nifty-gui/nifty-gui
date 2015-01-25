---
layout: post
title:  "Nifty 1.4.1 has been released"
date:   2015-01-25 21:30:00
categories: blog update
comments: true
image:
  feature: logo.png
---
Nifty 1.4.1 contains bugfixes and features. There might be some minor run-time incompatibilities with 1.4.x.

Kudos to the following 1.4.1 committers (alphabetical order):

* Aaron Mahan
* Brian Groenke
* Martin Karing
* relu91

Here are the download links for Nifty 1.4.1:

* [nifty-1.4.1-changelog.txt (sf.net)](http://sourceforge.net/projects/nifty-gui/files/nifty-gui/1.4.1/nifty-1.4.1-changelog.txt/download)
* [nifty-1.4.1.zip (sf.net)](http://sourceforge.net/projects/nifty-gui/files/nifty-gui/1.4.1/nifty-1.4.1.zip/download)
* [Nifty 1.4.1 Maven Projects Page (browse the JavaDoc online!)](http://nifty-gui.sourceforge.net/projects/1.4.1/index.html)
* [Nifty 1.4.1 Tag on github (browse source online)](https://github.com/void256/nifty-gui/tree/nifty-main-1.4.1)

For all Maven users: Simply add our sf.net Nifty Maven Repo to your pom.xml:

{% highlight XML %}
<repositories>
  <repository>
    <id>nifty-maven-repo.sourceforge.net</id>
    <url>http://nifty-gui.sourceforge.net/nifty-maven-repo</url>
  </repository>
</repositories>
{% endhighlight %}

and upgrade your dependency to 1.4.1:

{% highlight XML %}
<dependency>
<groupId>lessvoid</groupId>
<artifactId>nifty</artifactId>
<version>1.4.1</version>
</dependency>
{% endhighlight %}

### Quickstart/Demo/Test for none Maven users

You can run the following commands to download and run the Nifty examples from a commandline prompt. The commands given are for running the examples on Mac OS X but it should be easily modifiable to run under Windows and Linux as well.

{% highlight bash %}
# This runs the LWJGL example but you can use the same mechanism to run the examples for the
# other supported systems as well. Just swap-out the nifty-*-renderer jar.
cd /tmp
mkdir nifty-1.4.1
cd nifty-1.4.1

# of course you can download this manually as well
curl -O -L http://sourceforge.net/projects/nifty-gui/files/nifty-gui/1.4.1/nifty-1.4.1.zip
unzip nifty-1.4.1

# since we want to run the LWJGL example we download LWJGL as well
curl -O -L http://sourceforge.net/projects/java-game-lib/files/Official%20Releases/LWJGL%202.9.3/lwjgl-2.9.3.zip
unzip lwjgl-2.9.3.zip

# now run the de.lessvoid.nifty.examples.lwjgl.defaultcontrols.ControlsDemoMain example using lwjgl
java -cp \
nifty-1.4.1/nifty-1.4.1.jar:\
nifty-1.4.1/nifty-default-controls-1.4.1.jar:\
nifty-1.4.1/nifty-style-black-1.4.1.jar:\
nifty-1.4.1/nifty-lwjgl-renderer-1.4.1.jar:\
nifty-1.4.1/nifty-openal-soundsystem-1.4.1.jar:\
nifty-1.4.1/nifty-examples-lwjgl-1.4.1.jar:\
nifty-1.4.1/nifty-examples-1.4.1.jar:\
nifty-1.4.1/dependencies/eventbus-1.4.jar:\
nifty-1.4.1/dependencies/xpp3-1.1.4c.jar:\
nifty-1.4.1/dependencies/jglfont-core-1.4.jar:\
nifty-1.4.1/dependencies/jorbis-0.0.17.jar:\
lwjgl-2.9.3/jar/lwjgl.jar \
-Djava.library.path=lwjgl-2.9.3/native/macosx/ \
de.lessvoid.nifty.examples.lwjgl.defaultcontrols.ControlsDemoMain
{% endhighlight %}

void
