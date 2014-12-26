---
layout: post
status: publish
published: true
title: Separating Styles and Resources from the Main Nifty Jar
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 23
wordpress_url: http://nifty-gui.lessvoid.com/archives/23
date: '2008-10-24 00:30:12 +0200'
date_gmt: '2008-10-23 23:30:12 +0200'
categories:
- design
tags: []
comments: []
---
<p>At the moment the current "nifty.jar" is about 435 KB in size. This is because the jar contains some xml style definitions for the standard nifty controls and resource files like images and fonts. If you won't use the standard controls or images this is a waste of filesize.</p>
<p>So now we've extracted the resources into a separate "nifty-default-styles.jar". The "nifty.jar" now only contains the actual code and is only 280 KB big :)</p>
<p>Now you'll need both jars in your classpath when you want to use the default styles but you can add your own of course.</p>
<p>Currently only available in svn or in the latest 0.0.5-snapshot.</p>
<p>Nifty :)</p>
