---
layout: post
status: publish
published: true
title: Nifty Slick Integration in Nifty Version 0.0.4
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 19
wordpress_url: http://nifty-gui.lessvoid.com/archives/19
date: '2008-08-03 20:16:59 +0200'
date_gmt: '2008-08-03 19:16:59 +0200'
categories:
- bubble
tags:
- Slick
comments:
- id: 290
  author: Tobse
  author_email: tobse.fritz@gmx.de
  author_url: ''
  date: '2008-08-10 00:49:09 +0200'
  date_gmt: '2008-08-09 23:49:09 +0200'
  content: I tested Nifty v0.3 and it looks really great! I'am anxious for your next
    release. This is the GUI I needed for my game development. Keep it up!
- id: 291
  author: eriq
  author_email: eriq.adams@gmail.com
  author_url: http://eriqadams.blogspot.com
  date: '2008-08-28 09:28:38 +0200'
  date_gmt: '2008-08-28 08:28:38 +0200'
  content: "I'll wait for next release. When Nifty GUI 0.4 released ?\r\nI will try
    it in my game project.\r\n\r\nvoid: today :D"
- id: 292
  author: dlx
  author_email: jokkmokk@gmx.at
  author_url: ''
  date: '2008-09-07 13:07:17 +0200'
  date_gmt: '2008-09-07 12:07:17 +0200'
  content: Nifty looks really great. But imho it would be better to set the scope
    of the lwjgl and slick dependencies to provided.
- id: 293
  author: void
  author_email: void@lessvoid.com
  author_url: ''
  date: '2008-09-08 21:46:43 +0200'
  date_gmt: '2008-09-08 20:46:43 +0200'
  content: "why the provided scope dlx? this would mean you would need to know which
    version of slick and lwjgl are required and add these dependencies to your pom.xml
    on your own. especially the lwjgl stuff gets pretty tricky because there are actual
    three libs used (lwjg, jinput and lwjgl-util).\r\n\r\nwhy do you need this? at
    the moment the only thing I can imagine is, when you'd need a special version
    of the libs and you can do that easily within your pom.xml :)\r\n\r\nanything
    I am missing? o_O"
- id: 294
  author: dlx
  author_email: jokkmokk@gmx.at
  author_url: ''
  date: '2008-09-09 08:50:02 +0200'
  date_gmt: '2008-09-09 07:50:02 +0200'
  content: I'm using slick from svn and ideally I'd like to just add a jar to my classpath
    to use nifty. Modifying the pom doesn't sound optimal.
- id: 295
  author: void
  author_email: void@lessvoid.com
  author_url: ''
  date: '2008-09-09 21:37:20 +0200'
  date_gmt: '2008-09-09 20:37:20 +0200'
  content: "Why? :) The whole point of maven is, that you don't need to manually manage
    or know the dependencies. Just add nifty to your pom.xml as a dependency in your
    project and you're done because it *knows* what it needs to compile, run and even
    downloads missing jar files automatically for you.\r\n\r\nTake for instance the
    tapestry pom.xml: http:&#47;&#47;mirrors.ibiblio.org&#47;pub&#47;mirrors&#47;maven&#47;tapestry&#47;poms&#47;tapestry-4.0.2.pom.
    Imagine all of the dependencies would have the scope provided ... I would probably
    have a hard time figuring out all the dependencies with the right version on my
    own! o_O\r\n\r\nOf course you can use your own version of slick. Why not just
    add your \"slick-build-from-svn-jar\" to your pom.xml as a seperate dependency
    to make sure that maven will use the right version? :)\r\n\r\nThe slick and lwjgl
    version is tested with nifty and works. I don't know what will happen if you use
    another version :&#47;"
- id: 296
  author: dlx
  author_email: jokkmokk@gmx.at
  author_url: ''
  date: '2008-09-11 08:12:15 +0200'
  date_gmt: '2008-09-11 07:12:15 +0200'
  content: Yes, but those dependencies are not transient but direct dependencies of
    my project, so I do want and need to know about them.
- id: 297
  author: void
  author_email: void@lessvoid.com
  author_url: ''
  date: '2008-09-11 21:21:07 +0200'
  date_gmt: '2008-09-11 20:21:07 +0200'
  content: "Ok, lets sort this out =)\r\n\r\nFirst: You can always download nifty-<version>.jar,
    install it into your local maven repository (mvn install:install ...) and use
    it as is. In this case there won't be any transient dependencies. Exactly what
    you want! :)\r\n\r\nSecond: You will get the nifty dependencies only if you actually
    use a remote repository, f.i. http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo
    (like the nifty-examples).\r\n\r\nIn this case let's look at your pom.xml <strong>before<&#47;strong>
    nifty:\r\n<code>\r\n<dependencies>\r\n&nbsp;&nbsp;...\r\n&nbsp;&nbsp;<dependency>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<groupId>slick<&#47;groupId>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<artifactId>slick<&#47;artifactId>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<version>your-version-from-svn-xxx<&#47;version>\r\n&nbsp;&nbsp;<&#47;dependency>\r\n<&#47;dependencies>\r\n<&#47;code>\r\n\r\nand
    <strong>with<&#47;strong> nifty:\r\n<code>\r\n<dependencies>\r\n&nbsp;&nbsp;...\r\n&nbsp;&nbsp;<dependency>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<groupId>slick<&#47;groupId>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<artifactId>slick<&#47;artifactId>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<version>your-version-from-svn-xxx<&#47;version>\r\n&nbsp;&nbsp;<&#47;dependency>\r\n&nbsp;&nbsp;<dependency>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<groupId>lessvoid<&#47;groupId>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<artifactId>nifty<&#47;artifactId>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<version>0.0.4<&#47;version>\r\n&nbsp;&nbsp;<&#47;dependency>\r\n<&#47;dependencies>\r\n<&#47;code>\r\nIn
    this case Maven should use <strong>your-version-from-svn-xxx<&#47;strong> independent
    from the one nifty uses! Isn't this what you want?"
---
<p><a href="http:&#47;&#47;slick.cokeandcode.com&#47;" title="Slick 2D">Slick 2D<&#47;a> is a great library for game development. With the upcoming release of Nifty GUI 0.0.4 there will be an even easier way to integrate your Nifty GUI into your Slick application.</p>
<p>The current Nifty GUI Version 0.0.3 can already be used with Slick but it might become a little tricky to save&#47;restore OpenGL state when switching between Slick and Nifty rendering. The basic approach would be to call niftys render() method after you've rendered all of your Slick graphics. This "manual" rendering is the usual way for Nifty integration and is shown in the <a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty&#47;getting-started&#47;hello-world.html" title="Nifty Hello World">Hello World example<&#47;a> on the <a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty&#47;index.html" title="Nifty Project Website">Nifty Project Website<&#47;a>.</p>
<p>With the Release of Nifty Version 0.0.4 there will be a Slick GameState implementation available for easy of use. If you're new to Slick GameStates there's an <a href="http:&#47;&#47;slick.cokeandcode.com&#47;wiki&#47;doku.php?id=state_based_games" title="Slick GameState wiki">article in the Slick wiki<&#47;a> about the basic principles. With Nifty 0.0.4 you get a NiftyGameState class that extends the Slick BasicGameState. So you can simply use an instance of this new class (or a subclass) and voila you have the ability to add a Nifty GUI to your Slick app in no time :)</p>
<p>Example:</p>
<pre lang="java5">public void initStatesList(final GameContainer container) throws SlickException {<br />
&nbsp; NiftyGameState state = new NiftyGameState(MENU_ID);<br />
&nbsp; state.fromXml("mainmenu.xml", new ScreenController() {<br />
&nbsp;   public void bind(Nifty nifty, Screen screen) {<br />
&nbsp;   }<br />
&nbsp;   public void onEndScreen() {<br />
&nbsp;   }<br />
&nbsp;   public void onStartScreen() {<br />
&nbsp;   }<br />
&nbsp; });<br />
  addState(state);<br />
}<&#47;pre><br />
Nifty! :)</p>
