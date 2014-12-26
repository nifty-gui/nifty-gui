---
layout: post
status: publish
published: true
title: Improved support for dynamically created Controls
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 69
wordpress_url: http://nifty-gui.lessvoid.com/?p=69
date: '2009-03-09 23:02:19 +0100'
date_gmt: '2009-03-09 22:02:19 +0100'
categories:
- bubble
tags: []
comments: []
---
<p>Nifty was meant to read everything from XML files. But sometimes this is not enough because you need to decide from within your code what needs to be created.</p>
<p>This was possible in older versions of Nifty but was kinda tricky.</p>
<p>With Nifty 1.0 (currently available in svn) this has been improved. There are now special classes available to create and configure the build-in elements like Panel, Text, Label and Image.</p>
<p>Example use:  </p>
<pre lang="java5">CreatePanel createPanel = new CreatePanel();<br />
createPanel.setChildLayout("horizontal");<br />
createPanel.setHeight("8px");<br />
createPanel.create(nifty, screen, parent);<&#47;pre></p>
<p>There is now even support to dynamically create your controls with a CreateCustomControl class.</p>
<p>Nifty stuff :)</p>
