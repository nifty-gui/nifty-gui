---
layout: post
status: publish
published: true
title: Localization support and some more cool stuff!
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 153
wordpress_url: http://nifty-gui.lessvoid.com/?p=153
date: '2010-06-19 21:35:57 +0200'
date_gmt: '2010-06-19 20:35:57 +0200'
categories:
- Uncategorized
tags: []
comments:
- id: 426
  author: Jattra
  author_email: jattra@centrum.cz
  author_url: ''
  date: '2010-06-20 19:14:24 +0200'
  date_gmt: '2010-06-20 18:14:24 +0200'
  content: "Good work.\r\nI am going to rework my multi-lang menu to be done in proper
    way. \r\nGrrr. Hurray!"
- id: 443
  author: Tumaini
  author_email: hem@jordefamn.se
  author_url: ''
  date: '2010-07-12 21:07:36 +0200'
  date_gmt: '2010-07-12 20:07:36 +0200'
  content: "Very nice!\r\nI just started converting our game to use Nifty instead
    of GBUI and having done some Android work lately, this was just what I was looking
    for!\r\nThank you very much!"
- id: 538
  author: Loki
  author_email: lokinell@gmail.com
  author_url: ''
  date: '2010-10-18 01:11:47 +0200'
  date_gmt: '2010-10-18 00:11:47 +0200'
  content: Unfortunately, Nifty GUI do not support Chinese.
- id: 539
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2010-10-18 22:38:08 +0200'
  date_gmt: '2010-10-18 21:38:08 +0200'
  content: why? why is this? :) ever tried a chinese font?
- id: 543
  author: loki
  author_email: lokinell@gmail.com
  author_url: ''
  date: '2010-10-20 05:24:40 +0200'
  date_gmt: '2010-10-20 04:24:40 +0200'
  content: "I try to use Chinese in Nifty but there are many problems to show Chinese
    rightly.\r\n\r\nI generate a Chinese font for myself by using BMFont tool"
- id: 546
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2010-10-20 17:33:37 +0200'
  date_gmt: '2010-10-20 16:33:37 +0200'
  content: |-
    Can you post some screenshots or describe the problems you had in the sf.net help forum for nifty: https:&#47;&#47;sourceforge.net&#47;projects&#47;nifty-gui&#47;forums&#47;forum&#47;807893
    I'd like to know the problems you've encountered. Not sure if I could help but I'd like to know the problems at least =)
- id: 547
  author: Loki
  author_email: lokinell@gmail.com
  author_url: ''
  date: '2010-10-20 18:17:55 +0200'
  date_gmt: '2010-10-20 17:17:55 +0200'
  content: "Nifty GUI with Chinese problems\r\n1) When the font of text field is Chinese,
    then you cannot input for the text field.\r\n2) When integrate with jME, cannot
    show Chinese rightly, just can show the words in the first page of the font."
- id: 1544
  author: Hui
  author_email: zhenhui.703@gmail.com
  author_url: ''
  date: '2013-02-22 02:11:10 +0100'
  date_gmt: '2013-02-22 01:11:10 +0100'
  content: "Hi, I also met the problem when I tried to show Chinese in nifty.  And
    I already stuck in this issue for weeks. \r\nSo did you have the solution to the
    Chinese shown rightly? \r\nMany thanks."
- id: 1545
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-02-22 22:23:12 +0100'
  date_gmt: '2013-02-22 21:23:12 +0100'
  content: I didn't receive any example code, I'm afraid. Can you please add an issue
    at github for this? It would be great to have some minimal example code to try
    this on my own. I still thing this is probably related to the managment of the
    font or some error in reading the font file eventually. But without an example
    - I'm not a chinese expert :) - this is hard to know for sure.
---
<p>A <a href="https:&#47;&#47;sourceforge.net&#47;tracker&#47;?func=detail&amp;aid=2907134&amp;group_id=223898&amp;atid=1059825">feature request<&#47;a> made Nifty user <a href="https:&#47;&#47;sourceforge.net&#47;users&#47;lynorics&#47;">lynorics<&#47;a> create a patch that solved the issue and it directly point to solve the <a href="https:&#47;&#47;sourceforge.net&#47;tracker&#47;?func=detail&amp;aid=2948094&amp;group_id=223898&amp;atid=1059825">localization feature request<&#47;a> too :)</p>
<p>Every attribute of every Nifty element can now contain a special markup <strong>"${...}"<&#47;strong> that gets replaced with something else when the xml is loaded.</p>
<p>Example:</p>
<pre class="brush:xml"><text text="your home directory: ${ENV.HOME}" &#47;><&#47;pre><br />
The "${ENV.HOME}" will be replaced with your $HOME environment variable! :D</p>
<p>You have the following options now to use this new feature:</p>
<ul>
<li><strong>${id.key}<&#47;strong> lookup resource bundle with "id" and request "key" from it. This is explained in more detail below.<&#47;li>
<li><strong>${ENV.key}<&#47;strong> lookup "key" in the environment variables (System.getEnv()) and replace ${ENV.key} with the value received.<&#47;li>
<li><strong>${PROP.key}<&#47;strong> lookup "key" in the new Nifty.setGlobalProperties(Properties) properties or if the properties are not set this will use System.getProperties() to lookup "key"<&#47;li>
<li><strong>${CALL.method()}<&#47;strong> call method() at the current ScreenController and it is replaced with the value that method() returns. method() should return a String in this case.<&#47;li><br />
<&#47;ul><br />
If for some reason the replacement does not work out then nothing is replaced and you'll get the original ${...} String back.</p>
<p><strong>Localization Details<&#47;strong></p>
<p>Currently Nifty Localization is using standard Property file based Resourcebundles. This simply means you can create a property file containing keys that are referenced from the xml files.</p>
<p>Example:</p>
<pre>dialog.properties:<br />
hello = Hello World in Default Language</p>
<p>dialog_de.properties:<br />
hello = Hallo Welt in Deutsch</p>
<p>dialog_en.properties:<br />
hello = hello world in english<&#47;pre><br />
Once you have created these files you need to register the resourceBundle "dialog" with Nifty so that Nifty knows that it exists. You can do that with the new "resourceBundle" tag:</p>
<pre class="brush:xml"><resourceBundle id="dialog" filename="src&#47;main&#47;resources&#47;dialog" &#47;><&#47;pre></p>
<pre lang="xml"><&#47;pre></p>
<p>Now that Nifty knows about your resourceBundle you can access it with the method mentioned above:</p>
<pre class="brush:xml"><text text="${dialog.hello}" &#47;><&#47;pre></p>
<pre lang="xml"><&#47;pre><br />
Now Nifty will use the current set default locale to access the ResourceBundle with the id "dialog" and looks up the value for "hello". If you don't like that Nifty uses the default Locale you can set the Locale that Nifty should use with the "nifty.setLocale(Locale)" method.</p>
<p>Nifty stuff and Kudos goes out to lynorics! :D</p>
<p>void</p>
