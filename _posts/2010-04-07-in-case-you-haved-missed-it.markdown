---
layout: post
status: publish
published: true
title: In case you haved missed it ...
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 132
wordpress_url: http://nifty-gui.lessvoid.com/?p=132
date: '2010-04-07 21:50:03 +0200'
date_gmt: '2010-04-07 20:50:03 +0200'
categories:
- Uncategorized
tags: []
comments:
- id: 439
  author: lutherion
  author_email: hluther80@gmail.com
  author_url: ''
  date: '2010-07-10 20:49:25 +0200'
  date_gmt: '2010-07-10 19:49:25 +0200'
  content: "Hi all,\r\n\r\ni tried to get nifty working but it seems to be very difficult.\r\n\r\nI
    have a ListBox with items and can only get if there is a mouseclick (over the
    method invoke), but not which item or something(seems to be no params are allowed).
    Furthermore there are no abstract classes or interfaces to implement so the only
    way is to read all the sources and try. A tutorial would be nice, and i mean not
    the web start tutorial which looks nice, for real programming i need a practical
    \"how to\", which i can't find on the inet. Nifty looks very nice, but seems to
    be difficult to extent and use, especially for newbies. Go on and thx for the
    time to spend for the real good looking stuff."
---
<p>... some very interessting developments are on the way:</p>
<p><a href="http:&#47;&#47;jmonkeyengine.com&#47;blog&#47;blog&#47;2010&#47;04&#47;06&#47;jmonkeyengine-3-0-gets-a-very-nifty-gui&#47;">http:&#47;&#47;jmonkeyengine.com&#47;blog&#47;blog&#47;2010&#47;04&#47;06&#47;jmonkeyengine-3-0-gets-a-very-nifty-gui&#47;<&#47;a></p>
<p><a href="http:&#47;&#47;www.jmonkeyengine.com&#47;forum&#47;index.php?topic=11246.msg98829#msg98829">http:&#47;&#47;www.jmonkeyengine.com&#47;forum&#47;index.php?topic=11246.msg98829#msg98829<&#47;a></p>
<p>:)</p>
<p>And besides that, the current Nifty SVN has all dependencies to LWJGL and Slick2D removed! :D Nifty is now completly independent of the actual rendering system!</p>
<p>There is a lwjgl-slick2d renderer already available here:<br />
<a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-lwjgl-slick-renderer&#47;trunk&#47;">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-lwjgl-slick-renderer&#47;trunk&#47;<&#47;a></p>
<p>There is a work in progress JME2 renderer available here:<br />
<a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-jme-renderer&#47;trunk&#47;">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-jme-renderer&#47;trunk&#47;<&#47;a></p>
<p>And the friendly people over at <a href="http:&#47;&#47;www.jmonkeyengine.com&#47;">http:&#47;&#47;www.jmonkeyengine.com&#47;<&#47;a> are already working on a JME3 nifty renderer too! :)</p>
<p>If you want to create your own Nifty renderer for some other rendering system, well, you can do that now too!</p>
<p>Take a look at the <a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;spi&#47;">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;spi&#47;<&#47;a> ServiceProviderInterface for Nifty rendering. What about a Java2D implementation? :D</p>
<p>void</p>
