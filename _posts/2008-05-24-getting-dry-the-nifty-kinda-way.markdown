---
layout: post
status: publish
published: true
title: Getting DRY with style - the nifty kinda way :)
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 16
wordpress_url: http://nifty-gui.lessvoid.com/archives/16
date: '2008-05-24 10:20:33 +0200'
date_gmt: '2008-05-24 09:20:33 +0200'
categories:
- design
tags: []
comments: []
---
<p>Well, no, not "dry" ... but "DRY - (D)on't (R)epeat (Y)ourself" =)</p>
<p>Suppose for some reason you need a lot of red colored panels in your nifty screen. At the moment you'll need to do something like this:</p>
<pre lang="xml"> ...<br />
 ...<br />
 ...<br />
...<br />
 ...<&#47;pre><br />
So far so good, but what if you change your mind and want green panels instead? Ugly - no, not the color ;) - but you'll now have to change all this <code>backgroundColor<&#47;code> definitions. Quite ugly.</p>
<p><strong>Introducing Nifty Styles<&#47;strong><br />
So what we really want, is to specify the color of the panels only one time and use it multiple times.<br />
So with Nifty 0.0.3 you'll be able to use style definitions to get DRY =)</p>
<pre lang="xml"><!--<br />
  <attributes backgroundColor="#f00f"><&#47;attributes><br />
--><br />
 ...<&#47;pre><br />
This way you can define common attributes once and use them multiple times! This principle will be extended to attributes, effects, fonts and so on. This is not only nifty but will naturally lead to UI-skins! Imagine you have all your style definitions in one big "style.xml". So you could define colors, fonts, effects, etc. once in this file. Changing this single file or including another style.xml whould change your whole UI!</p>
<p>Nifty indeed :)</p>
<p>And still work in Progress too =)</p>
