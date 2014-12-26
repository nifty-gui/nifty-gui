---
layout: post
status: publish
published: true
title: ListBox Overhaul, new and improved API available and some more
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "The ListBox has been greatly improved and got itself a new Nifty Java API.
  You can find an example right here (click the image for a webstart demo) or keep
  reading for all the details below.\r\n\r\n<strong>Example<&#47;strong>\r\n\r\nYou
  can check the first example of the new ListBox online with the following Webstart
  URL:\r\n\r\n<a href=\"http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-default-controls-examples-1.0.jnlp\"><img
  title=\"Bildschirmfoto 2010-12-12 um 23.40.53\" src=\"..&#47;wp-content&#47;2010&#47;12&#47;Bildschirmfoto-2010-12-12-um-23.40.53-300x225.png\"
  alt=\"\" width=\"300\" height=\"225\" &#47;><&#47;a>\r\n\r\nThe source code of this
  project is available online. You can check out the source directly from SVN using
  <a href=\"https:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;svnroot&#47;nifty-gui&#47;nifty-default-controls-examples&#47;trunk\">https:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;svnroot&#47;nifty-gui&#47;nifty-default-controls-examples&#47;trunk<&#47;a>
  or you can browse it online here: <a href=\"http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-default-controls-examples&#47;trunk&#47;\">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-default-controls-examples&#47;trunk&#47;<&#47;a>.\r\n\r\nAnd
  if that is not enough ... we've done the same new API treatment  to the CheckBox
  and we plan to do the same to the remaining Nifty  Standard Controls!\r\n\r\n<strong>Reference
  in the Wiki<&#47;strong>\r\n\r\nYou can find a complete reference about the new
  standard controls in Nifty 1.3 already on the Wiki (for the ListBox and the CheckBox):\r\n\r\n<a
  href=\"http:&#47;&#47;sourceforge.net&#47;apps&#47;mediawiki&#47;nifty-gui&#47;index.php?title=Nifty_Standard_Controls_%28Nifty_1.3%29\">http:&#47;&#47;sourceforge.net&#47;apps&#47;mediawiki&#47;nifty-gui&#47;index.php?title=Nifty_Standard_Controls_%28Nifty_1.3%29<&#47;a>\r\n\r\nAnd
  you can keep reading about all the little Nifty details we've changed and improved
  and especially why.\r\n\r\n"
wordpress_id: 233
wordpress_url: http://nifty-gui.lessvoid.com/?p=233
date: '2010-12-13 00:10:32 +0100'
date_gmt: '2010-12-12 23:10:32 +0100'
categories:
- demo
- design
- docs
tags:
- nifty
- event bus
- eventbus
- listener
- new api
- listbox
- checkbox
comments:
- id: 676
  author: vfsd
  author_email: aferoman@o2.pl
  author_url: ''
  date: '2011-01-08 15:58:02 +0100'
  date_gmt: '2011-01-08 14:58:02 +0100'
  content: Nice work! Thx for this library!
- id: 736
  author: MadJack
  author_email: danyrioux@danyrioux.com
  author_url: ''
  date: '2011-02-13 19:53:35 +0100'
  date_gmt: '2011-02-13 18:53:35 +0100'
  content: "Hey void!\r\n\r\nGlad to see those changes.\r\n\r\nAs you know JME3 still
    uses Nifty 1.2 and I wondered how I was supposed to remove items in that version
    of the ListBox. You say above that it's not impossible, but so far I couldn't
    find a way.\r\n\r\nHow should I tackle this?"
- id: 737
  author: MadJack
  author_email: danyrioux@danyrioux.com
  author_url: ''
  date: '2011-02-13 20:30:11 +0100'
  date_gmt: '2011-02-13 19:30:11 +0100'
  content: "Nevermind. Found it.\r\n\r\nI just can't wait for 1.3 though. Things will
    be MUCH simpler. :)\r\n\r\nKeep it up Void. You're doing great."
- id: 745
  author: Nifty 1.3 Preview Demo | nifty-gui
  author_email: ''
  author_url: http://nifty-gui.lessvoid.com/archives/251
  date: '2011-02-24 01:33:50 +0100'
  date_gmt: '2011-02-24 00:33:50 +0100'
  content: '[...] first control demo for 1.3 we&#8217;ve shown some time ago really
    does not hold up to all of the improvements that have been getting into Nifty
    lately. [...]'
---
<p>The ListBox has been greatly improved and got itself a new Nifty Java API. You can find an example right here (click the image for a webstart demo) or keep reading for all the details below.</p>
<p><strong>Example<&#47;strong></p>
<p>You can check the first example of the new ListBox online with the following Webstart URL:</p>
<p><a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-default-controls-examples-1.0.jnlp"><img title="Bildschirmfoto 2010-12-12 um 23.40.53" src="..&#47;wp-content&#47;2010&#47;12&#47;Bildschirmfoto-2010-12-12-um-23.40.53-300x225.png" alt="" width="300" height="225" &#47;><&#47;a></p>
<p>The source code of this project is available online. You can check out the source directly from SVN using <a href="https:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;svnroot&#47;nifty-gui&#47;nifty-default-controls-examples&#47;trunk">https:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;svnroot&#47;nifty-gui&#47;nifty-default-controls-examples&#47;trunk<&#47;a> or you can browse it online here: <a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-default-controls-examples&#47;trunk&#47;">http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-default-controls-examples&#47;trunk&#47;<&#47;a>.</p>
<p>And if that is not enough ... we've done the same new API treatment  to the CheckBox and we plan to do the same to the remaining Nifty  Standard Controls!</p>
<p><strong>Reference in the Wiki<&#47;strong></p>
<p>You can find a complete reference about the new standard controls in Nifty 1.3 already on the Wiki (for the ListBox and the CheckBox):</p>
<p><a href="http:&#47;&#47;sourceforge.net&#47;apps&#47;mediawiki&#47;nifty-gui&#47;index.php?title=Nifty_Standard_Controls_%28Nifty_1.3%29">http:&#47;&#47;sourceforge.net&#47;apps&#47;mediawiki&#47;nifty-gui&#47;index.php?title=Nifty_Standard_Controls_%28Nifty_1.3%29<&#47;a></p>
<p>And you can keep reading about all the little Nifty details we've changed and improved and especially why.</p>
<p><a id="more"></a><a id="more-233"></a></p>
<p>It all started with a <a href="http:&#47;&#47;sourceforge.net&#47;tracker&#47;?func=detail&amp;aid=2874356&amp;group_id=223898&amp;atid=1059825">feature request for Nifty 1.3<&#47;a> which seems easy to add at first. Removing all or single elements from a Nifty ListBox sounds like a reasonable feature to have available. So I'd try to add this ... and failed miserable! :) The closer I looked into it the more despaired I've become.</p>
<p>What a mess!</p>
<p>Something I'd really love to do with Nifty 2.0 is to add a nice and clean API to the standard Nifty controls. The current way of interacting with most of the Nifty controls is kinda awkward (at least) and I want to improve this. So, I thought, hey, why not try to define a new API for the ListBox as a preparation for this and see how everything works out. Well, it turns out that in the end I've pretty much rewrote the whole ListBox! =) And that the new one is way better than what you currently have available!</p>
<p>One of the shortcomings of the old ListBox was speed. Internally it directly added the Nifty element tree adding elements dynamically and then used a Scrollpanel to scroll around that (maybe) huge list of elements. So basically it always rendered all elements and used the clipping of the element to hide everything that was not currently seen. Not a great idea after all :)</p>
<p>Another problem was the API and that's a little difficult to interact with Niftys internal screen representation. Although it's not impossible to dynamically add&#47;remove elements from Nifty this might not be the ideal solution to the ListBox problem.</p>
<p>So what did we changed? Well, actually a lot! :)</p>
<p><strong>The new ListBox:<&#47;strong></p>
<ul>
<li>Instead of relying upon the scroll panel we now only use Nifty elements for what you can see and simply replace the content of the elements when you scroll to a big list of items. So, when your ListBox only display 4 elements there really are only 4 Nifty elements allocated. Of course the ListBox allows many more Items to be added but in that case only the content of the 4 elements are replaced. You can now specify how many elements you want your display to show with the "displayItems" attribute.<&#47;li><br />
<&#47;ul></p>
<ul>
<li>Scrollbars are now optional and you can disable them completly with the "vertical" and "horizontal" attributes. Simply adding for instance vertical="false" to the ListBox will hide the vertical Scrollbar.<&#47;li><br />
<&#47;ul></p>
<ul>
<li>The ListBox now supports single as well as multiple selections. This means you can now configure your ListBox to allow the selection of only a single item or that you want to select multipe items. If you don't want to allow selection at all you can even disable the selection completly.<&#47;li><br />
<&#47;ul></p>
<ul>
<li> Instead of relying on storing Strings the new ListBox allows you to story your own Objects directly. So say you want your user to select different space ships by the name of the ship you could simply store your "Ship" instances directly in the ListBox. Nifty determines what text it should display for your instance by calling toString() of your objects. If this is not suitable for you, you can change this by setting a specifc <a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-default-controls&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;controls&#47;ListBoxViewConverter.java?revision=1167&amp;view=markup">ListBoxViewConverter<&#47;a> for your class.<&#47;li><br />
<&#47;ul></p>
<ul>
<li>The new ListBox now features a pretty complete API <a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-default-controls&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;controls&#47;ListBox.java?revision=1167&amp;view=markup">ListBox<&#47;a> that allows you to dynamically add, remove, select, clear items to the ListBox. This is now all available using generics..<&#47;li><br />
<&#47;ul><br />
<strong>EventBus Notification<&#47;strong></p>
<p>In the process of updating the ListBox we've noticed that we're missing a nice way to allow the controls to notify other components of the system about things happening. If the selection of the ListBox is changed we'd like other parts to be notified about that event. Well, the first thing that comes to ones mind is of course the Listener&#47;Observer pattern. But there are some problems with this approach, mostly the strong binding between the objects that communicate. There is a <a href="http:&#47;&#47;www.osgi.org&#47;wiki&#47;uploads&#47;Links&#47;whiteboard.pdf">great PDF available from the OSGI people<&#47;a> that discusses some of the problems of the Listener pattern.</p>
<p>Having previously worked with GWT 2.x and its event bus architecture we'd like to have someting similar for Nifty too. What we want is a loose coupling between Nifty (as the creator of events) and your application (as the receiver of the events). As it turns out a publish&#47;subscribe mechanism allows us to do just that! Nifty publishes events to some "global" EventBus. And your application on the other hand subscribes to this EventBus for all the events that it needs to handle. What sounds not very different from the standard Listener pattern at first is in fact a great improvement! There is only a dependency to the EventBus and not between the objects that communicate! Which helps a lot to decouple the objects from one another. Nifty does not need to know who will receive the event in the end. It just creates the event and publishes it to the EventBus and everybody interessted in it will be notified.</p>
<p>As it turns out there is a neat little project available that implements that mechanism for us. It is called <a href="http:&#47;&#47;www.eventbus.org&#47;">EventBus<&#47;a> and is available as Open Source under the Apache License, Version 2.0. It's only 80KB in size, so it should not hurt the download&#47;memory footprint of Nifty that much. Nifty is now using the EventBus project for ListBox and CheckBox notifications which worked very very well so far! You subscribe to the EventBus using the id of the element as a topic and you'll receive all events that the control with the given id produces. In the case of the ListBox this means that you receive instances of the <a href="http:&#47;&#47;nifty-gui.svn.sourceforge.net&#47;viewvc&#47;nifty-gui&#47;nifty-default-controls&#47;trunk&#47;src&#47;main&#47;java&#47;de&#47;lessvoid&#47;nifty&#47;controls&#47;ListBoxSelectionChangedEvent.java?revision=1169&amp;view=markup">ListBoxSelectionChangedEvent<&#47;a>. This basically sends you the current selection of the ListBox everytime the selection has been changed :)</p>
<p>Good Times,<br />
void</p>
<p>PS: And if you NOW don't feel like christmas is already here then I can't help you =)</p>
