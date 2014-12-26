---
layout: post
status: publish
published: true
title: Nifty plays well with others and of course with slick2d too
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "Nifty can be used as a standalone GUI. So when your game enters its menu
  or option screens Nifty is all there is on the screen and all user input is handled
  by Nifty.\r\n\r\nWhen using Nifty together with Slick2d this is represented by the
  NiftyGameState class. Using this class your Slick2d StateBasedGame can easliy switch
  from your in game state to the NiftyGameState and display the GUI.\r\n\r\n<strong>But<&#47;strong>
  this is not the only way Nifty can be used. You can easily use Nifty to render your
  in-game GUI too! Nifty plays well with others (as long as they use OpenGL&#47;lwjgl
  for rendering that is).\r\n\r\nIt's not complicated at all and I wanted to write
  example code that demonstrates how easy it is for a long time. Motivated by a question
  at the <a title=\"Nifty Forum at sourceforge.net\" href=\"http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;forums&#47;forum&#47;807893\">Nifty
  Forum at souceforge.net<&#47;a> I did now :)\r\n\r\nWhat's demonstrated in the new
  slick example is, how you can use Nifty to render a GUI on top of a normal slick
  GameState.\r\n\r\nHere is a screenshot of the example which renderes Text from within
  Slick that changes color when you press the keys 1-3. On top of this it renders
  a Nifty GUI that responds to mouse events. Additionally if you press 1-3 the colored
  Nifty boxes start to shake :D\r\n\r\n[caption id=\"attachment_92\" align=\"aligncenter\"
  width=\"300\" caption=\"Slick Overlay with Nifty GUI\"]<a href=\"http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;09&#47;bildschirmfoto-2009-09-13-um-145515.png\"><img
  class=\"size-medium wp-image-92\" title=\"Slick Overlay with Nifty GUI\" src=\"http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;09&#47;bildschirmfoto-2009-09-13-um-145515-300x225.png\"
  alt=\"Slick Overlay with Nifty GUI\" width=\"300\" height=\"225\" &#47;><&#47;a>[&#47;caption]\r\n\r\nYou
  can try it out with the Webstart:\r\n\r\n<a href=\"http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-slick-overlay-demo.jnlp\">http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-slick-overlay-demo.jnlp<&#47;a>\r\n\r\nand
  you can find the example in svn or you can browse it online here:\r\n\r\n<a href=\"http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;examples&#47;slick&#47;niftyoverlay&#47;\">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;examples&#47;slick&#47;niftyoverlay&#47;<&#47;a>
  (Java classes)\r\n\r\n<a href=\"http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;resources&#47;slick&#47;niftyoverlay&#47;overlay.xml?revision=534&amp;view=markup\">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;resources&#47;slick&#47;niftyoverlay&#47;overlay.xml?revision=534&amp;view=markup<&#47;a>
  (Nifty XML for the Screen - overlay.xml)\r\n\r\nKeep reading the full Article to
  see the details on how we get this to work and what you need to consider.\r\n\r\n"
wordpress_id: 91
wordpress_url: http://nifty-gui.lessvoid.com/?p=91
date: '2009-09-13 14:20:29 +0200'
date_gmt: '2009-09-13 13:20:29 +0200'
categories:
- design
- docs
tags:
- Slick
- slick2d
- integration
- compatibily
- overlay
- gui
comments:
- id: 338
  author: Murilo
  author_email: murilovmachado@gmail.com
  author_url: ''
  date: '2009-09-14 18:26:19 +0200'
  date_gmt: '2009-09-14 17:26:19 +0200'
  content: Thank you very much void, this is great!!
- id: 353
  author: orthogonal projection - StartTags.com
  author_email: ''
  author_url: http://starttags.com/tags/orthogonal-projection
  date: '2010-03-04 05:34:04 +0100'
  date_gmt: '2010-03-04 04:34:04 +0100'
  content: '[...] projection we also need to apply translations. ... Mail (will not
    be published) (required) ...nifty-gui Blog Archive Nifty plays well with others
    and of ...Which means that everything is setup for 2d OpenGL rendering (orthogonal
    projection, modelview [...]'
- id: 1098
  author: cere
  author_email: cereblanco@gmail.com
  author_url: ''
  date: '2011-12-08 17:41:57 +0100'
  date_gmt: '2011-12-08 16:41:57 +0100'
  content: "Is the svn link broken? \r\nI cant browse the source. \r\n\r\nPlease fix.
    Please. :) Thanks"
- id: 1099
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2011-12-09 00:46:19 +0100'
  date_gmt: '2011-12-08 23:46:19 +0100'
  content: svn? svn is soooo 2010 ;D we've switched to git now! see details <a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;321"
    rel="nofollow">here<&#47;a>
- id: 1100
  author: cere
  author_email: cereblanco@gmail.com
  author_url: ''
  date: '2011-12-09 02:37:28 +0100'
  date_gmt: '2011-12-09 01:37:28 +0100'
  content: thanks! :)
---
<p>Nifty can be used as a standalone GUI. So when your game enters its menu or option screens Nifty is all there is on the screen and all user input is handled by Nifty.</p>
<p>When using Nifty together with Slick2d this is represented by the NiftyGameState class. Using this class your Slick2d StateBasedGame can easliy switch from your in game state to the NiftyGameState and display the GUI.</p>
<p><strong>But<&#47;strong> this is not the only way Nifty can be used. You can easily use Nifty to render your in-game GUI too! Nifty plays well with others (as long as they use OpenGL&#47;lwjgl for rendering that is).</p>
<p>It's not complicated at all and I wanted to write example code that demonstrates how easy it is for a long time. Motivated by a question at the <a title="Nifty Forum at sourceforge.net" href="http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;forums&#47;forum&#47;807893">Nifty Forum at souceforge.net<&#47;a> I did now :)</p>
<p>What's demonstrated in the new slick example is, how you can use Nifty to render a GUI on top of a normal slick GameState.</p>
<p>Here is a screenshot of the example which renderes Text from within Slick that changes color when you press the keys 1-3. On top of this it renders a Nifty GUI that responds to mouse events. Additionally if you press 1-3 the colored Nifty boxes start to shake :D</p>
<p>[caption id="attachment_92" align="aligncenter" width="300" caption="Slick Overlay with Nifty GUI"]<a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;09&#47;bildschirmfoto-2009-09-13-um-145515.png"><img class="size-medium wp-image-92" title="Slick Overlay with Nifty GUI" src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2009&#47;09&#47;bildschirmfoto-2009-09-13-um-145515-300x225.png" alt="Slick Overlay with Nifty GUI" width="300" height="225" &#47;><&#47;a>[&#47;caption]</p>
<p>You can try it out with the Webstart:</p>
<p><a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-slick-overlay-demo.jnlp">http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-slick-overlay-demo.jnlp<&#47;a></p>
<p>and you can find the example in svn or you can browse it online here:</p>
<p><a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;examples&#47;slick&#47;niftyoverlay&#47;">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;examples&#47;slick&#47;niftyoverlay&#47;<&#47;a> (Java classes)</p>
<p><a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;resources&#47;slick&#47;niftyoverlay&#47;overlay.xml?revision=534&amp;view=markup">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-examples&#47;trunk&#47;src&#47;main&#47;resources&#47;slick&#47;niftyoverlay&#47;overlay.xml?revision=534&amp;view=markup<&#47;a> (Nifty XML for the Screen - overlay.xml)</p>
<p>Keep reading the full Article to see the details on how we get this to work and what you need to consider.</p>
<p><a id="more"></a><a id="more-91"></a></p>
<p>The way it works is pretty easy and it even works with anything you've rendered in lwjgl. Slick is not required but as shown it works very well with Slick too.</p>
<p>So you just need to:</p>
<ol>
<li> <strong>Save your current OpenGL-State and enable 2d Mode<&#47;strong><br />
You have to make sure you save your current OpenGL state and make sure you are in 2d "mode" before calling Nifty. Which means that everything is setup for 2d OpenGL rendering (orthogonal projection, modelview matrix aligned with the screen, etc. In Slick this is all handled by a single call to SlickCallable.enterSafeBlock(). This makes sure that the current Slick state is saved and changes in Slick or Nifty don't disturb each other (we're already in 2d mode too because of slick).<&#47;li></p>
<li><strong>Call Nifty to render its GUI<&#47;strong><br />
You call Nifty.render() to render the Nifty GUI. You can call render() with a boolean parameter that tells Nifty to clear the screen (parameter = true) or if it should not clear the screen at all (parameter = false). If you are rendering an overlay you probably want to leave the screen as is and render your GUI on top. So in this case simply call nifty.render(false).<&#47;li></p>
<li><strong>Restore your State and continue your own Rendering<&#47;strong><br />
When nifty.render() returns you can reset your state so that you can continue rendering 3d triangles or whatever you want to render on top (!) of Nifty GUI. Again in Slick this is a single SlickCallable.leaveSafeBlock() call.<&#47;li></p>
<li><strong>Forward KeyEvents<&#47;strong><br />
You decide if keyEvents get past through to Nifty by calling nifty.keyEvent() with the events you want that Nifty gets. If you don't call keyEvent() no key events will get to Nifty what might or might not be what you want.<&#47;li></p>
<li><strong>Forward MouseEvents<&#47;strong><br />
Handling mouse events are a bit trickier because they are handled through the InputSystem interface which you need to call the Nifty constructor with. But creating an implementation of the InputSystem is easy because at the moment it only consists of one method that Nifty uses to request a List of current available MouseEvents. So you're implementation can decide when and what mouse events get through to Nifty in the InputSystem-Implementation.<&#47;li><br />
<&#47;ol><br />
The example in nifty-examples svn demonstrates how to forward MouseEvents but does not care about keyboard Events.</p>
<p>And that's it! Nifty GUI playing together with others! Nifty! :D</p>
<p>void</p>
