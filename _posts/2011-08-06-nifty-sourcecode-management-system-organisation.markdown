---
layout: post
status: publish
published: true
title: Nifty Sourcecode Management System Organisation
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 312
wordpress_url: http://nifty-gui.lessvoid.com/?p=312
date: '2011-08-06 14:41:02 +0200'
date_gmt: '2011-08-06 13:41:02 +0200'
categories:
- Uncategorized
tags: []
comments: []
---
<p>As mentioned previously Nifty is now hosted at sf.net using git scm. Nifty SVN is still available but will mainly be used for a bugfix 1.3.1 Release (if severe bugs surface). The 1.3.1-SNAPSHOT source trees of Nifty are still being build automatically with Jenkins and 1.3.1-SNAPSHOT versions are still being deployed to the <a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo&#47;lessvoid&#47;">Maven Repo for Nifty <&#47;a>. Besides an eventual 1.3.1 release we will not use SVN anymore.</p>
<p>Consequently the Nifty projects available in the <a href="http:&#47;&#47;nifty-gui.git.sourceforge.net&#47;git&#47;gitweb-index.cgi">git repo at sf.net<&#47;a> have been updated to 1.4-SNAPSHOT. 1.4 will be build with Jenkins so that you can find nightly builds of 1.4-SNAPSHOT Nifty projects in the <a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty-maven-repo&#47;lessvoid&#47;">Maven Repo for Nifty <&#47;a> as well.</p>
<p>If you had svn write access to the Nifty projects before you will be able to push to git now too.</p>
<p>There is one exception tho: The main development for Nifty happens on the "develop" Branch of the individual projects. Consequently the "develop" branch will be automatically build and deployed by Jenkins (1.4-SNAPSHOT). Pushing to "master" is restricted and is reserved for release candidates. This should allow the development to continue freely on the "develop" branch (or on any other branches as well!).</p>
<p>On another note 1.4 is not scheduled yet in any way and should be considered experimental - at least for the time being. So make sure you wear a safety helmet in case it explodes and stuff :D</p>
<p>void</p>
