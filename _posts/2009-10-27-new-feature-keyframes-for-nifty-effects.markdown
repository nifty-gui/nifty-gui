---
layout: post
status: publish
published: true
title: New Feature - Keyframes for Nifty Effects
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "Well, I'm currently rewriting the old Nifty Introduction Demonstration that
  somehow didn't survive my last blog server switch :( It was <a href=\"http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;9\">posted
  right here<&#47;a> but the jnlp disappear. The demo explained some basic concepts
  of Nifty and it was written in Nifty :) I'm rewritting it now to update it to the
  current Nifty Standards.\r\n\r\nIn the process I just want a moving Nifty GUI Logo
  like this one\r\n\r\n<a href=\"http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;10&#47;tutorial-intro.gif\"><img
  class=\"alignnone size-medium wp-image-109\" title=\"tutorial-intro\" src=\"http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;10&#47;tutorial-intro.gif\"
  alt=\"\" width=\"128\" height=\"96\" &#47;><&#47;a>\r\n\r\nSo the logo should resize
  while fading in and keep on resizing while fading out.\r\n\r\n"
wordpress_id: 108
wordpress_url: http://nifty-gui.lessvoid.com/?p=108
date: '2009-10-27 02:05:40 +0100'
date_gmt: '2009-10-27 01:05:40 +0100'
categories:
- bubble
- design
tags:
- demo
- effects
- introduction
- interpolation
comments: []
---
<p>Well, I'm currently rewriting the old Nifty Introduction Demonstration that somehow didn't survive my last blog server switch :( It was <a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;9">posted right here<&#47;a> but the jnlp disappear. The demo explained some basic concepts of Nifty and it was written in Nifty :) I'm rewritting it now to update it to the current Nifty Standards.</p>
<p>In the process I just want a moving Nifty GUI Logo like this one</p>
<p><a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;10&#47;tutorial-intro.gif"><img class="alignnone size-medium wp-image-109" title="tutorial-intro" src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;10&#47;tutorial-intro.gif" alt="" width="128" height="96" &#47;><&#47;a></p>
<p>So the logo should resize while fading in and keep on resizing while fading out.</p>
<p><a id="more"></a><a id="more-108"></a><br />
My first try was this xml (only the resizing part is shown with the "imageSize" effect attached to "onStartScreen"):</p>
<pre lang="xml"><onStartScreen name="imageSize" startSize="0.7" endSize="1.0" time="600" neverStopRendering="true" &#47;><br />
<onStartScreen name="imageSize" startSize="1.0" endSize="1.3" time="600" neverStopRendering="true" startDelay="600" &#47;><br />
<&#47;pre></p>
<p>Which was reasonable but doesn't work. It doesn't work because effects with a startDelay are in fact already active. In this case the second onStartScreen imageSize effect overwrites the first one with it's intial startSize of 1.0 for the time the startDelay is active. Which, yeah, makes sense for most effects but sucks in this case.</p>
<p>What I'd really want to write is this instead:</p>
<pre lang="xml"><onStartScreen name="imageSize" neverStopRendering="true"><br />
  <value time="0" value="0.7" &#47;><br />
  <value time="600" value="1.0" &#47;><br />
  <value time="1200" value="1.3" &#47;><br />
<&#47;onStartScreen><&#47;pre></p>
<p>Well, and now you can actually write it like this! :D</p>
<p>What this does is performing linear interpolation for of the given values. In the example above the value for the imageSize effect starts with 0.7 and changes to 1.0 over a time of 600ms. After that it continues to be interpolated from 1.0 to 1.3 for the next 600ms. When value tags are present they will overwrite the length parameter of the effect. So in the example the imageSize effect will last for 1200ms.</p>
<p>I've just commited this to svn. It is still work in progress and is therefore only available for the "imageSize" and the "fade" effect. I'm currently thinking about extending it to other effects where it is appropriate. I'm also considering to not only interpolate float values but to make it more general and the actual values are effect specific, so that you could interpolate color values for instance.</p>
<p>void</p>
