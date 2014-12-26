---
layout: post
status: publish
published: true
title: Merry Christmas!
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 52
wordpress_url: http://nifty-gui.lessvoid.com/?p=52
date: '2008-12-23 20:59:26 +0100'
date_gmt: '2008-12-23 19:59:26 +0100'
categories:
- demo
tags: []
comments:
- id: 303
  author: Mac
  author_email: mac.williams@hotmail.com
  author_url: ''
  date: '2008-12-28 21:52:51 +0100'
  date_gmt: '2008-12-28 20:52:51 +0100'
  content: "Hi mate! This framework is really great! I have one question though, how
    can i disable or change the nifty level logging? I tried with a log4j.properties
    in the classpath but that didn't work. (I have commons-logging and log4j).\r\n\r\nBy
    the way, any news on the new version? I'm looking forward to use the checkboxes
    and the new skin! ;)\r\n\r\nThanks and Happy new year!\r\n\r\n--------------------------\r\n\r\nHi
    Mac,\r\n\r\nNifty is using the Java 1.4 Logging. Commons-Logging or Log4j have
    been voted out by some people because of webstart and applet size &#47; loading
    time considerations (I started using commons-logging but it simply was not worth
    the additional size).\r\n\r\nThis should disable everything but warnings with
    Java 1.4 Logging for all Nifty classes:\r\n Logger.getLogger(\"de.lessvoid\").setLevel(\"de.lessvoid\",Level.WARN);\r\n\r\nThe
    upcoming 1.0 Release of Nifty (current svn) has cleaned up logging too :)\r\n\r\nI'm
    not sure on the release date yet. I've planned next weekend but I want to make
    sure that everything is nice and clean so maybe early next year would probably
    be more realistic :)\r\n\r\nYou could create your own skin with the current nifty
    too and the cool checkmark effect would be possible to add with current Nifty
    features too ^^\r\n\r\nBest Regards,\r\nvoid "
- id: 305
  author: Mac
  author_email: mac.williams@hotmail.com
  author_url: ''
  date: '2008-12-30 01:59:43 +0100'
  date_gmt: '2008-12-30 00:59:43 +0100'
  content: "Thanks void! I will try what you said (I download the code from the svn
    to test but some things doesn't work, no problem I know you are working hard ;)\r\n\r\nHappy
    new year!\r\n\r\n-----\r\n\r\nIn fact I am working hard on it. Hang on ;)\r\n\r\nHappy
    new year to you too!\r\n\r\nPS: Your email adress is broken ;)"
---
<p>Here is a quick preview of the new Default Controls and the new Nifty 1.0 Style.</p>
<p>Note the awesome effects on the checkmark when I toggle the Checkbox ;) and watch the Keyboard only usage in the second part of the video :)</p>
<p><object width="636" height="480"><param name="allowfullscreen" value="true" &#47;><param name="allowscriptaccess" value="always" &#47;><param name="movie" value="http:&#47;&#47;vimeo.com&#47;moogaloop.swf?clip_id=2613406&amp;server=vimeo.com&amp;show_title=1&amp;show_byline=1&amp;show_portrait=0&amp;color=ffffff&amp;fullscreen=1" &#47;><embed src="http:&#47;&#47;vimeo.com&#47;moogaloop.swf?clip_id=2613406&amp;server=vimeo.com&amp;show_title=1&amp;show_byline=1&amp;show_portrait=0&amp;color=ffffff&amp;fullscreen=1" type="application&#47;x-shockwave-flash" allowfullscreen="true" allowscriptaccess="always" width="636" height="480"><&#47;embed><&#47;object><br &#47;><a href="http:&#47;&#47;vimeo.com&#47;2613406">Nifty GUI New Style and Control Preview<&#47;a> from <a href="http:&#47;&#47;vimeo.com&#47;user1070526">void<&#47;a> on <a href="http:&#47;&#47;vimeo.com">Vimeo<&#47;a>.</p>
<p>Nifty 1.0 - probably available this year! :o)</p>
<p>Have a Merry Christmas and watch out for the upcoming Nifty GUI Release!</p>
