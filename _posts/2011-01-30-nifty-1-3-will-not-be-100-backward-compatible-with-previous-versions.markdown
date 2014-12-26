---
layout: post
status: publish
published: true
title: Nifty 1.3 will not be 100% backward compatible with previous versions!
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "So, now that I have your attention - it's not so bad as it sounds, really
  :)\r\n\r\nBasically there have been two changes that might or might not influence
  your existing Nifty XML&#47;Code base. The <label> Element has been removed (please
  use <text style=\"nifty-label\"> or <control name=\"label\"> as a replacement. see
  more details below) and when you're already using the Standard Controls from Java,
  then the access to the Controls has now been changed too. Besides these two issues
  everything else should still work - and in some cases work better or does actually
  work for the first time with Nifty 1.3 :)\r\n\r\nSo there is nothing to be worried
  about too much.\r\n\r\nHowever there is one more important thing. If you've used
  Nifty XSD validation then the XSD Namespace has now been changed too. The new one
  is: \"http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-1.3.xsd\" instead of the
  old one \"http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty.xsd\". So you'll need
  to download the new version and modify the nifty xml tag if you want to use XML
  editor support to write the new Nifty 1.3 XML.\r\n\r\nThe correct way to specifiy
  the XSD in XML-Files for Nifty 1.3 is now:\r\n<pre class=\"brush:xml\"><nifty xmlns=\"http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-1.3.xsd\"
  xmlns:xsi=\"http:&#47;&#47;www.w3.org&#47;2001&#47;XMLSchema-instance\" xsi:schemaLocation=\"http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-1.3.xsd
  http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-1.3.xsd\"><&#47;pre>\r\nKeep
  reading for a detail explanation of the two changes mentioned above.\r\n\r\n"
wordpress_id: 241
wordpress_url: http://nifty-gui.lessvoid.com/?p=241
date: '2011-01-30 19:59:40 +0100'
date_gmt: '2011-01-30 18:59:40 +0100'
categories:
- Uncategorized
tags: []
comments:
- id: 714
  author: MadJack
  author_email: danyrioux@danyrioux.com
  author_url: ''
  date: '2011-02-01 17:21:05 +0100'
  date_gmt: '2011-02-01 16:21:05 +0100'
  content: "Great new, but the huge question is unanswered.\r\n\r\nETA! :P\r\n\r\nWhen?
    :D"
- id: 715
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2011-02-01 21:45:21 +0100'
  date_gmt: '2011-02-01 20:45:21 +0100'
  content: that's easy to answer really ... WHEN ITS DONE! :D
- id: 726
  author: Didialchichi
  author_email: didialchichi@hotmail.fr
  author_url: ''
  date: '2011-02-10 01:09:49 +0100'
  date_gmt: '2011-02-10 00:09:49 +0100'
  content: "Hi !\r\nWhen I saw the post I feared some bigger incompatibility.\r\nLike
    you said, changing the  element to a  one is not a big deal :)\r\n\r\nWhat is
    worrying me is the last part in fact : New Standard Control API\r\n\r\nOn my project,
    I have created many custom controls.\r\nI access to them via the old method :\r\nlifeTube
    \t\t= screen.findControl(\"lifeTube\", FillingTubeControl.class);\r\n\r\nWill
    I still be able to do so? \r\nOr do I have to convert all my custom Controls to
    NiftyControls ?"
- id: 730
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2011-02-12 15:47:28 +0100'
  date_gmt: '2011-02-12 14:47:28 +0100'
  content: |-
    Hi there,

    no worries there! You'll still be able to use the "old" way to access your own custom controls! :)

    The new NiftyControl stuff is reserved mainly for the "build in" standard controls so there is no need to convert them at all!

    Thanks for supporting Nifty,
    void

    PS: Well, there is something hiding in there that is not clearly expressed yet. There is a difference between a "real" control (like a button, a ListBox and so on) and a custom control that is "only" used as a template for a collection of Nifty elements that you could use multiple times on a screen. I think the new NiftyControl interface&#47;hierarchy should be used for all of the "real" controls. So if you would create some control that Nifty does not offer out of the box, let's say a TreeView or something then that control really should be a NiftyControl. On the other hand if you simply want to use the template way of things, like you just want to use a couple of elements (and other controls) together and you want to use them on several screens or several times on the same screen then you should use the "old" Control. Using this perspective then we should rename the Control into Template or something to really make things clear :) But it think this won't happen in Nifty 1.3 - so there is nothing to be afraid of :)
- id: 733
  author: Didialchichi
  author_email: didialchichi@hotmail.fr
  author_url: ''
  date: '2011-02-13 08:10:18 +0100'
  date_gmt: '2011-02-13 07:10:18 +0100'
  content: "Perfect!\r\nThanks for the answer :)"
---
<p>So, now that I have your attention - it's not so bad as it sounds, really :)</p>
<p>Basically there have been two changes that might or might not influence your existing Nifty XML&#47;Code base. The <label> Element has been removed (please use <text style="nifty-label"> or <control name="label"> as a replacement. see more details below) and when you're already using the Standard Controls from Java, then the access to the Controls has now been changed too. Besides these two issues everything else should still work - and in some cases work better or does actually work for the first time with Nifty 1.3 :)</p>
<p>So there is nothing to be worried about too much.</p>
<p>However there is one more important thing. If you've used Nifty XSD validation then the XSD Namespace has now been changed too. The new one is: "http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-1.3.xsd" instead of the old one "http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty.xsd". So you'll need to download the new version and modify the nifty xml tag if you want to use XML editor support to write the new Nifty 1.3 XML.</p>
<p>The correct way to specifiy the XSD in XML-Files for Nifty 1.3 is now:</p>
<pre class="brush:xml"><nifty xmlns="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-1.3.xsd" xmlns:xsi="http:&#47;&#47;www.w3.org&#47;2001&#47;XMLSchema-instance" xsi:schemaLocation="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-1.3.xsd http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-1.3.xsd"><&#47;pre><br />
Keep reading for a detail explanation of the two changes mentioned above.</p>
<p><a id="more"></a><a id="more-241"></a><strong>Label is now a Control<&#47;strong></p>
<p>The Nifty core only knows about
<panel>, <image> and <text> Elements which forms the base of all other elements that Nifty provides, including all of the Standard Controls that are build upon these Elements. So <text> is basically a low level output of text which means you'll need to specify the font to be used, the text, the color and so on. The <label> Element was just a <text> Element with a predefined style applied (style="nifty-label"). So this allows you to change the look of all Labels by simply changing the "nifty-label" style and you won't need to specify the font each time you want to output a text because this is already part of the style.</p>
<p>If you want to change the Label&#47;Text dynamically from Java you'll need to access the TextRenderer class of the Element and change the Text property on this class. This works but requires you to know the internals of Nifty and the Text-Elements. It would be much nicer if you could simply call setText() on the Label directly. And this was the main motivation for the current change.</p>
<p>So the Label is now a Standard Control which means you'll need to create it with "<control name="label">" instead of "<label>". This is a bit more to type, granted, but it makes clear that Label is just like all of the other Nifty Standard Controls. You can still use <text> of course and you can still modify the Text with the TextRenderer class. The benefit of having the Label as a new Control is that with the 1.3 Control API you can access the Label more directly and you can modify it as you can modify all other controls.</p>
<p>So for dynamic Labels that you'd like to use to show changing text informations you should now consider the Label control instead of the <text> Element.</p>
<p><strong>New Standard Control API<&#47;strong></p>
<p>Besides lots of bugfixes and improvements of the Nifty core, Nifty 1.3 will mostly concentrate on the Standard Controls. Buttons, Textfields, Listbox and so on will now be more powerful and easier to use from Java compared to previous Nifty versions (You should keep an eye on the <a href="http:&#47;&#47;sourceforge.net&#47;apps&#47;mediawiki&#47;nifty-gui&#47;index.php?title=Nifty_Standard_Controls_%28Nifty_1.3%29">Nifty 1.3 Standard Controls Reference in the Nifty wiki<&#47;a> for even more details which is constantly updated).</p>
<p>Instead of accessing the Controller of the Control directly there is now a dedicated interface available for each of the Controls. This Interface is dedicated to the Java API the control provides to you. Once you've retrieved the new Interface you can use the control through the API which offer you more higher level access to the functionality of the Control. So for instance to get the Selection of a ListBox you can now simply call:</p>
<pre class="brush:java">listBox.getSelection();<&#47;pre><br />
which will give you the Selection of the ListBox as a List of your Model Objects back (again, see the Reference in the Wiki for examples and more informations).</p>
<p>Access to the the API is through a new method on the Screen. To get the Button API:</p>
<pre class="brush:java">Button button = screen.findNiftyControl("appendButtonId", Button.class);<&#47;pre><br />
As the second parameter (in this case Button.class) the API Interface is required and the Nifty Id of the Element representing the control as the first parameter. The object you get back is the API Interface.</p>
<p>That's all for now. Thanks for your patience waiting for Nifty 1.3! It will be worth it :D</p>
<p>void</p>
