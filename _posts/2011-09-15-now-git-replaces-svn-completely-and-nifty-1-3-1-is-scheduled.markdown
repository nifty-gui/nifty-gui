---
layout: post
status: publish
published: true
title: now git replaces svn completely and Nifty 1.3.1 is scheduled
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 321
wordpress_url: http://nifty-gui.lessvoid.com/?p=321
date: '2011-09-15 01:08:01 +0200'
date_gmt: '2011-09-15 00:08:01 +0200'
categories:
- design
tags: []
comments:
- id: 1128
  author: Goffredo
  author_email: goffredo.goffrei@gmail.com
  author_url: ''
  date: '2011-12-20 21:31:57 +0100'
  date_gmt: '2011-12-20 20:31:57 +0100'
  content: "Hi, I can't seem to access the GIT repository the way sourceforge suggests.
    It only happens with nifty gui though, other projects are fine. Could you try
    and verify if it's only a problem on my side? I am trying to clone from git:&#47;&#47;nifty-gui.git.sourceforge.net&#47;gitroot&#47;nifty-gui&#47;nifty-gui\r\n\r\nThanks
    for nifty, I love it!"
- id: 1129
  author: goffredo
  author_email: goffredo.goffrei@gmail.com
  author_url: ''
  date: '2011-12-20 22:31:39 +0100'
  date_gmt: '2011-12-20 21:31:39 +0100'
  content: "Hi, I actually can't access the git repository. I try to clone from the
    link sourceforge gives, but I get an error saying that the repository was not
    found. Is it a problem on my side?\r\n\r\nBtw, thanks for nifty, I love it!"
- id: 1130
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2011-12-21 18:43:32 +0100'
  date_gmt: '2011-12-21 17:43:32 +0100'
  content: |-
    Unfortunatly one can't change the text displayed under code &#47; git and that URL is slightly wrong :&#47;

    The correct URL is: git:&#47;&#47;nifty-gui.git.sourceforge.net&#47;gitroot&#47;nifty-gui&#47;nifty
- id: 1133
  author: goffredo
  author_email: goffredo.goffrei@gmail.com
  author_url: ''
  date: '2011-12-23 01:39:46 +0100'
  date_gmt: '2011-12-23 00:39:46 +0100'
  content: Thanks! Sorry for the double post! Would you maybe at least make a post
    so this error gets more visibility (so that others can download from git too!)
- id: 1135
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2011-12-23 17:16:57 +0100'
  date_gmt: '2011-12-23 16:16:57 +0100'
  content: Yes, I'll do that ... but a bit later. Just need to post something else
    first, which I can't talk about yet ^^
---
<p>The idea to keep svn around for a bugfix release 1.3.1 while keeping the main development for Nifty 1.4 in git sounded reasonable at first. But since we've got so quickly used to git going back to svn each time really felt odd :) And although its possible to use git and svn together it kinda made no sense to keep svn around at all. So now everything is git which makes things more clear. Good bye svn!</p>
<p>So there is now a 1.3.1 branch in git. But wait! Doesn't Nifty consist of lots of individual jars (nifty, controls, style, renderers, sound-system implementations and so on)? So we need a branch for each individual jar, don't we?</p>
<p>Yes, but fortunately we can combine all of the individual maven projects into a <a href="http:&#47;&#47;www.sonatype.com&#47;books&#47;mvnex-book&#47;reference&#47;multimodule.html">multi-module maven project<&#47;a>. This way we can now build the nifty-core project together with all of the dependent jars. This gives us the additional benefit to use the parent pom to define versions for plugins we use as well as common dependencies in one place instead of all the individual poms.</p>
<p>To combine the different repositories into one we've used yet another feature of git. It's possible to treat another git repository as a remote repo when both exist on the same filesystem. And this his how it works:</p>
<p><code>git remote add <name-we-want-to-give-this> &#47;path&#47;to&#47;other&#47;repo&#47;.git<br />
git fetch <name-we-want-to-give-this><&#47;code></p>
<p>And that's all! We can now merge with this remote one exactly the same as with any regular branch! :)</p>
<p>So with all of the projects merged into one repository and with all of the maven multi-module setup in place everything looks pretty again. Instead of the individual projects&#47;repos we only need to branch a single git repository&#47;maven project. Here is the <a href="http:&#47;&#47;nifty-gui.git.sourceforge.net&#47;git&#47;gitweb.cgi?p=nifty-gui&#47;nifty;a=tree;h=refs&#47;heads&#47;1.3.1;hb=refs&#47;heads&#47;1.3.1">Nifty 1.3.1 Branch in the Git Repository<&#47;a>.</p>
<p>The current development is concentrating on Nifty 1.3.1. This version will contain bugfixes and improvements based on the Nifty 1.3 release. 1.3.1 will be compatible with 1.3 and will especially improve Niftys performance. For instance with some of the latest commits Nifty will now generate much less new objects each frame which will reduces the amout of GC runs necessary by quite a bit already :)</p>
<p>There are some other performance improvements scheduled as well especially to improve the rendering speed of text elements.</p>
<p>void</p>
