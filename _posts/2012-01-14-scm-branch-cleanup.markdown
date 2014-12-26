---
layout: post
status: publish
published: true
title: scm branch cleanup
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 416
wordpress_url: http://nifty-gui.lessvoid.com/?p=416
date: '2012-01-14 01:31:27 +0100'
date_gmt: '2012-01-14 00:31:27 +0100'
categories:
- Uncategorized
tags: []
comments:
- id: 1177
  author: cghislai
  author_email: charlyghislain@gmail.com
  author_url: ''
  date: '2012-01-16 15:10:27 +0100'
  date_gmt: '2012-01-16 14:10:27 +0100'
  content: "Hi!\r\n\r\nIt would be great for 1.4 to see a more java-friendly API.\r\n-
    The java builders are great, but I often found myself building the whole tree
    then parsing it to get some Element&#47;Controller pointers.\r\nSomething like
    \r\nScreen screen = new Screen(\"main\");\r\nLayer layer = new Layer(\"layer\");\r\nlayer.childLayoutVertical();\r\nscreen.addLayer(layer);\r\nScreenController
    ctrl = screen.getController();\r\n\r\nOr even better,\r\nscreen.addLayer(new Layer()
    {\r\n     initParam() {\r\n         childLayoutVertical();\r\n     }\r\n});\r\n-
    Concerning the findControl methods, why do we have to pass the class? Can't we
    just get the interface and cast it ourselves if necessary?\r\n- In the control
    builder, one can pass a Controller instance ( controller(myControllerInstance);
    ). This is total useless as only the class name is taken into account. It would
    be nice to be able to pass a real controller instance that will be bound when
    the elemtn will be built.\r\n- When  a new element is built, is it cached in any
    way? I use a windowed interface, and i notice a delay upon each window creation.
    It would be nice if i could create one once, then clone it if i need more instance.
    Somethinglike\r\nprivate static MyWindow instance  = new MyWindow();\r\n&#47;&#47;\r\nMyWindow
    newWindow = instance.cloneElement();\r\nnewWindow.setController(new MyWindowController(Object
    someparam));\r\nmyDroppableArea.add(newWindow);\r\n\r\nI mainly use java to build
    ui, and I often find some possible improvement. I should write them down as its
    quickly forgotten.\r\n\r\nAnyway, many thanks for this piece of code! You are
    doing a great job!"
- id: 1181
  author: Polygnom
  author_email: polygnom@hab-verschlafen.de
  author_url: ''
  date: '2012-01-19 10:41:40 +0100'
  date_gmt: '2012-01-19 09:41:40 +0100'
  content: Wow, very nice you're on GitHub now ;)  GitHub is simply awesome :D
- id: 1203
  author: Luiz Felipe
  author_email: luiz.felipe.gp@gmail.com
  author_url: ''
  date: '2012-02-15 15:34:11 +0100'
  date_gmt: '2012-02-15 14:34:11 +0100'
  content: "Hey, I am gonna use nifty-gui for my project and I think it's very awesome
    what you're doing!\r\n\r\nPlease when possible update the wiki. I notice that
    with the simplest tutorials that are completely different and even use deprecated
    classes on the examples :)\r\n\r\nAs soon as I get very familiarized with it,
    I'll update it too. But right now, in the learning curve, the wiki is making me
    learn the wrong (old) stuff.\r\n\r\nThanks again for the great library !"
---
<p>So now that 1.3.1 is out of the way, we've cleaned up the branches in git.</p>
<p>There are now two main development branches available:</p>
<p><strong>1.3<&#47;strong><br />
The branch formerly known as 1.3.1. This branch will be the base for an eventual 1.3.2 release. Mostly bugfixes should go in there but maybe some improvements will find their way into this as well.</p>
<p><strong>master<&#47;strong><br />
The main branch and the base for any 1.4 development. This will be an unstable version (1.4.0-SNAPSHOT) for a while but all the new nifty things will be in there.</p>
<p>While speaking of 1.4 there is not yet a final plan for it. We'll need to sort out all of the bug and feature requests first. Please feel free to suggest other things you'd like to see in the comments or on the forum.</p>
<p>Both branches are available at <a href="http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;develop">sf.net<&#47;a> and at <a href="https:&#47;&#47;github.com&#47;void256&#47;nifty-gui">github<&#47;a>. Synchronizing both repos is a manual process at the moment but works pretty good thanks to git!</p>
<p>Ah and one final word about the git repository at sf.net: Unfortunately the sf.net "code &#47; git" menu lists the wrong repository. I've talked to their tech support on IRC and this can't be changed at the moment :&#47;</p>
<p>The "develop" menu has the correct URL which is:</p>
<p><code>git clone git:&#47;&#47;nifty-gui.git.sourceforge.net&#47;gitroot&#47;nifty-gui&#47;nifty<&#47;code></p>
<p>So the <strong>correct<&#47;strong> URL ends with "nifty" and <strong>not<&#47;strong> with "nifty-gui"! Sorry about this =)</p>
<p>void</p>
