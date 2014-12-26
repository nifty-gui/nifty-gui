---
layout: post
status: publish
published: true
title: Nifty Snapshots are now available
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 114
wordpress_url: http://nifty-gui.lessvoid.com/?p=114
date: '2009-12-06 19:00:56 +0100'
date_gmt: '2009-12-06 18:00:56 +0100'
categories:
- Uncategorized
tags: []
comments: []
---
<p>To get access to the latest and greates Nifty, Snapshots (= Development Versions) are now available at the Nifty Maven Repository I've set up at sourceforge.net.</p>
<p>You can get the latest Snapshot here: <a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo&#47;lessvoid&#47;nifty&#47;1.1-SNAPSHOT&#47;">http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo&#47;lessvoid&#47;nifty&#47;1.1-SNAPSHOT&#47;<&#47;a></p>
<p>In case you're using Maven you've probably already using this in your pom.xml:</p>
<pre lang="xml"><repositories><br />
  <repository><br />
    <id>nifty-maven-repo.sourceforge.net<&#47;id><br />
    <url>http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo<&#47;url><br />
  <&#47;repository><br />
<&#47;repositories><&#47;pre></p>
<p>You only need to change your nifty dependency to snapshot and you're always using the latest Nifty Build:</p>
<pre lang="xml"><dependency><br />
  <groupId>lessvoid<&#47;groupId><br />
  <artifactId>nifty<&#47;artifactId><br />
  <version>1.1-SNAPSHOT<&#47;version><br />
<&#47;dependency><&#47;pre></p>
<p>Good Luck and Have Fun!<br />
void</p>
