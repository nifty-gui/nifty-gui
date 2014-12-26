---
layout: post
status: publish
published: true
title: Nifty 1.2 released
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 157
wordpress_url: http://nifty-gui.lessvoid.com/?p=157
date: '2010-08-02 22:08:21 +0200'
date_gmt: '2010-08-02 21:08:21 +0200'
categories:
- demo
- release
tags:
- nifty
- release
- nifty 1.2
- nifty renderer
comments:
- id: 452
  author: Erlend Sogge Heggen
  author_email: e.soghe@gmail.com
  author_url: http://jmonkeyengine.com
  date: '2010-08-03 14:35:00 +0200'
  date_gmt: '2010-08-03 13:35:00 +0200'
  content: "Awesome work mate! <strong>Loved<&#47;strong> the tutorial. Very nice
    choice of music too :) Do note though, when running a jnlp file on my Ubuntu 10.04
    Inspiron 1720 laptop, I can't run it in full screen nor can I change the height
    of the window, and so some visuals are lost at the bottom of the screen. For the
    page controls in the tutorial this worked out okay since they were still clearly
    visible but cut off a little, while in the examples, I ran into this:\r\n\r\nhttp:&#47;&#47;imgur.com&#47;BoxeC.png\r\n\r\nAlso,
    why doesn't scrolling (through the examples) work?\r\n\r\nWe'll be in touch a"
- id: 453
  author: Erlend Sogge Heggen
  author_email: e.soghe@gmail.com
  author_url: http://jmonkeyengine.com
  date: '2010-08-03 14:37:06 +0200'
  date_gmt: '2010-08-03 13:37:06 +0200'
  content: "(Hrmph, anti-spam sort of screwed me over)\r\n\r\n... We'll be in touch
    about the jME integrations. I'll be sure to type up a blog post about it once
    it's all well and done :)\r\n\r\nBy the way, I'm sure you could easily create
    an awesome-looking showcase video for Nifty, eh? :D"
- id: 454
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com
  date: '2010-08-04 18:11:50 +0200'
  date_gmt: '2010-08-04 17:11:50 +0200'
  content: "Hi Erlend and thank you ^^\r\n\r\nI was not aware of the trouble with
    linux and odd ;P display resolutions. I am aware of the fixed resolution of 1024x768
    pixels in the nifty examples as well as the tutorial. This is not as much a problem
    of Nifty as it is a matter of my laziness writting the examples&#47;tutorial :>
    Supporting multiple resolutions in a way to make all resolutions look well is
    not an easy task - even when using Nifty. As Nifty assumes an already initialized
    display mode this is more like a problem with the startup code in the examples.
    I'll think of some things on how to fix this in later versions of the example&#47;tutorial
    project.\r\n\r\nThanks for pointing this out tho =)"
- id: 456
  author: Erlend Sogge Heggen
  author_email: e.soghe@gmail.com
  author_url: http://jmonkeyengine.com
  date: '2010-08-04 22:39:01 +0200'
  date_gmt: '2010-08-04 21:39:01 +0200'
  content: "As a quick fix you could just make sure you don't have any clickable elements
    that are displayed far down on the screen? ;)\r\n\r\nGood to see you finally updated
    the looks of your site man. Kudos on picking a great-looking theme =P"
- id: 458
  author: Dennis
  author_email: dennis@franke-s.de
  author_url: ''
  date: '2010-08-07 22:50:45 +0200'
  date_gmt: '2010-08-07 21:50:45 +0200'
  content: "Hi,\r\nDon't know if it's really a Bug or if it's supposed to be like
    this, but if i have a Panel with a Button in it and then hide the Panel, the Button
    is still clickable.\r\nSo if I hide the Panel and then just click where the Button
    used to be, it still calls the Java-Method."
- id: 459
  author: admin
  author_email: void@lessvoid.com
  author_url: http://
  date: '2010-08-08 00:29:45 +0200'
  date_gmt: '2010-08-07 23:29:45 +0200'
  content: |-
    Hey there! I can't reproduce this issue with the current Nifty Version. I've just created a panel with a button control and the onClick() of that button will hide the panel from java with element.hide(). After that the panel is hidden and I can't click on the button anymore. Which Version do you use? How does your xml and code look?

    PS: Maybe the best way to get help with issues like that is on the nifty help forum at sf.net: http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;forums&#47;forum&#47;807893 :)
- id: 465
  author: durandal
  author_email: durandal@mailcatch.com
  author_url: ''
  date: '2010-08-19 23:17:11 +0200'
  date_gmt: '2010-08-19 22:17:11 +0200'
  content: "Hey there. I was about to write my own widget set when I stumbled on your
    project.\r\n\r\nI can however not find any licence info. Does it have an open
    souce licence and if so which, or ?"
- id: 466
  author: admin
  author_email: void@lessvoid.com
  author_url: http://
  date: '2010-08-19 23:43:13 +0200'
  date_gmt: '2010-08-19 22:43:13 +0200'
  content: |-
    Nifty is using the BSD License which means you can do anything with it :)
    You can find more information about the project at its project page: https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47; which also states the license.
- id: 467
  author: durandal
  author_email: nifty@durandal.nl
  author_url: ''
  date: '2010-08-20 00:18:15 +0200'
  date_gmt: '2010-08-19 23:18:15 +0200'
  content: "Ah thank you and also for the fast reply. Must have missed that.\r\nI'm
    starting on an isometric game with Slick and just got the 'world map' running
    in a widget (resizable clipped rectangle) that, i think could run in a Nifty panel.
    Would that be a good possibility or would it be better to keep UI more seperated?\r\nAlso,
    Nifty has screens and Slick has GameStates that both serve as different modes
    in the game. Could you give an idea about the interaction between the two? Can
    i drop down to 1 gameState in Slick and use Nifty screens in stead for instance?\r\nThanks
    :)"
- id: 468
  author: durandal
  author_email: nifty@durandal.nl
  author_url: ''
  date: '2010-08-20 01:01:33 +0200'
  date_gmt: '2010-08-20 00:01:33 +0200'
  content: "Another question if I may - I am playing around with some lists and scroll
    bars and i notice that my mouse scrollwheel has no effect on them. Is this correct?
    \r\nIf so, will it be implemented &#47; is it easy to implement?\r\nThank you.
    Kudo's on what i've seen sofar. It's nifty indeed ;)"
- id: 469
  author: admin
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2010-08-20 03:30:54 +0200'
  date_gmt: '2010-08-20 02:30:54 +0200'
  content: 'You probably dont want to run your game in a nifty panel. Nifty absolutly
    can be used to render your ingame gui too. Usually you would render your game
    and then render your nifty gui above that, like a gui overlay. If I understand
    your question correctly, that is. There was a blog post about nifty slick overlays
    some weeks ago that might help too. PS: There is a Nifty help forum at the sf.net
    project page available too where you can ask more questions.'
- id: 470
  author: admin
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2010-08-20 03:33:57 +0200'
  date_gmt: '2010-08-20 02:33:57 +0200'
  content: yes, there is no mouse wheel support in nifty currently. I don't know how
    difficult it is :) but it will eventually be added.
- id: 487
  author: Ugly
  author_email: finn.@bastu.net
  author_url: ''
  date: '2010-09-11 18:09:21 +0200'
  date_gmt: '2010-09-11 17:09:21 +0200'
  content: "About this chatArea example. Which Jar contains this:\r\nimport de.lessvoid.nifty.controls.scrollpanel.ScrollPanel.AutoScroll;\r\n\r\nCan't
    get this tutorial running as supposed because Nifty libs I find lack this class.
    (?)"
- id: 513
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2010-09-29 16:32:48 +0200'
  date_gmt: '2010-09-29 15:32:48 +0200'
  content: The class "de.lessvoid.nifty.controls.scrollpanel.ScrollPanel.AutoScroll"
    is part of the nifty-default-controls.jar. The version 1.2 should be available
    for download from sf.net. And sorry for the late response.
- id: 544
  author: Julien
  author_email: gouessej@hotmail.com
  author_url: http://gouessej.wordpress.com
  date: '2010-10-20 13:31:44 +0200'
  date_gmt: '2010-10-20 12:31:44 +0200'
  content: I would like to get some support of JOGL...
- id: 545
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2010-10-20 17:30:54 +0200'
  date_gmt: '2010-10-20 16:30:54 +0200'
  content: either post on the help forum or on the feature tracker (which you probably
    already did :) I answered there ...) or you can contact me directly at void (
    a t ) lessvoid ( dot ) com.
- id: 653
  author: jMonkeyEngine.org &#124; Blog &#124; jMonkeyEngine Progress Brief 2nd &amp;
    3rd quarter
  author_email: ''
  author_url: http://jmonkeyengine.org/2010/08/24/jmonkeyengine-progress-brief-2nd-3rd-quarter/
  date: '2010-12-31 00:30:15 +0100'
  date_gmt: '2010-12-30 23:30:15 +0100'
  content: '[...] &middot; Nifty GUI 1.2 [...]'
- id: 672
  author: Mike
  author_email: mjkeeney@gmail.com
  author_url: ''
  date: '2011-01-06 22:15:04 +0100'
  date_gmt: '2011-01-06 21:15:04 +0100'
  content: Is source available for the demos?
- id: 673
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2011-01-06 23:28:35 +0100'
  date_gmt: '2011-01-06 22:28:35 +0100'
  content: 'of course! everything is open source! you can find all the sources at
    the sf.net project side: http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;'
---
<p><a href="https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;">Nifty 1.2 - Download it<&#47;a><br />
<a href="https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.2&#47;nifty-1.2-changelog.txt&#47;download">Nifty 1.2 - View Changelog<&#47;a><br />
<a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-examples-1.2.jnlp">Nifty 1.2 - Enjoy the Examples<&#47;a></p>
<p>And there is an updated tutorial&#47;demo available too:<br />
<a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-tutorial-1.2.jnlp">Nifty 1.2 - Updated Tutorial&#47;Demo<&#47;a></p>
<p>[caption id="attachment_158" align="aligncenter" width="500" caption="Nifty 1.2 Tutorial&#47;Demo"]<a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2010&#47;08&#47;nifty-12.png"><img src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2010&#47;08&#47;nifty-12.png" alt="Nifty 1.2 Tutorial&#47;Demo" title="Nifty 1.2 Tutorial&#47;Demo" width="500" height="375" class="size-full wp-image-158" &#47;><&#47;a>[&#47;caption]</p>
<p>The Tutorial&#47;Demo has been greatly improved and updated with Nifty 1.2 informations and is <strong>*THE*<&#47;strong> source to learn how to use Nifty!</p>
<p><strong>Important Information<&#47;strong></p>
<p>Please note that starting with Nifty 1.2 the main nifty.jar is now independend of lwjgl and slick2d. This means you need a nifty-<system>-renderer.jar for your rendering system! For instance, if you would like to use Nifty with a Lwjgl based rendering backend you will now need to download nifty-1.2.jar as well as nifty-lwjgl-renderer-1.0.jar!</p>
<p>Please note that we assume that all of the required jars of your rendering system, like lwjgl.jar, slick.jar and so on are downloaded by yourself. The nifty-<system>-renderer.jar only acts as the adapter between nifty and your rendering backend. They don't come with all the required libs. This decision was simply done under the assumption that Nifty comes as an add on to an existing application.</p>
<p>The Nifty 1.2 compatible jme2 renderer will be available soon after the 1.2 release and Nifty 1.2 will be integrated into jme3 soon as well.</p>
<p>have fun :)</p>
