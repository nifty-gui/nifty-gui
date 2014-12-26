---
layout: post
status: publish
published: true
title: LWJGL Renderer updated to LWJGL 2.8.1
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 370
wordpress_url: http://nifty-gui.lessvoid.com/?p=370
date: '2011-11-06 19:27:30 +0100'
date_gmt: '2011-11-06 18:27:30 +0100'
categories:
- Uncategorized
tags: []
comments:
- id: 1010
  author: arielsan
  author_email: ariel.coppes@gemserk.com
  author_url: http://blog.gemserk.com
  date: '2011-11-07 01:21:02 +0100'
  date_gmt: '2011-11-07 00:21:02 +0100'
  content: Glad that it worked so well :D
- id: 1074
  author: Jeff
  author_email: jfelrod1960@gmail.com
  author_url: ''
  date: '2011-12-01 17:48:09 +0100'
  date_gmt: '2011-12-01 16:48:09 +0100'
  content: Any plans to improve performance of nifty gui to run in a small environment
    such as android?
- id: 1078
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2011-12-01 23:35:18 +0100'
  date_gmt: '2011-12-01 22:35:18 +0100'
  content: |-
    Yes and no :)

    Well, Android was not a target plattform while developing Nifty. This being said, Nifty 1.3.1 improved especially GC performance a lot and this version of Nifty should therefore work a lot better on Android too. It's not available yet but you can try it using a nightly build.

    On the other hand there have been movements to bring JME3 (and the Nifty that comes with JME3) to Android. I'm not sure what the current state of this is though but I'm sure that you can find more informations about it on the jmonkeyengine websites.
- id: 1080
  author: Jeff
  author_email: jfelrod1960@gmail.com
  author_url: ''
  date: '2011-12-02 01:00:06 +0100'
  date_gmt: '2011-12-02 00:00:06 +0100'
  content: Thanks void!  I appreciate your time.
---
<p>Thanks to the work of <a href="http:&#47;&#47;blog.gemserk.com&#47;2011&#47;10&#47;22&#47;lwjgl-on-maven-central&#47;">Gemserk<&#47;a> LWJGL is now available in the central Maven repo!</p>
<p>So, switching to a new LWJGL version was actually very easy. I've just changed the existing dependencies in the pom.xml for the nifty-lwjgl-renderer project to:</p>
<pre class="brush:xml"><dependency><br />
  <groupId>org.lwjgl.lwjgl<&#47;groupId><br />
  <artifactId>lwjgl<&#47;artifactId><br />
  <version>2.8.1<&#47;version><br />
<&#47;dependency><br />
<dependency><br />
  <groupId>org.lwjgl.lwjgl<&#47;groupId><br />
  <artifactId>lwjgl_util<&#47;artifactId><br />
  <version>2.8.1<&#47;version><br />
<&#47;dependency><&#47;pre></p>
<p>and that's all! :D</p>
<p>As an additional benefit they created a <a href="http:&#47;&#47;lwjgl.org&#47;wiki&#47;index.php?title=LWJGL_use_in_Maven">natives plugin<&#47;a> that will unpack all of the LWJGL natives in the target&#47;natives directory. When combined with an <a href="http:&#47;&#47;code.google.com&#47;p&#47;mavennatives&#47;">eclipse plugin<&#47;a> this will even add the natives to the native library location inside of Eclipse and ... that's all! :D</p>
<p>I've added it to the nifty-examples pom.xml and now you don't need to manually specify the "-Djava.library.path=
<path-to-lwjgl>" setting when you run any of the examples anymore! Works pretty well!</p>
<p><strong>Great work Gemserk!<&#47;strong><br />
void</p>
