---
layout: post
status: publish
published: true
title: Nifty 1.4 has been released
author: void
#  display_name: void
#  login: admin
#  email: void@lessvoid.com
#  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 604
wordpress_url: http://nifty-gui.lessvoid.com/?p=604
date: '2014-07-08 21:04:08 +0200'
date_gmt: '2014-07-08 20:04:08 +0200'
categories:
- Uncategorized
tags: []
comments:
- id: 3475
  author: dinastyao
  author_email: dinastyao@gmail.com
  author_url: http://www.dinastyao.com
  date: '2014-07-13 17:52:58 +0200'
  date_gmt: '2014-07-13 16:52:58 +0200'
  content: "machinesssss!\r\njust don't forget Slick2D ;)"
- id: 3476
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2014-07-13 21:05:19 +0200'
  date_gmt: '2014-07-13 20:05:19 +0200'
  content: You mean, to update its version? Is it available in Maven?
- id: 3479
  author: dinastyao
  author_email: dinastyao@gmail.com
  author_url: http://www.dinastyao.com
  date: '2014-07-16 08:55:07 +0200'
  date_gmt: '2014-07-16 07:55:07 +0200'
  content: "I'm saying that slick2d is still in use and it would be great if you keep
    on going and improving the renderer...\r\nSome time a go I read somewhere that
    some things concerning performance weren't implemented for nifty-slick2d renderer(batchrenderer
    maybe?)\r\n\r\nThanks you for you great work anyway."
- id: 3480
  author: dinastyao
  author_email: dinastyao@gmail.com
  author_url: http://www.dinastyao.com
  date: '2014-07-16 13:20:03 +0200'
  date_gmt: '2014-07-16 12:20:03 +0200'
  content: "Actually I taking about the slick-renderer, just don't leave it behind.\r\nI
    think I read I while ago something about the batchrenderer not been implemented
    for slick. I am just concerned about nifty-gui's performance in our project ;)\r\n\r\nTanks
    a lot for your great work!"
- id: 3483
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2014-07-17 22:09:15 +0200'
  date_gmt: '2014-07-17 21:09:15 +0200'
  content: Does Slick2d still use LWJGL for rendering? If so, I'm sure you can use
    the LWJGL renderer together with Slick2D if all else fails. I'm not a Slick2D
    expert enough to judge if it would actually make sense to implement the https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;blob&#47;1.4&#47;nifty-core&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;render&#47;batch&#47;spi&#47;BatchRenderBackend.java
    using Slick2Ds rendering possibilities...
- id: 3507
  author: max
  author_email: maxip89@freenet.de
  author_url: ''
  date: '2014-07-27 04:28:40 +0200'
  date_gmt: '2014-07-27 03:28:40 +0200'
  content: "hi,\r\ncan you maybe write down the dependencies for this project? Its
    a mess for the people who dont use maven!\r\nregards,\r\nMax"
- id: 3556
  author: 3xp0n3nt
  author_email: aaron@forerunnergames.com
  author_url: http://forerunnergames.com/
  date: '2014-08-04 14:14:25 +0200'
  date_gmt: '2014-08-04 13:14:25 +0200'
  content: '@dinastyao I read your comments just now and have submitted a pull request
    adding batch rendering to slick. That said, you should read the pull request commit
    notes because there are some things you should definitely be aware of, such as
    the fact that Slick 2D is almost completely dead, and uses outdated rendering
    techniques. I highly recommend switching to something else, LibGDX, LWJGL, or
    even JOGL. Good luck!'
- id: 3660
  author: Zanval
  author_email: zanval@aleron.eu
  author_url: http://-
  date: '2014-08-25 12:16:10 +0200'
  date_gmt: '2014-08-25 11:16:10 +0200'
  content: Is the wiki offline? I can't find it.
- id: 3673
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2014-08-31 20:41:52 +0200'
  date_gmt: '2014-08-31 19:41:52 +0200'
  content: 'Unfortunately the sf.net wiki has been discontinued by sf.net and is not
    available anymore. Some parts of the wiki have been rescued to the github wiki
    here: https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;wiki'
---
<p>Nifty 1.4 contains numerous bugfixes and features. However, you should view it as a more stable and grown up Nifty 1.3 and not as something completely new. That being said, there might be some minor compile-time incompatibilities with 1.3.x but it shouldn't be nothing too serious.</p>
<p>Kudos to the following 1.4 committers. Most of the new features like the libGDX renderer (Martin Karing) or a complete refactoring of the batched renderer (Aaron Mahan) - which now even supports multiple texture atlases - was only possible because of the hard work of the following people (alphabetical order):</p>
<p>Aaron Mahan<br />
Alexander Tumin<br />
Guillaume Simard<br />
Illarion Jenkins - CI<br />
Joachim Durchholz<br />
Jonathan Fischer Friberg<br />
Joris van der Wel<br />
Julien Gouesse<br />
Mark<br />
Martin Karing<br />
Stefan Hofmann<br />
Tony Ivanov<br />
Torge Rothe<br />
Xerxes R&aring;nby<br />
Zehao Sun<br />
bgroenks<br />
relu91<br />
vilarion</p>
<p>THANK YOU!</p>
<p>Here are the download links for Nifty 1.4.0:</p>
<p><a href="http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.4&#47;nifty-1.4.0-changelog.txt&#47;download">Nifty 1.4.0 change log (sf.net)<&#47;a><br />
<a href="https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.4&#47;">Nifty 1.4.0 Download Folder at sf.net<&#47;a><br />
<a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;projects&#47;1.4.0&#47;">Nifty 1.4.0 Maven Projects Page (browse the JavaDoc online!)<&#47;a><br />
<a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;tree&#47;nifty-main-1.4.0">Nifty 1.4.0 on github (browse source online)<&#47;a></p>
<p>For all Maven users: Simply add our sf.net Nifty Maven Repo to your pom.xml:</p>
<p><code><repositories><br />
<repository><br />
<id>nifty-maven-repo.sourceforge.net<&#47;id><br />
<url>http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo<&#47;url><br />
<&#47;repository><br />
<&#47;repositories><&#47;code></p>
<p>and upgrade your dependency to 1.4.0:</p>
<p><code><dependency><br />
<groupId>lessvoid<&#47;groupId><br />
<artifactId>nifty<&#47;artifactId><br />
<version>1.4.0<&#47;version><br />
<&#47;dependency><br />
<&#47;code></p>
<p>Have a lot of fun with Nifty 1.4.0! The best Nifty since Nifty ;-)<br />
void</p>
