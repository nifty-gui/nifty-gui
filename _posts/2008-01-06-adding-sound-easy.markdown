---
layout: post
status: publish
published: true
title: Adding Sound - Easy!
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 8
wordpress_url: http://nifty-gui.lessvoid.com/archives/8
date: '2008-01-06 21:32:12 +0100'
date_gmt: '2008-01-06 20:32:12 +0100'
categories:
- design
tags: []
comments: []
---
<p>The feature to play soundeffects and probably music in Nifty was in the back of my head from the very beginning. And today I've added it and it worked like a breeze :)</p>
<p>Well, at first I went for something like adding it parallel to visual effects. Something like that:</p>
<pre lang="xml">
<element><br />
  ...<br />
  <effect><br />
    <onstartscreen name="fade"><&#47;onstartscreen><br />
  <&#47;effect><br />
  <onclicksound name="bleep"><&#47;onclicksound><br />
<&#47;element><&#47;pre><br />
But then it  occurred to me, that a sound *effect* is only a special kind of an effect! So I've quickly added a new kind of an effect: "PlaySound" and voila:</p>
<pre lang="xml">
<element><br />
  ...<br />
  <effect><br />
    <onstartscreen name="fade"><&#47;onstartscreen><br />
    <onclick name="playSound" sound="bleep"><&#47;onclick><br />
  <&#47;effect><br />
<&#47;element><&#47;pre><br />
Feels good. Looks good. Is good. Was very quick to implement too! :)</p>
<p>Sometimes life is good! :D</p>
