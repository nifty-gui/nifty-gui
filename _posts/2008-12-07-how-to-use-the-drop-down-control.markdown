---
layout: post
status: publish
published: true
title: How to use the Drop Down Control
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "Nifty 0.0.5 adds support for a Drop Down Control. Here we show how to actually
  use it.\r\n\r\n<strong>XML<&#47;strong>\r\n\r\nFirst of all you need to add the
  standard Nifty controls to your xml. To keep things easy for the moment we use the
  default Nifty styles too.\r\n<pre lang=\"xml\"><!-- load default styles and controls
  --><&#47;pre>\r\nSo adding a Drop Down Control works like adding any other control:\r\n\r\nBasically
  you address the control you want to add, in this case it's \"dropDownControl\".
  And you need to give your control an id too, so that you can reference it later
  (\"dropDown1\").\r\n\r\n"
wordpress_id: 49
wordpress_url: http://nifty-gui.lessvoid.com/?p=49
date: '2008-12-07 11:01:32 +0100'
date_gmt: '2008-12-07 10:01:32 +0100'
categories:
- docs
tags:
- drop down control how to
comments: []
---
<p>Nifty 0.0.5 adds support for a Drop Down Control. Here we show how to actually use it.</p>
<p><strong>XML<&#47;strong></p>
<p>First of all you need to add the standard Nifty controls to your xml. To keep things easy for the moment we use the default Nifty styles too.</p>
<pre lang="xml"><!-- load default styles and controls --><&#47;pre><br />
So adding a Drop Down Control works like adding any other control:</p>
<p>Basically you address the control you want to add, in this case it's "dropDownControl". And you need to give your control an id too, so that you can reference it later ("dropDown1").</p>
<p><a id="more"></a><a id="more-49"></a><br />
<strong>Java<&#47;strong></p>
<p>Back in Java you want to populate your new Drop Down Control with data.</p>
<p>For this you need the Drop Down Control Class. With Nifty 0.0.5 there is a new method on the Nifty Screen class that makes selecting Controls even easier:</p>
<pre lang="java5">public class Screen {<br />
    ...<br />
    public < T extends Controller > T findControl(<br />
        final String elementName,<br />
        final Class < T > requestedControlClass) ...<&#47;pre><br />
Looks complicated but it is really easy to use. To get the Drop Down Control instance from a Screen we simply use the following line:</p>
<pre lang="java5">DropDownControl dropDown1 = screen.findControl("dropDown1", DropDownControl.class);<&#47;pre><br />
And now that we have the instance we can use it to add items to the Drop Down Control:</p>
<pre lang="java5">dropDown1.addItem("Nifty GUI");<br />
dropDown1.addItem("Slick2d");<br />
dropDown1.addItem("Lwjgl");<&#47;pre><br />
<strong>Set the Selected Item<&#47;strong></p>
<p>To select a value from the Drop Down Control we can use the setSelectedItem() or setSelectedItemIdx() methods:</p>
<pre lang="java5">public void setSelectedItemIdx(final int idx)<br />
public void setSelectedItem(final String text)<&#47;pre><br />
The first allows you to select the Item with its index and the second can be used to select the text items directly.</p>
<p>So we use:</p>
<pre lang="java5">dropDown1.setSelectedItemIdx(2);<br />
  or<br />
dropDown1.setSelectedItem("Lwjgl");<&#47;pre><br />
to select the last Item.</p>
<p><strong>Get the Selected Item<&#47;strong></p>
<p>Getting the selected Item works - you guessed it - the same. You can use one of the following methods:</p>
<pre lang="java5">public int getSelectedItemIdx()<br />
public String getSelectedItem()<&#47;pre><br />
So you can get the index of the selected item or the String directly:</p>
<pre lang="java5">assertEquals(2, dropDown1.getSelectedItemIdx());<br />
  or<br />
assertEquals("Lwjgl", dropDown1.getSelectedItem());<&#47;pre><br />
That's it! Nifty!</p>
<p>Have Fun! :D</p>
