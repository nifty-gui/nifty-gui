---
layout: post
status: publish
published: true
title: New effect "gradient" Available
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 116
wordpress_url: http://nifty-gui.lessvoid.com/?p=116
date: '2009-12-06 19:19:48 +0100'
date_gmt: '2009-12-06 18:19:48 +0100'
categories:
- Uncategorized
tags: []
comments: []
---
<p>There is a new Effect available in SVN now. The "gradient" Effect allows you to draw vertical or horizontal gradients. You can use different colors and a percentage or pixel value for the position where the specific color should be.</p>
<p>Example:</p>
<pre lang="xml"><onActive name="gradient" direction="vertical"><br />
  <value offset="0%" color="#f00f" &#47;><br />
  <value offset="20%" color="#0f0f" &#47;><br />
  <value offset="60%" color="#00ff" &#47;><br />
  <value offset="70%" color="#ff0f" &#47;><br />
  <value offset="100%" color="#ffff" &#47;><br />
<&#47;onActive><&#47;pre></p>
<p>Result:</p>
<p><a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;12&#47;gradient.png"><img class="alignnone size-medium wp-image-117" title="Gradient Example" src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;12&#47;gradient.png" alt="" width="267" height="200" &#47;><&#47;a></p>
<p>Awesome! :D</p>
<p>You can use different Alpha Values for the colors too to make a gradient overlay elements or you could change the blendMode to multiply before applying the gradient with the "blendMode" effect :)</p>
<p>Have Fun!<br />
void :)</p>
