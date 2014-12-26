---
layout: post
status: publish
published: true
title: Updated Slick and Lwjgl Library Versions + More Maven Love
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 74
wordpress_url: http://nifty-gui.lessvoid.com/?p=74
date: '2009-03-14 11:11:12 +0100'
date_gmt: '2009-03-14 10:11:12 +0100'
categories:
- bubble
- docs
tags:
- Slick
- maven
- maven repo
- maven repositoy
- lwjgl
comments: []
---
<p>I've now updated the Nifty Dependencies in SVN (for the Nifty 1.0 Release) to the following Versions:</p>
<ul>
<li>Slick #239<&#47;li>
<li>Lwjgl 2.0.1<&#47;li><br />
<&#47;ul><br />
Besides that I realized that I've already had setup a Nifty Maven Repository at Sourceforge. This was meant to store Nifty releases to allow other Projects that use Maven and Nifty to easily access the Nifty libs. So for instance you just need to add:</p>
<pre><repositories><br />
  <repository><br />
&nbsp;&nbsp;  <id>nifty-maven-repo.sourceforge.net<&#47;id><br />
&nbsp;&nbsp;&nbsp; <url>http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo<&#47;url><br />
&nbsp; <&#47;repository><br />
<&#47;repositories><&#47;pre><br />
to your applications pom.xml and it will automatically find the Nifty dependency :) So far so good.</p>
<p>BUT</p>
<p>Today I realized that I could use the same approach to get Lwjgl and Slick2D easily under Maven control. So when switching the Versions to Slick #239 und Lwjgl 2.0.1 I deployed both libs to the Nifty Maven Repo at Sourceforge! :D</p>
<p>So what does this mean for you?</p>
<p>It means just one thing. It's now easier then ever to build Nifty! Just "svn co" it from Sourceforge and execute "mvn package" and it should automatically download all required libs without any changes!</p>
<p>Nifty! :D</p>
