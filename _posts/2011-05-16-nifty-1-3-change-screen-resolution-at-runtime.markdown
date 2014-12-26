---
layout: post
status: publish
published: true
title: Nifty 1.3 Change Screen Resolution at Runtime
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 269
wordpress_url: http://nifty-gui.lessvoid.com/?p=269
date: '2011-05-16 23:08:32 +0200'
date_gmt: '2011-05-16 22:08:32 +0200'
categories:
- Uncategorized
tags: []
comments: []
---
<p>Finally the open issues and feature requests that have been scheduled for 1.3 are nearly done ... erm, for real this time :D</p>
<p>One of the latest additions is the possibility to change the screen resolution at runtime without restarting a Screen or Nifty. All you need to do is to change the display mode of the rendering system you're using and then simply call:</p>
<pre class="brush:java">nifty.resolutionChanged();<&#47;pre></p>
<p>to notify Nifty of the new display resolution. Nifty will finish the rendering of the current frame and then it will update the current Screen to the new resolution as well as notifies all active controls.</p>
<p>You can find some details for this on the <a href="http:&#47;&#47;sourceforge.net&#47;tracker&#47;?func=detail&aid=3005778&group_id=223898&atid=1059825">feature request tracker<&#47;a>.</p>
<p>And here is a little video that is demonstrating that it is actually working ;)</p>
<p><iframe src="http:&#47;&#47;player.vimeo.com&#47;video&#47;23824855?color=FF7700" width="640" height="480" frameborder="0"><&#47;iframe></p>
<p>void</p>
