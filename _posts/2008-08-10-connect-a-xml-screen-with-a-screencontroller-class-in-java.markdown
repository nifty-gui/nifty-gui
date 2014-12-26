---
layout: post
status: publish
published: true
title: Connect a xml screen with a ScreenController class in java
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 20
wordpress_url: http://nifty-gui.lessvoid.com/archives/20
date: '2008-08-10 10:07:42 +0200'
date_gmt: '2008-08-10 09:07:42 +0200'
categories:
- design
tags: []
comments: []
---
<p>Nifty GUI uses XML to store the layout of your GUI. To connect such a XML description with some class in Java there is the ScreenController Interface you need to provide:</p>
<pre lang="java5">&#47;**<br />
&nbsp;* ScreenController Interface all screen controllers should support.<br />
&nbsp;* @author void<br />
&nbsp;*&#47;<br />
public interface ScreenController {<br />
  &#47;**<br />
&nbsp;  * Bind this ScreenController to a screen. This happens<br />
&nbsp;  * when the Screen got the onStartScreen() method.<br />
&nbsp;  * @param nifty nifty<br />
&nbsp;  * @param screen screen<br />
&nbsp;  *&#47;<br />
&nbsp; void bind(Nifty nifty, Screen screen);</p>
<p>  &#47;**<br />
&nbsp;  * called when all start effects are ended and the screen<br />
&nbsp;  * is ready for interactive manipulation.<br />
&nbsp;  *&#47;<br />
&nbsp; void onStartScreen();</p>
<p>  &#47;**<br />
&nbsp;  * called when the onEndScreen effects ended and this screen is done.<br />
&nbsp;  *&#47;<br />
&nbsp; void onEndScreen();<br />
}<&#47;pre></p>
<p>To let Nifty know what ScreenController class you want to use for a Nifty Screen there is the "controller" attribute on the <screen> xml tag:</p>
<pre lang="xml">...<br />
<nifty><br />
  <screen id="start" controller="com.mypackage.HelloWorldStartScreen"><br />
  ...<br />
  <&#47;screen><br />
<&#47;nifty><&#47;pre></p>
<p>To resolve the concrete ScreenController instance Nifty can use two different ways:</p>
<ol>
<li>Nifty creates a new instance of the given ScreenController class and registers this instance with the Screen<&#47;li>
<li>You can give Nifty a ScreenController instance that matches the class given in the controller attribute. Nifty will first look for an existing instance and creates a new class only when it can't find one.<&#47;li><br />
<&#47;ol></p>
<p>To register ScreenController instances with Nifty there are additional parameters on the "fromXml()" method of the Nifty class. This way you can even add multiple different instances for use in multiple Nifty screens.</p>
<pre lang="java5">  &#47;**<br />
   * load xml.<br />
   * @param filename file to load<br />
   * @param controllers controllers to use<br />
   *&#47;<br />
  public void fromXml(final String filename, final ScreenController ... controllers) {<br />
...<&#47;pre></p>
<p><strong>Note: <&#47;strong>You still need the controller attribute in the xml so that Nifty can connect the screen with your ScreenController instance.</p>
<p><strong>Note: <&#47;strong>In case you use anonymous inner classes like in this example here:</p>
<pre lang="java5">class MyStuff {<br />
...<br />
nifty.fromXml("menu.xml", new ScreenController() {<br />
    public void bind(Nifty nifty, Screen screen) {<br />
...<&#47;pre></p>
<p>the classname then turns to "MyStuff$1".</p>
