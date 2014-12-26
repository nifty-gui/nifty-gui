---
layout: post
status: publish
published: true
title: Dynamically Changing Button Text&#47;Width and the Centerlayout
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "Jattra tries to dynamically change button text from within Java and he needs
  a Button that automatically resizes according to the width of the changed text.\r\n\r\nYou
  can follow both threads at the Nifty Help Forum at sf.net:\r\n\r\n<ul>\r\n<a href=\"https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;forums&#47;forum&#47;807893&#47;topic&#47;3546927\">Changing
  Button Text Thread<&#47;a><&#47;ul>\r\n\r\n<ul>\r\n<a href=\"https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;forums&#47;forum&#47;807893&#47;topic&#47;3558041\">Resizing
  Buttons to Text Thread<&#47;a>.<&#47;ul>\r\n\r\nRead the complete blog post for
  details.\r\n"
wordpress_id: 122
wordpress_url: http://nifty-gui.lessvoid.com/?p=122
date: '2010-02-21 18:03:55 +0100'
date_gmt: '2010-02-21 17:03:55 +0100'
categories:
- bubble
- design
tags:
- buttons
- changing text
- button text
- button control text
comments:
- id: 364
  author: qubodup
  author_email: qubodup@gmail.com
  author_url: http://qubodup.net
  date: '2010-04-07 04:49:21 +0200'
  date_gmt: '2010-04-07 03:49:21 +0200'
  content: "gradients make me cry :)\r\n\r\nIf you're looking for gui elements to
    use, the following three might be of use:\r\n\r\nhttp:&#47;&#47;opengameart.org&#47;content&#47;magic-buttons\r\nhttp:&#47;&#47;opengameart.org&#47;content&#47;10-basic-message-boxes\r\nhttp:&#47;&#47;opengameart.org&#47;content&#47;2d-bars"
---
<p>Jattra tries to dynamically change button text from within Java and he needs a Button that automatically resizes according to the width of the changed text.</p>
<p>You can follow both threads at the Nifty Help Forum at sf.net:</p>
<ul>
<a href="https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;forums&#47;forum&#47;807893&#47;topic&#47;3546927">Changing Button Text Thread<&#47;a><&#47;ul></p>
<ul>
<a href="https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;forums&#47;forum&#47;807893&#47;topic&#47;3558041">Resizing Buttons to Text Thread<&#47;a>.<&#47;ul></p>
<p>Read the complete blog post for details.<br />
<a id="more"></a><a id="more-122"></a></p>
<p>Dynamically changing text was easy and has been added to the ButtonControl. You can now simply change text with the following code from java:</p>
<pre lang="java5">screen.findControl("backButton", ButtonControl.class).setText("New Text");<&#47;pre></p>
<p>Automatically resizing Buttons was a bit more involving tho.</p>
<p>Nifty actually has build in support for resizing elements according to other elements. But before we can understand what that means we first need to know a bit about the internal workings of a Nifty Button Control.</p>
<p>A Nifty Button consists of a Panel element with childLayout="center" and one child element which is a Label element that represents the actual button text. So far so good and the standard style set (nifty-style-black) simply adds a fixed width to the button.</p>
<p>So simply using the Button control:</p>
<pre lang="xml"><control name="button" label="Hello World" &#47;><&#47;pre></p>
<p>will give you a button with a style specific fixed width.</p>
<p>You can override this fixed width when you use the button control like that:</p>
<pre lang="xml"><control name="button" width="4532px" label="Hello Large Button World" &#47;><&#47;pre></p>
<p>This works pretty good if you don't need to change the button label text. No matter what you do to a button with a fixed width it will always stay at this width (Unless you change the width constraint from code as one of Jattras code examples shows. And although this works it is a bit cumbersome but until now the only way to achieve resizing buttons).</p>
<p>So what should we do to fix this?</p>
<p>Well, actually there is not really much to do. If you ever used a childLayout="vertical" on something without setting a width but every child in that special element had a fixed size you already have seen that Nifty actually resizes the parent element!</p>
<p>And this is basically default behaviour in Nifty: Whenever Nifty finds an element without a width (or height) constraint it looks at all the child elements and if they all have a fixed width (or height) it automatically resizes the parent element to the size of all child elements.</p>
<p>And this is just what we need here :)</p>
<p>As stated above the Button control uses childLayout="center" and up until now the center layout did not support this kind of auto-resizing feature. But this has been added to svn a moment ago.</p>
<p>So whenever Nifty encounters an element with childLayout="center" and the child element has a fixed width then it will resize the element to the child width. And this works for our button control too! And it works even when dynamically changing text from within Java :D</p>
<p>Well, there is one thing to note tho. For backward compatibility the nifty-style-black still sets a fixed width to the button. So to enable the auto resizing you have to override the witdh of the button with an empty String.</p>
<p>Example to enable the auto resizing Button:</p>
<pre lang="xml"><control name="button" width="" label="autoresizing button"&#47;><&#47;pre></p>
<p>And that's it. :)</p>
<p><strong>Note:<&#47;strong><br />
Another thing to note is, that the change to the center layout logic now requires you to set a width to all elements that use childLayout="center". Before this change there was no auto resizing done to elements that use center layout. To prevent the auto resizing that will be used on that elements now you need to specify a width to this elements.</p>
