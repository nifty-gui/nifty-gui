---
layout: post
status: publish
published: true
title: Switching images with nifty effects in game
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 112
wordpress_url: http://nifty-gui.lessvoid.com/?p=112
date: '2009-11-01 18:04:11 +0100'
date_gmt: '2009-11-01 17:04:11 +0100'
categories:
- Uncategorized
tags: []
comments:
- id: 343
  author: Steven
  author_email: smovesmit@gmail.com
  author_url: ''
  date: '2009-11-04 22:39:40 +0100'
  date_gmt: '2009-11-04 21:39:40 +0100'
  content: "Hi void, \r\n\r\nI really wanna use nifty, but unfortunately I keep getting
    an error.\r\nThe problem seems to be with \r\nNifty nifty = new Nifty(\r\n    new
    RenderDeviceLwjgl(),\r\n    new SoundSystem(new SlickSoundLoader()), &#47;&#47;Here
    is the problem. Can't find this class\r\n    new TimeProvider());\r\n\r\nI don't
    know what to do!?"
---
<p>Someone over at the nifty forums asked how to switch images from java. You can <a href="https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;forums&#47;forum&#47;807893&#47;topic&#47;3445518">find the thread and my answer here.<&#47;a></p>
<p>Well. So far so good but switching images can be a lot more exciting with nifty effects :)</p>
<p>So let's start with adding a new effect and attach it to the image with the onCustom effect:</p>
<pre class="brush:xml"><image id="inventar" filename="icon1.png"><br />
  <effect><br />
    <onCustom name="imageSize" startSize="1.7" endSize="1.0" length="150" &#47;><br />
  <&#47;effect><br />
<&#47;image><&#47;pre><br />
This adds an "imageSize" effect that starts with a imagesize of 1.7 times the original size and resizes the image back to the original size over the time of 150 ms. The "onCustom" effect means, that we can trigger this effect from java.</p>
<p>So when it comes time to change the image to the rocket launcher, the plasma gun or the BFG9000 :D we can use the following java code to trigger the effect and change the image:</p>
<pre class="brush:java">getElement("inventar").getRenderer(ImageRenderer.class).setImage(iconRocketLauncher)<br />
getElement("inventar").startEffect(EffectEventId.onCustom);<&#47;pre><br />
The first line changes the image and the second line triggers the onCustom effect.</p>
<p>I've updated the slick overlay example with a little nifty image switching in the right upper border of the screen. You can change the different images with pressing the keys "a", "b" and "c".</p>
<p>You can find it here:</p>
<p><a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-slick-overlay-demo.jnlp">http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-slick-overlay-demo.jnlp<&#47;a></p>
<p>and you can find the updated example in svn or you can browse it online here:</p>
<p><a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples-slick&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;examples&#47;slick&#47;niftyoverlay&#47;">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;examples&#47;slick&#47;niftyoverlay&#47;<&#47;a> (Java classes)</p>
<p><a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples-slick&#47;trunk&#47;src&#47;main&#47;resources&#47;slick&#47;niftyoverlay&#47;overlay.xml">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;resources&#47;slick&#47;niftyoverlay&#47;overlay.xml?revision=534&amp;view=markup<&#47;a> (Nifty XML for the Screen - overlay.xml)</p>
<p>Nifty! :D</p>
