---
layout: post
status: publish
published: true
title: Inside Niftys RenderDevice and how to speed it up (Part 1&#47;2)
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "When I started Nifty my mindset was like, \"well, games can render millions
  of polys each frame so throwing a couple of hundred textured polys at the GPU shouldn't
  hurt performance that much\".\r\n\r\nWell, I was wrong.\r\n\r\nTo achieve a somewhat
  high performance you still have to play by the GPU rules. Simply throwing polys
  at the GPU and expect the best rendering performance doesn't really work. In this
  two part series of blog posts I'll try to explain what basically sucks in the current
  way we render things and what we've done in the last couple of months to achieve
  better rendering performance.\r\n\r\nThere is of course always room for more improvement.
  One thing I'd like to tackle in the future and especially in Nifty 2.0 would be
  to render only the parts of a scene that have changed. Since the best performance
  you can ever get is not to render :) But since this is a bit more involved for now
  we're stuck with the \"render the whole GUI each frame\" approach of current generation
  Nifty. BUT at least we can make Nifty render fast. Very fast.\r\n"
wordpress_id: 505
wordpress_url: http://nifty-gui.lessvoid.com/?p=505
date: '2013-03-10 19:32:13 +0100'
date_gmt: '2013-03-10 18:32:13 +0100'
categories:
- design
tags: []
comments:
- id: 1583
  author: HariboTer
  author_email: olafungabunga@yahoo.de
  author_url: ''
  date: '2013-03-10 19:47:25 +0100'
  date_gmt: '2013-03-10 18:47:25 +0100'
  content: "Well, better performance is always a desirable thing :D Thanks for this
    interesting information :)\r\n\r\n(Btw, the top text row of the comment panel
    I'm currently typing this in is overlayed by a white \"Comments\" text which is
    quite irritating. Just so you know^^)"
- id: 1584
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-03-10 19:58:30 +0100'
  date_gmt: '2013-03-10 18:58:30 +0100'
  content: Thanks! Can you make a screenshot of the issue please? The comment panel
    looks ok here :&#47;
- id: 1665
  author: Dennis
  author_email: Test@IRC-Mania.de
  author_url: ''
  date: '2013-04-22 00:08:33 +0200'
  date_gmt: '2013-04-21 23:08:33 +0200'
  content: Well done !
- id: 1669
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-04-22 21:18:12 +0200'
  date_gmt: '2013-04-22 20:18:12 +0200'
  content: Thanks Mr. S ;)
- id: 1728
  author: Dionysius
  author_email: dragon.dionysius@gmail.com
  author_url: ''
  date: '2013-04-30 18:28:08 +0200'
  date_gmt: '2013-04-30 17:28:08 +0200'
  content: "http:&#47;&#47;oi41.tinypic.com&#47;20svghy.jpg\r\nversus\r\nhttp:&#47;&#47;oi40.tinypic.com&#47;5etvf7.jpg"
- id: 1729
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-04-30 21:25:09 +0200'
  date_gmt: '2013-04-30 20:25:09 +0200'
  content: Thanks! Looks odd. Not sure what I can do about that tho :&#47; Does it
    happen in all browsers?
---
<p>When I started Nifty my mindset was like, "well, games can render millions of polys each frame so throwing a couple of hundred textured polys at the GPU shouldn't hurt performance that much".</p>
<p>Well, I was wrong.</p>
<p>To achieve a somewhat high performance you still have to play by the GPU rules. Simply throwing polys at the GPU and expect the best rendering performance doesn't really work. In this two part series of blog posts I'll try to explain what basically sucks in the current way we render things and what we've done in the last couple of months to achieve better rendering performance.</p>
<p>There is of course always room for more improvement. One thing I'd like to tackle in the future and especially in Nifty 2.0 would be to render only the parts of a scene that have changed. Since the best performance you can ever get is not to render :) But since this is a bit more involved for now we're stuck with the "render the whole GUI each frame" approach of current generation Nifty. BUT at least we can make Nifty render fast. Very fast.<br />
<a id="more"></a><a id="more-505"></a><br />
So here we go. A trip down the current way how Nifty renders its GUI and how we can try to be a bit smart to optimize it a lot. In this first part we'll look at the way the current renderer works and how bad some of the decisions have been according to rendering performance. In the second part of this two part series we'll look at the way we can make everything better and speed it up.</p>
<p>When it comes to rendering Nifty only knows three somewhat high level primitives:</p>
<ul>
<li>render a quad in a single color or with different colors at each vertex (for gradient support)<&#47;li>
<li>render a textured image<&#47;li>
<li>render text with a given font<&#47;li><br />
<&#47;ul></p>
<p>So all of the elements on your Nifty screen and all effects you apply to them will end up as a number of colored quads, textured images or text renderings.</p>
<p>To actually perform all of this Nifty provides some <a href="http:&#47;&#47;en.wikipedia.org&#47;wiki&#47;Service_provider_interface">SPI<&#47;a> in the <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;tree&#47;1.3&#47;nifty-core&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;spi&#47;render">de.lessvoid.nifty.spi.render<&#47;a> package. If you implement the four simple interfaces for the rendering system of your choice you're done and Nifty can be used with your rendering system. Nifty provides native LWJGL, JOGL, jME3, Slick2D and even Java2D adapter implementations already.</p>
<p>So let's take a look at the main interface of the SPI the <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;blob&#47;1.3&#47;nifty-core&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;spi&#47;render&#47;RenderDevice.java">de.lessvoid.nifty.spi.render.RenderDevice<&#47;a>. There are methods to load images and fonts and methods to let Nifty request the size of the screen. Other methods let the implementation know when a render frame begins and when it ends. However, the core of the RenderDevice interface are a couple of methods to render colored quads, images and text.</p>
<p>The render*() methods contain almost all the state that is required to perform the render directly as method parameters. Things like where to render the quad on the screen and which width, height and color to use are given as parameters.</p>
<p>Besides those parameters there are two additional states that can be modified by Nifty in calling RenderDevice methods which are:</p>
<ul>
<li>the current blendmode - that defines how blending should be configured prior to rendering elements<&#47;li>
<li>enable or disable clipping - to restrict rendering to a certain rectangle on the screen. Everything outside this clipping rectangle will not be rendered.<&#47;li><br />
<&#47;ul></p>
<p>So Nifty calls beginFrame() and then repeats for everything it needs to render: set the state (clipping and blending) and then calls renderQuad(), renderImage() or renderFont() and finally it calls endFrame(). In each render*() call the implementation will now ensure that the correct textures are set or that texturing is disabled, to render plain colored quads. In case of font rendering the correct bitmap font texture needs to be selected so that the text can be rendered properly and so on.</p>
<p>And here is the main issue in the naive implementations that have been used so far especially in the LWJGL renderer. Changing state costs performance since each state switch results in quite a lot of processing on it's way through all the different layers involved on the way to the GPU. There are driver calls, OS calls, state checks, command queues and so on to finally set the GPU in the state we need to render our triangles. If you're interessted in all of the details there is a great series of blogs available by Fabian Giesen called <a href="http:&#47;&#47;fgiesen.wordpress.com&#47;2011&#47;07&#47;09&#47;a-trip-through-the-graphics-pipeline-2011-index&#47;">A trip through the Graphics Pipeline 2011<&#47;a>.</p>
<p>So the first issue the current way Nifty renders stuff is that we change state quite a lot each frame. If we need to render a single colored untextured quad we'll need to disable texturing. If we need to render a certain image next, we'll need to enable texturing again and make sure the texture of the image we need to render is enabled. The same happens to clipping and blending which need to be enabled or disabled as well. So we're constantly changing state which, well, hurts performance.</p>
<p>A second issue that is especially apparent in the native LWJGL renderer is the way the actual vertex data is submitted to the GPU. When submitting data to OpenGL the classic (and very old way) to submit vertex data has been used: the immediate mode. Which means each vertex is send with multiple OpenGL calls. Here is an example:</p>
<pre class="brush:java">&#47;&#47; code to render a single quad with vertex color - DON'T DO THAT!<br />
  GL11.glBegin(GL11.GL_QUADS);<br />
      GL11.glColor4f(topLeft.getRed(), topLeft.getGreen(), topLeft.getBlue(), topLeft.getAlpha());<br />
      GL11.glVertex2i(x, y);<br />
      GL11.glColor4f(topRight.getRed(), topRight.getGreen(), topRight.getBlue(), topRight.getAlpha());<br />
      GL11.glVertex2i(x + width, y);<br />
      GL11.glColor4f(bottomRight.getRed(), bottomRight.getGreen(), bottomRight.getBlue(), bottomRight.getAlpha());<br />
      GL11.glVertex2i(x + width, y + height);<br />
      GL11.glColor4f(bottomLeft.getRed(), bottomLeft.getGreen(), bottomLeft.getBlue(), bottomLeft.getAlpha());<br />
      GL11.glVertex2i(x, y + height);<br />
    GL11.glEnd();<&#47;pre></p>
<p>That's really bad. First there are lots of calls to the GL and each of the calls will need to get through the different layers and checks again. This will only be ok if you send very view vertices but as the vertex count increases the overhead of the individual method calls will add up and will hurt performance as well.</p>
<p>So these are the main issues we'll need to solve to improve rendering performance:</p>
<ul>
<li>reduce state switches<&#47;li>
<li>reduce draw calls to send vertex data<&#47;li><br />
<&#47;ul></p>
<p>The second part of this mini blog series will explain how we solve those two issues by providing a special RenderDevice implementation. Interesting enough this special RenderDevice solves additional issues and makes implementing a new renderer for Nifty more easy as well :)</p>
<p>Instead of hundreds of glVertex() calls we can render the whole GUI in very few draw calls and most of the time even only in a single one! And all of this with no changes to the rest of Nifty or your code. In most case you'll be able to use the new special RenderDevice for a performance boost and that's it. nifty! :)</p>
<p>Curious? See you on the next blog post!</p>
<p>void :D</p>
