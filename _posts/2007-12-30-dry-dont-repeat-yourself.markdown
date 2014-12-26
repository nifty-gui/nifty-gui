---
layout: post
status: publish
published: true
title: DRY - Don't Repeat Yourself
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: " Check the following code fragment:\r\n<pre lang=\"java5\">    &#47;&#47;
  get post mode and default to post = true, when nothing is given\r\n    boolean post
  = true;\r\n    if (effectType.isSetPost()) {\r\n      if (PostType.FALSE == effectType.getPost())
  {\r\n        post = false;\r\n      } else if (PostType.TRUE == effectType.getPost())
  {\r\n        post = true;\r\n      }\r\n    }<&#47;pre>\r\nAlthough there's no code
  duplicated in there (the root of all software evil), there is still some information
  duplicated. The comment repeats the same information as the code! And duplicate
  information is not good. If I want to change the default value I need to change
  two lines: the actual code and the comment. And maybe I forget to change one part
  and voila introduced inconsistence. Which comes back to me someday and is going
  to bite me.\r\n\r\nSo what to do, to make it still readable and easily changeable?\r\n\r\n"
wordpress_id: 7
wordpress_url: http://nifty-gui.lessvoid.com/archives/7
date: '2007-12-30 17:45:53 +0100'
date_gmt: '2007-12-30 16:45:53 +0100'
categories:
- design
tags: []
comments: []
---
<p> Check the following code fragment:</p>
<pre lang="java5">    &#47;&#47; get post mode and default to post = true, when nothing is given<br />
    boolean post = true;<br />
    if (effectType.isSetPost()) {<br />
      if (PostType.FALSE == effectType.getPost()) {<br />
        post = false;<br />
      } else if (PostType.TRUE == effectType.getPost()) {<br />
        post = true;<br />
      }<br />
    }<&#47;pre><br />
Although there's no code duplicated in there (the root of all software evil), there is still some information duplicated. The comment repeats the same information as the code! And duplicate information is not good. If I want to change the default value I need to change two lines: the actual code and the comment. And maybe I forget to change one part and voila introduced inconsistence. Which comes back to me someday and is going to bite me.</p>
<p>So what to do, to make it still readable and easily changeable?</p>
<p><a id="more"></a><a id="more-7"></a></p>
<p>Just select "true"  and in eclipse press "right mouse button" &#47; refactor &#47; extract constant. Enter a name and press Return to get something like this:</p>
<pre lang="java5">  &#47;**<br />
   * default value for post&#47;pre effect state.<br />
   *&#47;<br />
  private static final boolean DEFAULT_EFFECT_POST = true;</p>
<p>  &#47;&#47; get post mode<br />
  boolean post = DEFAULT_EFFECT_POST;<br />
  if (effectType.isSetPost()) {<br />
  ...<&#47;pre><br />
<strong>Wow! Clean, Readable, Changeable!<&#47;strong></p>
<p>Good Code :)</p>
