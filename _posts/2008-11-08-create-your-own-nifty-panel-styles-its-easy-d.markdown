---
layout: post
status: publish
published: true
title: Create your own Nifty Panel Styles - it's easy! :D
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "To customize and extend the default nifty styles is easy. In this example
  you add your own panel style :)\r\n\r\n<strong>Default Styles<&#47;strong>\r\nNifty
  default styles need to be included using the <useStyle> tag:\r\n<pre lang=\"xml\">
  <!-- include the default styles--><&#47;pre>\r\nTo actual use this Style for a panel
  you use the \"style\" Attribute in the <panel> tag:\r\n<pre lang=\"xml\"> <!-- panel
  with \"nifty-panel\" style --><&#47;pre>\r\nWhen you're using the two (!) lines
  you'll get the well known default \"yellow, red, black\" colored panel:\r\n\r\n[caption
  id=\"\" align=\"aligncenter\" width=\"256\" caption=\"default panel style\"]<a href=\"http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;custom-panels-default-panel.png\"><img
  title=\"default panel style\" src=\"http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;custom-panels-default-panel-thumb.png\"
  alt=\"default panel style\" width=\"256\" height=\"161\" &#47;><&#47;a>[&#47;caption]\r\n\r\n"
wordpress_id: 25
wordpress_url: http://nifty-gui.lessvoid.com/?p=25
date: '2008-11-08 22:08:46 +0100'
date_gmt: '2008-11-08 21:08:46 +0100'
categories:
- docs
tags: []
comments:
- id: 1530
  author: Abu Don
  author_email: avais_shaikh@yahoo.com
  author_url: ''
  date: '2013-02-06 12:04:54 +0100'
  date_gmt: '2013-02-06 11:04:54 +0100'
  content: This is great, customizable. Thanks for this lovely API
- id: 1893
  author: me
  author_email: pfl@greenlogix.eu
  author_url: ''
  date: '2013-06-10 17:56:57 +0200'
  date_gmt: '2013-06-10 16:56:57 +0200'
  content: "well it does not say how to create a custom ontrol\r\n\r\n!--\r\n style
    for a custom panel --\r\n \r\n--&amp; gt;\r\n\r\nhow is this supposed to work\r\n\r\nmaybe
    a web page design problem here\r\n\r\ni try to use  but it does not seem to work"
- id: 1896
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-06-10 21:10:14 +0200'
  date_gmt: '2013-06-10 20:10:14 +0200'
  content: Yes! Sorry aboout that! I think some details of this very old blog post
    have been lost because of some Wordpress-Update :( The topic explained is covered
    in detail in the Nifty manual. See this blog post http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;495
    fot the download link. I think the information provided in the manual should help
    you to fill in the missing pieces!
---
<p>To customize and extend the default nifty styles is easy. In this example you add your own panel style :)</p>
<p><strong>Default Styles<&#47;strong><br />
Nifty default styles need to be included using the <useStyle> tag:</p>
<pre lang="xml"> <!-- include the default styles--><&#47;pre><br />
To actual use this Style for a panel you use the "style" Attribute in the
<panel> tag:</p>
<pre lang="xml"> <!-- panel with "nifty-panel" style --><&#47;pre><br />
When you're using the two (!) lines you'll get the well known default "yellow, red, black" colored panel:</p>
<p>[caption id="" align="aligncenter" width="256" caption="default panel style"]<a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;custom-panels-default-panel.png"><img title="default panel style" src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;custom-panels-default-panel-thumb.png" alt="default panel style" width="256" height="161" &#47;><&#47;a>[&#47;caption]</p>
<p><a id="more"></a><a id="more-25"></a><br />
<strong>Note<&#47;strong><br />
The "nifty-default-styles.xml" was until Version 0.0.4 a part of "nifty.jar". Starting with Version 0.0.5 the default styles will be a part of the seperate "nifty-default-styles.jar".</p>
<p><strong>The Default "nifty-panel" Style<&#47;strong><br />
Here is the complete style definition for the "nifty-panel" style:</p>
<pre lang="xml"><!-- style for a nifty panel --><br />
<!--<br />
  <attributes<br />
    backgroundImage="dialog.png"<br />
    imageMode="resize:32,32,32,32,32,32,32,168,32,32,32,32" &#47;><br />
--><&#47;pre><br />
Not really complicated, eh? :) Besides the funny numbers of course, but we get to those in a minute :)</p>
<p>The "nifty-panel" Style only consists of a background image named "dialog.png". This image is a part of "nifty-default-styles.jar" and looks like this:</p>
<p>[caption id="" align="aligncenter" width="96" caption="dialog.png"]<a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;dialog.png"><img title="dialog.png" src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;dialog.png" alt="dialog" width="96" height="232" &#47;><&#47;a>[&#47;caption]</p>
<p>If you would just use a fixed sized image as the background for your panel you can skip the "imageMode" attribute. But if you need to resize your panel later the default behavior would stretch the image. For some images that could work but when you use rounded edges you would get bad streching like in this example:</p>
<p>[caption id="" align="aligncenter" width="48" caption="bad stretching"]<img title="bad stretching" src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;dialog-stretched.png" alt="bad stretching" width="48" height="110" &#47;>[&#47;caption]</p>
<p>So to kinda work around this limitation you can add the "imageMode" attribute to give Nifty a hint where stretching is possible and where the proportions need to be kept. The "imageMode" Attribute will take 12 arguments:<br />
<code>w1,w2,w3,h1,w4,w5,w6,h2,w7,w8,w9,h3<&#47;code><br />
where "w" means "width" and "h" means "height". Here is an example:</p>
<p>[caption id="" align="aligncenter" width="96" caption="stretching"]<img title="stretching" src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;dialog-sizeable.png" alt="stretching" width="96" height="232" &#47;>[&#47;caption]</p>
<p>Nifty will stretch only the subimages w2, w5 and w8 horizontally when your panel needs to be stretched horizontal and it will change the height of parts w4, w5 and w6 when vertical stretching occurs. All other subimages will stay unchanged and this way we can keep the round edges.</p>
<p><strong>Finally your own Style<&#47;strong><br />
I've made a half transparent Image:</p>
<p>[caption id="" align="aligncenter" width="128" caption="custom panel picture"]<img title="custom panel picture" src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;custom-panel.png" alt="custom panel picture" width="128" height="128" &#47;>[&#47;caption]</p>
<p>Making this into a custom style is pretty easy. You just need to add the image to your resources so that Nifty can find it and you add this to your xml:</p>
<pre lang="xml"><!--<br />
 style for a custom panel --></p>
<p>--><&#47;pre><br />
And thats it :)</p>
<p>Now you just use this Style in your panel and you get a resizable custom panel (Here we have two overlapping panels to show of the transparency too):</p>
<p>[caption id="" align="aligncenter" width="256" caption="custom panel picture"]<a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;custom-panels-custom-panel.png"><img title="custom panel picture" src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;custom-panels-changed-panel-thumb.png" alt="custom panel picture" width="256" height="161" &#47;><&#47;a>[&#47;caption]</p>
<p>Have Fun :D</p>
