---
layout: post
status: publish
published: true
title: Nifty 1.3 has been released
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 287
wordpress_url: http://nifty-gui.lessvoid.com/?p=287
date: '2011-06-26 23:24:26 +0200'
date_gmt: '2011-06-26 22:24:26 +0200'
categories:
- Uncategorized
tags: []
comments:
- id: 928
  author: Erlend Sogge Heggen
  author_email: e.soghe@gmail.com
  author_url: http://erlendheggen.net
  date: '2011-06-28 18:15:53 +0200'
  date_gmt: '2011-06-28 17:15:53 +0200'
  content: "Tried the demo, loving the chat and drag&amp;drop test, don't believe
    those were there before! =P (you didn't mention what in particular was new in
    the demo).\r\n\r\nJust got back \"home\" in Canada from a quick trip to Norway.
    Once I get my head screwed on straight I'll put an announcement together to point
    to this post."
---
<p>Nifty 1.3 is done! Finally! ;)</p>
<p><strong>Get it here:<&#47;strong>
<ul>
<li><a href="https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.3&#47;">Nifty 1.3 Download Folder at sf.net<&#47;a><&#47;li>
<li><a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;projects&#47;1.3&#47;">Nifty 1.3 Maven Projects Page (You can browse the JavaDoc online here)<&#47;a><&#47;li><br />
<&#47;ul></p>
<p>When you're using Maven, you can simply add our sf.net Nifty Maven Repo to your pom.xml:</p>
<pre class="brush:xml">  <repositories><br />
    <repository><br />
      <id>nifty-maven-repo.sourceforge.net<&#47;id><br />
      <url>http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo<&#47;url><br />
    <&#47;repository><br />
  <&#47;repositories><&#47;pre></p>
<p>and then you can add this dependency:</p>
<pre class="brush:xml">    <dependency><br />
      <groupId>lessvoid<&#47;groupId><br />
      <artifactId>nifty<&#47;artifactId><br />
      <version>1.3<&#47;version><br />
    <&#47;dependency><&#47;pre></p>
<p>Here are some quick informations about 1.3.</p>
<p><strong>Lots of Changes<&#47;strong></p>
<p>There have been well <a href="http:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;files&#47;nifty-gui&#47;1.3&#47;nifty-1.3-changelog.txt&#47;download">over 200 changes<&#47;a>! Nifty 1.3 fixes a lot of bugs and adds a lot of new features.</p>
<p>To summarize Nifty 1.3 you could say this is the "controls" release of Nifty. Nifty 1.3 was mainly targeted on improving and extending the nifty-default-controls project. The existing controls have been revised&#47;rewritten and lots of new controls have been added. You can find an overview and details about the <a href="http:&#47;&#47;sourceforge.net&#47;apps&#47;mediawiki&#47;nifty-gui&#47;index.php?title=Nifty_Standard_Controls_(Nifty_1.3)">new controls in the Nifty wiki<&#47;a>.</p>
<p>If you've used controls with Nifty 1.2 then you'll need to update your projects since the 1.3 controls are not backward compatible with the old ones :&#47; Well, the XML part is still working the same but interacting with the controls from Java has changed.</p>
<p>Besides this most of the Nifty core elements (Panel, Image, Text) should work as before. The only difference is that the label element has now been removed and replaced by a label control. So when you've used labels before you need to change them as well. This was described before in <a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;241">a former blog post<&#47;a> too.</p>
<p><strong>JavaBuilder<&#47;strong></p>
<p>Another new feature is the Java Builder pattern that you can use to create GUIs from Java without any XML at all. This feature was <a href="http:&#47;&#47;nifty-gui.lessvoid.com&#47;archives&#47;179">described in a former blog post<&#47;a> and has now been extended to work with all of the new controls and with the build-in core elements alike.</p>
<p><strong>And a new Demo of course<&#47;strong></p>
<p>And here is the <a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-default-controls-examples-1.3.jnlp">Nifty 1.3 controls demo<&#47;a>:</p>
<p><a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;webstart&#47;nifty-default-controls-examples-1.3.jnlp"><img src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;wp-content&#47;2011&#47;06&#47;Bildschirmfoto-2011-06-26-um-22.56.39-1024x806.png" alt="" title="Nifty 1.3 Controls Demo" width="512" height="403" class="aligncenter size-large wp-image-289" &#47;><&#47;a></p>
<p>If you're unable to run the demo above, you can watch the video instead:</p>
<p><iframe src="http:&#47;&#47;player.vimeo.com&#47;video&#47;25637085?color=FF7700" width="640" height="480" frameborder="0"><&#47;iframe>
<p><a href="http:&#47;&#47;vimeo.com&#47;25637085">Nifty 1.3 Controls Example&#47;Demonstration<&#47;a> from <a href="http:&#47;&#47;vimeo.com&#47;user1070526">void<&#47;a> on <a href="http:&#47;&#47;vimeo.com">Vimeo<&#47;a>.<&#47;p></p>
<p>More in future blogs posts...<br />
void</p>
<p>PS: Nifty 1.3 will put the support for the nifty-style-grey on hold for some time. This means that this alternative style is currently not updated to be compatible with 1.3. This is just because of lack of time. We might eventually come back to that style later or if we find a maintainer for that style. Interessted? Contact me! :)</p>
