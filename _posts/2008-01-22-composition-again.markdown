---
layout: post
status: publish
published: true
title: Composition again...
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 10
wordpress_url: http://nifty-gui.lessvoid.com/archives/10
date: '2008-01-22 01:12:22 +0100'
date_gmt: '2008-01-22 00:12:22 +0100'
categories:
- design
tags: []
comments: []
---
<p>In a previous blog post I've talked about that <a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;6" title="composition&#47;delegation is a good thing">composition&#47;delegation has some benefits over inheritance<&#47;a>. Today I realized that I actually was right ;)</p>
<p>As I was playing around in Nifty I've noticed that my TextElements don't support coloured backgrounds. As a matter of fact the TextRenderer just didn't support it. The first thought was to simply add this feature. But then I realized that the missing functionality was right there in front of me. I already implemented backgroundColor (and even backgroundImage) for Panels in the PanelRenderer.</p>
<p>So it occurred to me that I only have to combine them both in some nifty <strong>composition <&#47;strong>;) and be done.</p>
<p>So I've just changed the Element class to support not only a single ElementRenderer but several! And by the way I discovered the "varargs declaration" in Java:</p>
<pre lang="java5">public Element(<br />
    final String newId,<br />
    final Element newParent,<br />
    final ElementRenderer ... newElementRenderer) {<br />
  ...<br />
}<&#47;pre><br />
You can use it this way:</p>
<pre lang="java5">Element element = new Element("myId", parent, new PanelRenderer(), new TextRenderer());<&#47;pre><br />
Nifty indeed :)</p>
