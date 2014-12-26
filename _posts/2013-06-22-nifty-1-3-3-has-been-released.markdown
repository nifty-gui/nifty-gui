---
layout: post
status: publish
published: true
title: Nifty 1.3.3 has been released
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "Nifty 1.3.3 is mainly a bugfix release and is compatible with older Nifty
  1.3.x versions.\r\n\r\nThe main new feature is the batched renderer for improved
  rendering speed of more complex GUIs (with many elements) and OpenGL Core Profile
  support (in native LWJGL&#47;JOGL renderer).\r\n\r\nUsage of the batched renderer
  is easy. Instead of your regular RenderDevice implementation you use the new BatchRenderDevice
  that Nifty already provides together with the BatchRendererBackend implementation
  and the size of the texture atlas. The following examples all use a texture atlas
  of 2048x2048 pixels which worked very well for all Nifty standard examples.\r\n\r\nExamples:\r\n"
wordpress_id: 562
wordpress_url: http://nifty-gui.lessvoid.com/?p=562
date: '2013-06-22 01:29:36 +0200'
date_gmt: '2013-06-22 00:29:36 +0200'
categories:
- Uncategorized
tags: []
comments:
- id: 2165
  author: atomix
  author_email: hn.sgmedia@gmail.com
  author_url: ''
  date: '2013-07-22 08:42:24 +0200'
  date_gmt: '2013-07-22 07:42:24 +0200'
  content: "Hi void, \r\n\r\nI'm @atomix from jME forum.\r\n\r\n I picked up the Nifty
    Editor from @relucri and port to the jME SDK. I want to suggest additional improvements
    since i'm developing those tools and want to corporate tight with the lastest
    features and changes.\r\n\r\nIf you've read this please reply to my email. Really
    appreciate that!\r\n\r\nRegards,"
- id: 2259
  author: mifth
  author_email: paulgeraskin@gmail.com
  author_url: ''
  date: '2013-08-27 22:00:17 +0200'
  date_gmt: '2013-08-27 21:00:17 +0200'
  content: Great news! Thank you a lot!
---
<p>Nifty 1.3.3 is mainly a bugfix release and is compatible with older Nifty 1.3.x versions.</p>
<p>The main new feature is the batched renderer for improved rendering speed of more complex GUIs (with many elements) and OpenGL Core Profile support (in native LWJGL&#47;JOGL renderer).</p>
<p>Usage of the batched renderer is easy. Instead of your regular RenderDevice implementation you use the new BatchRenderDevice that Nifty already provides together with the BatchRendererBackend implementation and the size of the texture atlas. The following examples all use a texture atlas of 2048x2048 pixels which worked very well for all Nifty standard examples.</p>
<p>Examples:<br />
<a id="more"></a><a id="more-562"></a></p>
<p><strong>LWJGL Example:<&#47;strong></p>
<pre class="brush:java">BatchRenderDevice renderDevice = new BatchRenderDevice(<br />
new LwjglBatchRenderBackend(), 2048, 2048);<&#47;pre></p>
<p><strong>LWJGL Core Profile Example:<&#47;strong></p>
<pre class="brush:java">BatchRenderDevice renderDevice = new BatchRenderDevice(<br />
new LwjglBatchRenderBackendCoreProfile(), 2048, 2048);<&#47;pre></p>
<p>And if you prefer JOGL (which we've updated and added new example code for as well):</p>
<p><strong>JOGL Example:<&#47;strong></p>
<pre class="brush:java">BatchRenderDevice renderDevice = new BatchRenderDevice(<br />
new JoglBatchRenderBackend(), 2048, 2048);<&#47;pre></p>
<p><strong>JOGL Core Profile Example:<&#47;strong></p>
<pre class="brush:java">BatchRenderDevice renderDevice = new BatchRenderDevice(<br />
new JoglBatchRenderBackendCoreProfile(), 2048, 2048);<&#47;pre></p>
<p>Usage in jMonkeyEngine is even easier. You just have to call the constructor of the NiftyJmeDisplay with two additional parameters (the width and height of the texture atlas you'd like to use):</p>
<p><strong>jMonkeyEngine Example:<&#47;strong></p>
<pre class="brush:java">    niftyDisplay = new NiftyJmeDisplayTime(<br />
        assetManager,<br />
        inputManager,<br />
        audioRenderer,<br />
        guiViewPort,<br />
        2048, 2048); &#47;&#47; add these two to enable the batched renderer<&#47;pre></p>
<p>As always it's up to you to initialize LWJGL&#47;JOGL before initializing Nifty but you can find code on how to do that in the nifty-examples project.</p>
<p>If you'd like to learn more about the batched renderer and how it works you can find out more about it in this two part blog post:</p>
<p><a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;505">Inside Niftys RenderDevice and how to speed it up (Part 1&#47;2)<&#47;a><br />
<a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;530">Inside Niftys RenderDevice and how to speed it up (Part 2&#47;2)<&#47;a></p>
<p>And here are the other Nifty links:</p>
<p><a href="http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.3.3&#47;nifty-1.3.3-changelog.txt&#47;download">Nifty 1.3.3 change log (sf.net)<&#47;a><br />
<a href="https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.3.3&#47;">Nifty 1.3.3 Download Folder at sf.net<&#47;a><br />
<a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;projects&#47;1.3.3&#47;">Nifty 1.3.3 Maven Projects Page (browse the JavaDoc online!)<&#47;a><br />
Get a nightly jME3 build with Nifty 1.3.3 (starting tommorow or so ;) since it was just been added with <a href="http:&#47;&#47;code.google.com&#47;p&#47;jmonkeyengine&#47;source&#47;detail?r=10656">this commit<&#47;a> to the jme repo)</p>
<p>For all Maven users: Simply add our sf.net Nifty Maven Repo to your pom.xml:</p>
<pre class="brush:xml">  <repositories><br />
    <repository><br />
      <id>nifty-maven-repo.sourceforge.net<&#47;id><br />
      <url>http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo<&#47;url><br />
    <&#47;repository><br />
  <&#47;repositories><&#47;pre></p>
<p>and upgrade your dependency to 1.3.3:</p>
<pre class="brush:xml">    <dependency><br />
      <groupId>lessvoid<&#47;groupId><br />
      <artifactId>nifty<&#47;artifactId><br />
      <version>1.3.3<&#47;version><br />
    <&#47;dependency><&#47;pre></p>
<p>Have a lot of fun with Nifty 1.3.3! The best Nifty since Nifty ;-)<br />
void</p>
