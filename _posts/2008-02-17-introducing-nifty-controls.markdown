---
layout: post
status: publish
published: true
title: Introducing Nifty Controls
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 11
wordpress_url: http://nifty-gui.lessvoid.com/archives/11
date: '2008-02-17 15:25:21 +0100'
date_gmt: '2008-02-17 14:25:21 +0100'
categories:
- demo
tags: []
comments: []
---
<p>At the moment there are not many actual controls in Nifty. Besides from clicking on elements, f.i. an Image, there was so far not a lot of interaction possible.</p>
<p>Well, it's about time to change this with Nifty Controls :)</p>
<p>To allow as much freedom as possible I've decided against predefining <code><scrollbar><&#47;scrollbar><&#47;code> new control types that let you only change color or customize some images. Instead the idea is, that you use the already existing elements (Text, Image, Panel) and combine them to create the control you like.</p>
<p>You can create your own control with the <controlDefinition> Tag:</p>
<pre lang="xml">
  <controldefinition name="coolControl" controller="my.package.SimpleSlider"></p>
<panel layout="absolute" backgroundcolor="#f0ff">
      <img id="scrollposition" filename="ball.tga" &#47;><br />
    <&#47;panel><br />
  <&#47;controldefinition><&#47;pre><br />
With this xml t<code><&#47;code>ag you can define a control like it is illustrated in figure 1.</p>
<p><a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;blog-controldefinition.png" title="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;blog-controldefinition.png"><&#47;a></p>
<p style="text-align: center"><a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;blog-controldefinition.png" title="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;blog-controldefinition.png"><img src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;blog-controldefinition-small.png" alt="blog-controldefinition.png" border="0" hspace="10" vspace="10" &#47;><&#47;a><&#47;p><br />
A controldefinition actually defines a couple of things:</p>
<ol>
<li>The name of the new control with the <code>name<&#47;code> attribute.<&#47;li>
<li>The control logic class with the <code>controller<&#47;code> attribute.<&#47;li>
<li>The actual look of the control with a combination of panel, image and text elements. You can even add effects. So if you want a hover effect you can simply add it to the control definition :)<&#47;li><br />
<&#47;ol><br />
With the mandatory attribute "name" you give your new control a name so that you can refer to it later. There's another new tag to use your newly created control, the <code>control<&#47;code> tag:</p>
<pre lang="xml">
  <control name="coolControl"><&#47;control><&#47;pre><br />
So you define the look and feel as well as the control logic once and then use it everywhere you need it like figure 2 shows.</p>
<p><a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;blog-control.png" title="blog-control.png"><&#47;a></p>
<p style="text-align: center" align="left"><a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;blog-control.png" title="blog-control.png"><img src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;blog-control-small.png" alt="blog-control.png" border="0" hspace="10" vspace="10" &#47;><&#47;a><&#47;p><br />
That's all for now. This just covers the basic idea. More on controls later.</p>
<p>Have Fun!</p>
