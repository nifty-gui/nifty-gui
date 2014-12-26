---
layout: post
status: publish
published: true
title: Removing XmlBeans...
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 14
wordpress_url: http://nifty-gui.lessvoid.com/archives/14
date: '2008-04-20 22:36:01 +0200'
date_gmt: '2008-04-20 21:36:01 +0200'
categories:
- bubble
tags: []
comments: []
---
<p>Actually I like XmlBeans a lot. Define a XML-Schema (XSD) for your XML-File, throw XmlBeans at it and  let it generate some Java classes. Voila. XML-Binding. Validate against the XML-Schema, easily access your XML-File from within Java, etc... XML? Java? Done!</p>
<p>There's even a Maven Plugin for it, so one can generate the classes on the fly within your build process. Great Stuff!</p>
<p>So why would some half insane person want to removed it?</p>
<p>Well, one drawback is the size of the lib. Because XmlBeans is your Swiss army knife of XML-Java-Binding it is rather large being around 2,6 MB in size. Altough this is not that bad in DSL-century you can still notice it when running a Java Applet or Java Webstart.</p>
<p>Another and somewhat profoundly drawback is, that in a Java Webstart it seems to parse Xml-Files very very slow. I'm not sure what exactly causes this slowdown but from what I grasp this might be a classLoader issue or something. When run localy everything works fine but from within a Webstart Version it runs painfully slow. I've tried to pinpoint what causes these issues but had not much luck in doing so.</p>
<p>Now I've settled to get rid of XmlBeans and replace it with some simpler form of xml parsing. What I'm currently using is <a href="http:&#47;&#47;www.extreme.indiana.edu&#47;xgws&#47;xsoap&#47;xpp&#47;">XPP3<&#47;a>. Hopefully this will remove the issues. The lib is very small (25 KB) and very fast. The only drawback so far is that I lose the Schema-Validation. I'll keep the XML-Schema for development and documentation purposes but the actual Loader inside Nifty won't check against the Schema anymore. Which is a bit sad but something I can't do anything about at this very moment.</p>
