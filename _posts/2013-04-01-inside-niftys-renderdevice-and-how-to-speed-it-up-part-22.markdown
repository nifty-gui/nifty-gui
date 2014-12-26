---
layout: post
status: publish
published: true
title: Inside Niftys RenderDevice and how to speed it up (Part 2&#47;2)
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "Welcome back to the second part of this two part mini series. Today we'll
  speed up the Nifty rendering process. So fasten your seat belts - it will be a long
  and rough ride! :)\r\n\r\nIn the <a href=\"http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;505\">first
  part<&#47;a> we've identified several problems that we'd like to fix today:\r\n\r\n<ul>\r\n\t<li>primitive&#47;polygon
  submission is not optimal and requires way too many GL calls<&#47;li>\r\n\t<li>we
  switch GL state very often, especially enabling&#47;disabling texturing while rendering
  and switching the current texture costs performance<&#47;li>\r\n\t<li>we enable&#47;disable
  and change the clipping rectangle very often (eventually)<&#47;li>\r\n\t<li>we change
  other states like the blend mode. Not a lot but it is still happening if you ask
  for it within your screen (f.i. using some effect)<&#47;li>\r\n<&#47;ul>\r\n\r\nSo
  let's tackle these issues one at a time.\r\n"
wordpress_id: 530
wordpress_url: http://nifty-gui.lessvoid.com/?p=530
date: '2013-04-01 10:17:19 +0200'
date_gmt: '2013-04-01 09:17:19 +0200'
categories:
- design
tags: []
comments:
- id: 1815
  author: Enrico
  author_email: enrico.b@gmx.de
  author_url: ''
  date: '2013-05-23 15:29:35 +0200'
  date_gmt: '2013-05-23 14:29:35 +0200'
  content: "Hi, thanks for this post.\r\n\r\nIf I want to use the new functionality
    within a JME3 Application, do I have to do something with the BatchRenderDevice?
    (I mean where can I set that it should be used instead of the \"usual Render Device\")\r\n\r\nmy
    code for initializing nifty looks like this:\r\n\r\nNiftyJmeDisplay niftyDisplay
    = new NiftyJmeDisplay(assetManager, app.getInputManager(), app.getAudioRenderer(),
    app.getGuiViewPort());\r\n\r\nNifty nifty = niftyDisplay.getNifty();\r\nnifty.fromXml(...);\r\n\r\nBatchRenderDevice
    renderDevice = new BatchRenderDevice(\r\n            new JmeBatchRenderBackend(niftyDisplay),\r\n
    \           2048,\r\n            2048);\r\n\r\napp.getGuiViewPort().addProcessor(niftyDisplay);\r\n\r\nI
    am feeling I missed something to do with the renderDevice. :)\r\n\r\nBTW:\r\nCompiles
    fine with actual JME3 \"beta (nightly) updates\" activated and updated within
    the JME3 SDK.\r\n\r\nBut at runtime:\r\nUncaught exception thrown in Thread[LWJGL
    Renderer Thread,5,main]\r\njava.lang.NoClassDefFoundError: org&#47;jglfont&#47;spi&#47;BitmapFontRenderer\r\n\r\nSeems
    like I have to reference some additional lib?\r\n\r\nThanks for an advise and
    keep up the good work!\r\n\r\nEnrico"
- id: 1816
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-05-23 22:58:54 +0200'
  date_gmt: '2013-05-23 21:58:54 +0200'
  content: Actually that looks good! jglfont-core.jar is required on your classpath.
    I've just checked with the latest nightly build and this jar is part of the zip
    you can download. Can you check if that specific jar is part of your runtime classpath?
- id: 1823
  author: Enrico
  author_email: enrico.b@gmx.de
  author_url: ''
  date: '2013-05-24 13:01:43 +0200'
  date_gmt: '2013-05-24 12:01:43 +0200'
  content: "OK,runs now.\r\nAs it seems, the JME3 built in \"Beta Update\" did not
    download the jglfont-core.jar - so I did manually and everything runs fine now.\r\n\r\nI
    am not gaining any Performance. But it was worth a try. :)\r\n\r\nAt the beginning
    of my project I realized in first tests that nifty only (no other AbstractAppstate
    implementation with 3d models added) runs very smooth, a test with only a few
    3dModels ran very smooth too.\r\nBut Nifty + 3d Models AppStates together = big
    Performance drop.\r\n\r\nThanks again,\r\nEnrico."
- id: 1825
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-05-24 22:00:27 +0200'
  date_gmt: '2013-05-24 21:00:27 +0200'
  content: Uhm, that's odd! Any chance that I can take a look? The batched renderer
    really should be faster! Now I'm curious ... ;)
---
<p>Welcome back to the second part of this two part mini series. Today we'll speed up the Nifty rendering process. So fasten your seat belts - it will be a long and rough ride! :)</p>
<p>In the <a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;505">first part<&#47;a> we've identified several problems that we'd like to fix today:</p>
<ul>
<li>primitive&#47;polygon submission is not optimal and requires way too many GL calls<&#47;li>
<li>we switch GL state very often, especially enabling&#47;disabling texturing while rendering and switching the current texture costs performance<&#47;li>
<li>we enable&#47;disable and change the clipping rectangle very often (eventually)<&#47;li>
<li>we change other states like the blend mode. Not a lot but it is still happening if you ask for it within your screen (f.i. using some effect)<&#47;li><br />
<&#47;ul></p>
<p>So let's tackle these issues one at a time.<br />
<a id="more"></a><a id="more-530"></a><br />
I won't go into the specific OpenGL methods too much and will concentrate more on the "Nifty" side of things. You can read up the OpenGL calls <a href="http:&#47;&#47;www.opengl.org&#47;sdk&#47;docs&#47;man3&#47;">elsewhere<&#47;a>. But the interesting part - at least for me and I hope for you as well - is to connect all the dots and combine the individual pieces to optimize Nifty.</p>
<p>So without further ado let's rock.</p>
<p><em><strong>Optimize polygon submission<&#47;strong><&#47;em></p>
<p>Well, that one is a no brainer since vertex arrays have been a part of the OpenGL spec since the very early OpenGL 1.1 days (1995 or so). The idea is to put all of your vertex data into an array and then give OpenGL a pointer to that array and tell it in a single call: now go and render all of them. Since I'd like to keep the current LWJGL renderer compatible with legacy OpenGL (for now, be patient ^^) I've used plain old client-side vertex arrays. This means that all of the vertex data is stored on the CPU and is only send to the GPU for rendering. This allows us to take advantage of the GL_QUADS rendering mode (which has been optimized away from core profile unfortunatly).</p>
<p>So for each Nifty <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;blob&#47;1.3&#47;nifty-core&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;spi&#47;render&#47;RenderDevice.java">RenderDevice<&#47;a> render*() method (renderQuad() or renderImage()) we add four vertices to the vertex buffer representing a single quad. When Nifty later calls the endFrame() method we render all of the quads with a single glDrawElements(GL_QUADS) call. This way we can remove a couple of hundred individual glBegin()&#47;glEnd()&#47;glVertex() calls and GL can now take our buffer, process it in one step and render it! Much faster!</p>
<p>Great! We're done! Woohoo!</p>
<p>But wait, what do you say? We're not finished yet? What's with all of the texturing and what is with font rendering? And what if we want to render a single colored untextured quad like a Nifty panel with a plain backgroundColor? Wouldn't we have to disable texturing somehow in between all those quads in the vertex array?</p>
<p>Of course you're right - readers are always right ;) So let's continue.</p>
<p><strong>Disable texturing while rendering with a vertex array<&#47;strong></p>
<p>If we would enable&#47;disable texturing all the time or switch the current texture while we're rendering our quads we would end up with individual render calls again. In the end all of our performance gains would have been lost again. So what should we do?</p>
<p>Our first trick is to let texturing enabled all the time. We'll simply submit textured quads only - all the time. To render a plain colored quad we'll reserve some pixels of a plain solid color in our texture, let's say white. If we then render a quad with texture coordinates of this white piece of the texture we would end up with a solid white quad - and this quad could really be any size we want since it doesn't really matter if we would stretch a single white pixel to the size of the screen. It would still be white :)</p>
<p>So that's great. We can render white colored quads. Yeah! But what about other colors? Ok, that's easy as well. We attach vertex colors to our vertices. To render a plain colored red quad for instance, we'll set the vertex color of each vertex to red and in the end we'd have a red quad instead of a white one. And btw. this allows us to add support for linear gradients as well when we use different colors.</p>
<p>So to summarize our vertex data, here are the components we store per vertex in our array:</p>
<ul>
<li>The position of the vertex. At the moment we're only 2d so two floats should be appropriate (x and y coordinates)<&#47;li>
<li>The texture coordinate for the vertex (u and v, 2 floats as well)<&#47;li>
<li>A color for the vertex. That will add 4 more floats.<&#47;li><br />
<&#47;ul></p>
<p>So there is no need to disable texturing at all! If we want to render a plain colored quad we'll simply adjust the texture coordinates of that quad in our vertex array to match up with the plain colored area in our texture.</p>
<p><strong>Switch the current texture while rendering with a vertex array<&#47;strong></p>
<p>Finally we'll need a way to render different textures. The solution to this is simple in theory but was a bit more involved in the end.</p>
<p>The idea is to combine all of our individual images into one big texture. The name for this optimization technique is "texture atlas" or "texture packing". In most cases you would combine your textures into a bigger one as a pre-process using some custom tool. But for Nifty we'll need to do that dynamically. Let's enter the world of texture packing algorithms also known as bin packing.</p>
<p>Well, as it appears this topic is a huge one! There are even PhD Thesis discussing this in depth (The PhD Thesis of Andrea Lodi f.i.: <a href="http:&#47;&#47;citeseerx.ist.psu.edu&#47;viewdoc&#47;download?doi=10.1.1.98.3502&rep=rep1&type=pdf">Algorithms for Two Dimensional Bin Packing and Assignment Problems<&#47;a>).</p>
<p>One of the more simple algorithms and the one we'll use for now is the <a href="http:&#47;&#47;www.blackpawn.com&#47;texts&#47;lightmaps&#47;default.html">Lightmap Packing Algorithm<&#47;a> by <a href="http:&#47;&#47;www.blackpawn.com&#47;">Black Pawn<&#47;a> and the <a href="https:&#47;&#47;github.com&#47;lukaszdk&#47;texture-atlas-generator">Java port<&#47;a> of this algorithm done by <a href="https:&#47;&#47;github.com&#47;lukaszdk&#47;texture-atlas-generator">lukaszdk<&#47;a>.</p>
<p>The Java version of the <a href="https:&#47;&#47;github.com&#47;lukaszdk&#47;texture-atlas-generator">Texture Atlas Generator<&#47;a> is meant to be used as an executable jar that combines multiple textures into a bigger one using java.awt.image.* stuff. Not bad but for Nifty we'd like to do that on the fly and we'd like to separate the actual algorithm from the actual handling of the graphics. In the end the algorithm can work independently of the actual graphics data. So we've modified his code a bit. If you're interested in the details you can find our version in the <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;blob&#47;1.3&#47;nifty-core&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;batch&#47;TextureAtlasGenerator.java">TextureAtlasGenerator<&#47;a> class. This class can be used on its own since it is self contained and doesn't need any Nifty dependencies at all - if you ever need a similar algorithm ;)</p>
<p>So we'll start with a big texture (something like 2048x2048 pixels works very well so far) and as Nifty loads images we'll put them into this texture at positions the TextureAtlasGenerator calculates for us. And with that in place switching textures actually only means changing the texture coordinates according to the data that the TextureAtlasGenerator has generated. And voila we can keep rendering all of our quads in a single call using the same big texture! :D</p>
<p><strong>Managing image resources<&#47;strong></p>
<p>The texture atlas will be filled with images for the current Nifty screen. This way all images used will be part of the texture atlas. To do that Nifty keeps track of which image belongs to which screen when it reads a XML file or when a screen is created with the Builder pattern. This tracking is done for all loaded screens and even for images that are dynamically created with Nifty.createImage().</p>
<p>In general Nifty will make sure that it uploads all images that are required for a screen when the screen is started (and gets active). Consequently when a screen ends the texture atlas is being reset to an empty one.</p>
<p>Nifty already implemented a reference counting mechanism to keep track of loaded images. This was used to prevent loading the same image multiple times and it is still being used for this purpose. However, the process of loading an image is now separated into two steps:</p>
<p>1. the actual image data is loaded into whatever image representation an implementation supports (for instance the implementation using native LWJGL will simply load an image file into a ByteBuffer) and</p>
<p>2. the loaded image data is put into the texture atlas at a position that Nifty decided when it's time for this image to be a part of the current screens texture atlas.</p>
<p>When an image is accessed dynamically while a screen is running it will be uploaded at the time of the first access. For best performance static images - or at least ones that Nifty knows about before the screen is started - should be preferred so that Nifty can upload them to the texture atlas at the time the screen starts and not while the screen is already running. Although I think you can get away with a couple of image uploads if you don't access too many new images at once.</p>
<p>All of this is required to keep only the currently active images in the texture atlas. This should work quite well for most use cases. However, it's still possible to use up all of the available space in the texture atlas. In that case Nifty will complain in the log but there will be missing images. All of the Nifty examples run well with a 2K (2048x2048) texture so far. The batch renderer provides a better rendering performance but you might want to plan for some additional tests to check if all of your images fit into the texture.</p>
<p><strong>Clipping<&#47;strong></p>
<p>Nifty allows you to clip child elements to the area of it's parent element. This way only the part that intersects with the parent element will be displayed. The original Nifty renderer used glScissor() for this prior to rendering the child elements. The problem with this approach is that we can't change the size of the scissor box while rendering with vertex arrays. So what should we do?</p>
<p>Well, clipping is a somewhat simple 2d operation so we simply clip on the CPU now! The quads we will send into the vertex array will already be clipped so we don't have to change the scissor state while we're rendering :)</p>
<p><strong>Blending<&#47;strong></p>
<p>Nifty renders everything with usual alpha transparency blending (glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)) but it can be changed to Multiply (glBlendFunc(GL_DST_COLOR, GL_ZERO)) if necessary for rendering special effects. However, for the optimized renderer this is a slight problem since we can't change the blending mode while rendering a vertex array. What Nifty will do now is to create a new batch when the blending mode changes. Since the Multiply blending mode is currently the only "other" blend mode supported and this is rarely used we'll get away with the new batch approach. Rendering a couple of batches per frame is still a lot better than rendering hundreds of individual quads with hundreds of draw calls.</p>
<p>So with blending out of the way we've actually solved all of the problems we set out to fix! :D</p>
<p>The only thing we have to discuss is how all of this has been implemented.</p>
<p>Usually this is done by each rendering system individually like a LWJGL, JOGL or JME3 batched renderer implementation. However each of these individual implementations would solve the exact same problems outlined above. All of the batching, texture atlas management and so on. Not a very nifty solution.</p>
<p>A better approach would be to solve the managment of the batches once and then use a simplified implementation for the native implementations. And that's exactly what we did :D</p>
<p><strong>Unified batched RenderDevice implementation!<&#47;strong></p>
<p>Nifty 1.3.3 will provide a default implementation of the RenderDevice interface. The <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;blob&#47;1.3&#47;nifty-core&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;batch&#47;BatchRenderDevice.java">de.lessvoid.nifty.batch.BatchRenderDevice<&#47;a> handles all of things we've mentioned so far including all of the texture packing logic and so on.</p>
<p>Of course there is still the need for specific implementations to connect all of this to LWJGL, JOGL or jME. But these implementations are now much simpler since they don't have to handle all of the details the original RenderDevice has to. The <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;blob&#47;1.3&#47;nifty-core&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;batch&#47;spi&#47;BatchRenderBackend.java">de.lessvoid.nifty.batch.spi.BatchRenderBackend<&#47;a> SPI is much simpler. The BatchRenderBackend will receive quads that it should buffer in whatever way it sees fit. The LWJGL implementation fills a vertex array exactly as we've described above. Handling the texture atlas is reduced to creating a texture and replacing subtextures.</p>
<p>Rendering fonts is also handled inside the BatchRenderDevice which means the BatchRenderBackend doesn't have to implement that. Font rendering is reduced to rendering quads as well and the font texture is simply treated as another part of the texture atlas. This has the additional benefit that font rendering looks the same no matter what rendering system you use because things like kerning, text string width calculations and so on are all happening inside of Nifty now :)</p>
<p>But wait, there is even more!</p>
<p><strong>Bonus: LWJGL OpenGL Core Profile implementation<&#47;strong></p>
<p>Until this point you could not really use Nifty with modern OpenGL because the original LWJGL implementation didn't support it. With the new BatchRenderDevice a full OpenGL Core Profile implementation is now available! :D</p>
<p><strong>Shut up and <del datetime="2013-04-01T08:09:45+00:00">take my money<&#47;del> show me how to use it<&#47;strong></p>
<p>Usage of the batched renderer is very easy. In place of your usual RenderDevice implementation you use the BatchRendererDevice when you instantiante Nifty:</p>
<pre class="brush:java">BatchRenderDevice renderDevice = new BatchRenderDevice(<br />
    put-in-batched-renderer-backend-here, &#47;&#47; BatchRenderBackend impl<br />
    2048, &#47;&#47; width of texture atlas<br />
    2048); &#47;&#47; height of texture atlas<&#47;pre></p>
<p>The first parameter is an implementation of the <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;blob&#47;1.3&#47;nifty-core&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;batch&#47;spi&#47;BatchRenderBackend.java">de.lessvoid.nifty.batch.spi.BatchRenderBackend<&#47;a> interface that connects the batched renderer to a specific OpenGL implementation. Currently the following implementations exist:</p>
<ul>
<li>LWJGL legacy batched renderer: <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;blob&#47;1.3&#47;nifty-renderer-lwjgl&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;renderer&#47;lwjgl&#47;render&#47;batch&#47;LwjglBatchRenderBackend.java">de.lessvoid.nifty.renderer.lwjgl.render.batch.LwjglBatchRenderBackend<&#47;a><&#47;li>
<li>LWJGL core profile batched renderer: <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;blob&#47;1.3&#47;nifty-renderer-lwjgl&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;renderer&#47;lwjgl&#47;render&#47;batch&#47;LwjglBatchRenderBackendCoreProfile.java">de.lessvoid.nifty.renderer.lwjgl.render.batch.LwjglBatchRenderBackendCoreProfile<&#47;a><&#47;li>
<li>jme3 batched renderer implementation: <a href="http:&#47;&#47;code.google.com&#47;p&#47;jmonkeyengine&#47;source&#47;browse&#47;trunk&#47;engine&#47;src&#47;niftygui&#47;com&#47;jme3&#47;niftygui&#47;JmeBatchRenderBackend.java">com.jme3.niftygui.JmeBatchRenderBackend<&#47;a><&#47;li><br />
<&#47;ul></p>
<p>You'll need to use Nifty 1.3.3 or Nifty 1.4 nightly builds to have these new implementations available. For jme3 you'll need a nightly build as well.</p>
<p>Besides the BatchRendererBackend implementation you'll need to provide the size of the texture atlas as parameters. As mentioned above a 2k texture was enough to run all of Niftys standard examples.</p>
<p>So, that's all there is!</p>
<p>If you made it through this huge post. Congratulations! You now know all the things that took me a couple of months to figure out :)</p>
<p>I hope you've enjoyed this in-depth explanation a bit!</p>
<p>See you next time!</p>
<p>void</p>
