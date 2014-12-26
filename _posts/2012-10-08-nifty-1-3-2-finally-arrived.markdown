---
layout: post
status: publish
published: true
title: Nifty 1.3.2 finally arrived
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 459
wordpress_url: http://nifty-gui.lessvoid.com/?p=459
date: '2012-10-08 23:18:02 +0200'
date_gmt: '2012-10-08 22:18:02 +0200'
categories:
- release
tags: []
comments:
- id: 1422
  author: TQ
  author_email: tomo.cesnik@gmail.com
  author_url: ''
  date: '2012-10-09 21:02:58 +0200'
  date_gmt: '2012-10-09 20:02:58 +0200'
  content: Uh, really happy that this is out! Congratulations! :)
- id: 1424
  author: fm27
  author_email: fm27@gmx.de
  author_url: ''
  date: '2012-10-10 01:38:07 +0200'
  date_gmt: '2012-10-10 00:38:07 +0200'
  content: "Hello nifty community!\r\n\r\nI want to use nifty within my jME3-Application
    and I need nested context menus. Is is possible to modify the popup-\"menu\"-example
    of nifty supporting NESTED menu&#47;menu-items? What do I have to do? Is there
    example code?"
- id: 1425
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2012-10-10 22:01:45 +0200'
  date_gmt: '2012-10-10 21:01:45 +0200'
  content: Well, there is no direct build-in support for that (e.g. there is no flag
    or option you can directly set for a menu-item to get that functionality) BUT
    nothing prevents you to simple show a new menu when a menu-item is clicked, right?
- id: 1531
  author: fabio
  author_email: baenor@live.it
  author_url: ''
  date: '2013-02-07 15:53:36 +0100'
  date_gmt: '2013-02-07 14:53:36 +0100'
  content: "i'm a noob but i'm really angry....how the hell can i download the folder
    from sourceforge????\r\nthanks for help"
- id: 1532
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-02-07 23:16:53 +0100'
  date_gmt: '2013-02-07 22:16:53 +0100'
  content: you click at the link and then you download all the jars you need ... or
    you take some time and learn about maven which will automatically download all
    the dependencies you need if you use the xml snippets provided above
- id: 1533
  author: fabio
  author_email: baenor@live.it
  author_url: ''
  date: '2013-02-08 12:16:59 +0100'
  date_gmt: '2013-02-08 11:16:59 +0100'
  content: there is no way to download all the folder? because i need also the javadoc
- id: 1534
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-02-09 01:13:22 +0100'
  date_gmt: '2013-02-09 00:13:22 +0100'
  content: Ah! No not by default unfortunately :&#47; BUT I've zipped all the jars
    including the source and javadoc jars into a single file "_nifty-complete-1.3.2.zip".
    It's currently in state pending but should be available for download soon. Great?
    :)
- id: 1536
  author: fabio
  author_email: baenor@live.it
  author_url: ''
  date: '2013-02-09 13:51:00 +0100'
  date_gmt: '2013-02-09 12:51:00 +0100'
  content: awesome thanks! i need a GUI for my slick game, i hope i can get nifty
    working as soon as possible :P
- id: 1918
  author: wiki
  author_email: floriland@gmx.net
  author_url: ''
  date: '2013-06-15 09:31:57 +0200'
  date_gmt: '2013-06-15 08:31:57 +0200'
  content: 'Just a short question: Is it possible that the de.lessvoid.nifty.batch
    package is missing in the sourceforge build? Because if I look on gitHub, the
    folder''s there, but the _nifty-complete-1.3.2.zip does not contain any jar file
    with a batch package'
- id: 1919
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-06-15 13:35:38 +0200'
  date_gmt: '2013-06-15 12:35:38 +0200'
  content: Yes, it's missing from 1.3.2 since it was introduced after the 1.3.2 release
    :) The batched renderer will be part of 1.3.3 Nifty. If you want to try it now
    you have to use a nightly build from the Maven repo (there is not a single zip
    available - only individual jars) OR you build it from source yourself - which
    is not difficult. You just need to git clone the repo and call "mvn package".
    Or you wait some more days for a regular Nifty 1.3.3 release :)
- id: 2707
  author: Primogenitor
  author_email: primogenitor@hotmail.com
  author_url: ''
  date: '2014-01-08 16:55:49 +0100'
  date_gmt: '2014-01-08 15:55:49 +0100'
  content: FYI, the dependency tags should be groupId and artifact Id (note the capital
    I). Might depend on the version of maven you use, but took me a while to spot
    after a copy-n-paste!
---
<p>Nifty 1.3.2 is mainly a bugfix release and is compatible with Nifty 1.3.1. There are a couple of nifty new features available too :) We've counted 175 individual changes which is <strong>A LOT<&#47;strong>!</p>
<p>There will be an updated Nifty Manual available soon. Until it is available you can find out what's new in the <a href="http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.3.2&#47;nifty-1.3.2-changelog.txt&#47;download">Nifty 1.3.2 change log (sf.net)<&#47;a></p>
<p>And here are the other Nifty links:</p>
<p><a href="https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.3.2&#47;">Nifty 1.3.2 Download Folder at sf.net<&#47;a><br />
<a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;projects&#47;1.3.2&#47;">Nifty 1.3.2 Maven Projects Page (browse the JavaDoc online!)<&#47;a><br />
<a href="http:&#47;&#47;www.jmonkeyengine.com&#47;nightly&#47;">Get a nightly jME3 build with Nifty 1.3.2 (jME3_2012-10-08.zip+) <&#47;a><br />
<a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-default-controls-examples-1.3.2.jnlp">Webstart - Nifty Default Controls Example (1.3.2)<&#47;a><br />
<a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-examples-1.3.2.jnlp">Webstart - Nifty Standard Examples (1.3.2)<&#47;a></p>
<p>For Maven simply add our sf.net Nifty Maven Repo to your pom.xml:</p>
<pre class="brush:xml">  <repositories><br />
    <repository><br />
      <id>nifty-maven-repo.sourceforge.net<&#47;id><br />
      <url>http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo<&#47;url><br />
    <&#47;repository><br />
  <&#47;repositories><&#47;pre></p>
<p>and upgrade your dependency to 1.3.2:</p>
<pre class="brush:xml">    <dependency><br />
      <groupId>lessvoid<&#47;groupId><br />
      <artifactId>nifty<&#47;artifactId><br />
      <version>1.3.2<&#47;version><br />
    <&#47;dependency><&#47;pre></p>
<p>Have a lot of fun with Nifty 1.3.2! The best Nifty since Nifty ;-)<br />
void</p>
