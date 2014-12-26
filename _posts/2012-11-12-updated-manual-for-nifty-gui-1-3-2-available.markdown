---
layout: post
status: publish
published: true
title: Updated Manual for Nifty GUI 1.3.2 available
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 495
wordpress_url: http://nifty-gui.lessvoid.com/?p=495
date: '2012-11-12 23:23:01 +0100'
date_gmt: '2012-11-12 22:23:01 +0100'
categories:
- docs
tags: []
comments:
- id: 1727
  author: Dionysius
  author_email: dragon.dionysius@gmail.com
  author_url: ''
  date: '2013-04-30 18:20:55 +0200'
  date_gmt: '2013-04-30 17:20:55 +0200'
  content: "I have to say this manual is really worth to read. There are a lot things
    very well explained. But I miss a way to animate an image itself. Using nifty
    and animate is difficult to find that what I want.\r\n\r\nI'd like to have a animated
    loading image (while waiting for server accepting my connection), like they exist
    on almost every page using ajax calls. Do you know a way to do that in nifty?
    I'm currently using the slick's StateBased implementation.\r\n\r\nMy idea would
    be like: using a sprited image and load it using imageMode=\"animate:w-sprite,h-sprite,interval-ms\",
    but if there's a way to do it I'd like to know ;)\r\n\r\n-----\r\n\r\nAbout the
    manual - or nifty itself - some things I miss (these are just indicators for the
    next manual version, I worked around all these issues somehow):\r\n- How can I
    modify styles in Runtime\r\n- How could a font size be set in xml, when using
    .ttf&#47;.odf directly\r\n- Passing a style with font in it gives error when using
    in a control\r\n\r\nI had many different issues when I tried to support different
    resolutions (e.g. open nifty-examples in FullHD resolution and you'll see what
    I mean). heights are too small, fonts too small, paddings too short. It would
    be nice, if every element property size is flexible depending on a value from
    parent or children.\r\n\r\nMy current ugly workaround: everything optimized for
    FullHD resolution and then scaling it down if someone has a lower resolution or
    wants to use windowed mode. That means, everything in around the same aspect ration
    looks well. But hey, today almost every gamer has a widescreen display."
- id: 1730
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-04-30 21:30:10 +0200'
  date_gmt: '2013-04-30 20:30:10 +0200'
  content: |-
    Unfortunatly amost everything you named are currently missing feature in Nifty ;) Most of them not that easy to fix&#47;add. You might want to add them to the github issue tracker so that they don't get lost. It's much easier to reply&#47;work on things there.

    Lack of animation support is one of those missing things. I'd suggest using a custom effect for this as a workaround. You could add a couple of images to your custom effect, add your effect to the onActive slot and then basically just display individual images in time. Not very comfortable tho but might work. Lack of a format to store animation was the biggest issue when I looked into that some time ago. But somehow I never got around to add that.
---
<p>The updated Manual for Nifty GUI 1.3.2 is now available as a sf.net file download: <a href="http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.3.2&#47;nifty-gui-the-manual-1.3.2.pdf&#47;download">nifty-gui-the-manual-1.3.2.pdf<&#47;a></p>
<p>[caption id="attachment_496" align="aligncenter" width="300" caption="Nifty GUI Manual 1.3.2"]<a href="http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.3.2&#47;nifty-gui-the-manual-1.3.2.pdf&#47;download"><img src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2012&#47;11&#47;Bildschirmfoto-2012-11-12-um-23.04.59-300x211.png" alt="" title="Nifty GUI Manual 1.3.2" width="300" height="211" class="size-medium wp-image-496" &#47;><&#47;a>[&#47;caption]</p>
<p>The changes are marked in the document but here is a summary of everthing that has been added:
<ul>
<li>Grammar and spelling corrections thanks to wezrule<&#47;li>
<li>Get Nifty Version String Feature Description<&#47;li>
<li>(Short) description of imageMode="subImageDirect:x,y,w,h" Feature<&#47;li>
<li>Padding feature description including lots of examples with screenshots<&#47;li>
<li>Margin feature description<&#47;li>
<li>Renderorder feature description<&#47;li>
<li>Nifty event consuming and disable flags description<&#47;li>
<li>Description of general mouse event processing changes in Nifty 1.3.2<&#47;li>
<li>Dynamically changing effect parameters example<&#47;li><br />
<&#47;ul></p>
<p>:)<br />
void</p>
