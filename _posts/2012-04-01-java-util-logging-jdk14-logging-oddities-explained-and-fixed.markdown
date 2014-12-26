---
layout: post
status: publish
published: true
title: java.util.logging (jdk14 logging) oddities explained (and fixed)
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 436
wordpress_url: http://nifty-gui.lessvoid.com/?p=436
date: '2012-04-01 18:37:15 +0200'
date_gmt: '2012-04-01 17:37:15 +0200'
categories:
- bubble
- design
tags: []
comments:
- id: 1242
  author: Tony Ivanov
  author_email: telamohn@gmail.com
  author_url: http://www.tonyivanov.se
  date: '2012-04-02 14:34:54 +0200'
  date_gmt: '2012-04-02 13:34:54 +0200'
  content: "Good job solving that one! :D\r\nI ended up googling the same problem
    when I first got into nifty.\r\nThat means I should be able to remove that infamous
    logger squelch workaround,\r\nThanks!"
- id: 1251
  author: Dom
  author_email: do.werner@gmail.com
  author_url: http://twitter.com/pinguwien
  date: '2012-04-16 06:54:43 +0200'
  date_gmt: '2012-04-16 05:54:43 +0200'
  content: "Hi,\r\nFirst of all: SorryI'm posting here, it's a little offtopic, but
    I haven't found another way to communicate with you ;)\r\n\r\nI've been using
    niftygui on a project for my university (jme3 game) and I'd loved it. You do an
    absolutely great job here, dude! \r\n\r\nNow I thought about a little Java-App
    I want to create. No Game, so I think using jme3 would be a little \"over the
    top\". But I want to use NiftyGUI for my Java-App, so my question is: Is itpossible
    to use NiftyGUI for a \"normal\" Java-App? And if yes, how? (Or is there anything
    I should know which makes Swing or anything else a better choice for my approach
    ;) ) It would be great if you could answer this question for me.\r\n\r\nGreetings
    and keep up your awesome work,\r\nDominik"
- id: 1256
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2012-04-16 21:53:00 +0200'
  date_gmt: '2012-04-16 20:53:00 +0200'
  content: That greatly depends on what exactly you need to do :) Nifty has a Java2d
    renderer which could be used when you don't want or can't use OpenGL. I'm not
    sure in what state the Java2d renderer currently is though (I've not written it
    myself) but it might be worth to check it out. I'd recommend to use the native
    LWJGL with Nifty since that's what I'm using :) but this would mean you need to
    go the whole OpenGL Display creation way. You can use OpenGL&#47;LWJGL in a "normal"
    Java-App but again this depends on what context you're in. Nifty is not suited
    for every possible GUI :) Maybe take a look at JavaFX 2.0?
- id: 1282
  author: jmaasing
  author_email: jmaasing@gmail.com
  author_url: ''
  date: '2012-06-07 19:11:24 +0200'
  date_gmt: '2012-06-07 18:11:24 +0200'
  content: You could of course alos have named your special loggers something like
    Logger.getLogger("de.lessvoid.logger.SpecialLog") and not have to tie them to
    a class name but still be able to shut off all loggers but I guess you wanted
    to keep the same pattern with getClass().getName() everywhere.
- id: 1283
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2012-06-07 20:44:20 +0200'
  date_gmt: '2012-06-07 19:44:20 +0200'
  content: Well, that would work too but the main oddity here is that in the actual
    logfile you'd still see the class (!) that called the jdk14 logger (independenly
    of the logger name). So you'd still have the problem that if a class "some.package.A"
    logs using &ldquo;de.lessvoid.logger.SpecialLog&rdquo; you would still see "some.package.A"
    in the log. And you wouldn't be able to turn that log off using "some.package"
    ;-)
- id: 1285
  author: 3xp0n3nt
  author_email: knightofiam@gmail.com
  author_url: ''
  date: '2012-06-12 07:22:41 +0200'
  date_gmt: '2012-06-12 06:22:41 +0200'
  content: I really appreciate this fix, keep up the great work.
- id: 1318
  author: jmaasing
  author_email: jmaasing@gmail.com
  author_url: ''
  date: '2012-07-13 23:49:18 +0200'
  date_gmt: '2012-07-13 22:49:18 +0200'
  content: "Entering the realm of speculation here since I haven't actually tested
    this but I think the entries in the log file are determined by the SimpleFormatter
    and you might be able to turn the class name off if you set up the formatter for
    the special logs. Something like:\r\njava.util.logging.SimpleFormatter.format=\"%4$s:
    %3$s - %5$s \"\r\n\r\nhttp:&#47;&#47;docs.oracle.com&#47;javase&#47;7&#47;docs&#47;api&#47;java&#47;util&#47;logging&#47;SimpleFormatter.html\r\n\r\nBut
    as I said, idle speculation only :-)"
- id: 1319
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2012-07-14 01:19:12 +0200'
  date_gmt: '2012-07-14 00:19:12 +0200'
  content: Thanks for this tip! I think this really could work :) On the other hand
    the special logs are not really that important I think. I'm considering sl4fj
    as the future logging framework for Nifty which will only add 26 KB which should
    be ok and would be more flexible. What do you think? =)
- id: 1320
  author: hash
  author_email: hash@exultant.us
  author_url: http://exultant.us/
  date: '2012-07-15 02:18:14 +0200'
  date_gmt: '2012-07-15 01:18:14 +0200'
  content: Switching to slf4j would make me pretty happy, if you're interested in
    a random two cents.  Right now I deal with nifty's logging via the jul-over-slf4j
    bridge.
- id: 1321
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2012-07-15 12:52:33 +0200'
  date_gmt: '2012-07-15 11:52:33 +0200'
  content: I am interested! :) Appreciated! Thank you! Probably not in 1.3.x but really
    considering this for 1.4 or 2.x :)
- id: 1332
  author: jmaasing
  author_email: jmaasing@gmail.com
  author_url: ''
  date: '2012-07-21 18:35:17 +0200'
  date_gmt: '2012-07-21 17:35:17 +0200'
  content: slf4j or logback (http:&#47;&#47;logback.qos.ch&#47;). JDK logging is nice
    since it means no extra dependecies but in general it is the weakest (least flexible)
    of the log frameworks. So +1 for switching to anything :-)
- id: 1333
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2012-07-22 00:22:56 +0200'
  date_gmt: '2012-07-21 23:22:56 +0200'
  content: hehe .. full ack!
- id: 1344
  author: Julien
  author_email: gouessej@orange.fr
  author_url: http://gouessej.wordpress.com
  date: '2012-07-30 15:18:10 +0200'
  date_gmt: '2012-07-30 14:18:10 +0200'
  content: The standard logging API is enough for your needs, isn't it? I don't see
    the interest of adding another dependency (sl4fj or logback), really. Developers
    are free to use bridges if they are not satisfied by your choice but if you use
    a third party library in Nifty GUI, we will all be forced to use it. Please don't
    do that.
- id: 1345
  author: toolforger
  author_email: toolforger@durchholz.org
  author_url: ''
  date: '2012-08-01 08:09:32 +0200'
  date_gmt: '2012-08-01 07:09:32 +0200'
  content: "You use logback in the nifty libs and tell application programmers to
    install log4j.\r\nPros and Cons:\r\n+ log4j won't do any message formatting unless
    the message is going to be logged (j.u.l. can't do that), so you can riddle the
    code with logging calls with marginal overhead. Heavy j.u.l. logging will either
    slow down the code, or require checking the log level before each call.\r\n- log4j+logback
    is far more complicated to set up.\r\n+ log4j allows you to combine j.u.l., commons
    logging, and logback in one application and control them from a single configuration
    file. I.e. as soon as another library is added, log4j may be required anyway."
- id: 1346
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2012-08-01 22:56:48 +0200'
  date_gmt: '2012-08-01 21:56:48 +0200'
  content: |-
    Great points but we're talking slf4j which would allow you - the Nifty user - to decide the actual logging framework. When you're using log4j already in your code Nifty will use log4j. If you're using java.util.logging then yeah, that would be fine for Nifty as well. As long as a slf4j adapter exists for the logging framework of your choice (and I bet one exists!) Nifty would happily use it :)

    That is still an appealing idea to me although I tend to agree with Julien that simply sticking with vanilla java.util.logging would work for Nifty as well.

    Any more opinions?
- id: 1416
  author: Nico
  author_email: elhansa@gmail.com
  author_url: ''
  date: '2012-10-02 04:46:10 +0200'
  date_gmt: '2012-10-02 03:46:10 +0200'
  content: "Nifty is really great and the new version (new for me, cause I was using
    1.2 ;) is really wonderful!\r\n\r\nBy the way, what do you think about a mailing
    list for users of nifty? I think it could help. Or there is one already available?\r\n\r\nRegards
    and thanks for nifty!"
- id: 1417
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2012-10-03 13:59:53 +0200'
  date_gmt: '2012-10-03 12:59:53 +0200'
  content: "Thanks! Actually there was a mailing-list for Nifty a while ago but it
    somehow didn't survive the latest sf.net update :) On the other hand it was not
    been used a lot. But with enough people interessted in a mailing-list we can bring
    that back. I'm not sure if it is really necessary since you now have other ways
    to get help with Nifty or get in contact:\n\n\n        Github: https:&#47;&#47;github.com&#47;void256&#47;nifty-gui\n\tTwitter:
    https:&#47;&#47;twitter.com&#47;niftygui\n\tJMonkeyEngine Forum for Nifty: http:&#47;&#47;jmonkeyengine.org&#47;groups&#47;gui&#47;forum&#47;\n"
---
<p>Usually when you add logging to your application you create a java.util.logging.Logger that has the same name as the class you use the Logger in. Your code might look like this:</p>
<pre class="brush:java">package some.test;</p>
<p>import java.util.logging.Logger;</p>
<p>public class Main {<br />
  private static Logger log = Logger.getLogger(Main.class.getName());</p>
<p>  public static void main(final String[] args) {<br />
    log.info("test");<br />
  }<br />
}<br />
<&#47;pre></p>
<p>This works well and you get something like this as the log output:</p>
<pre class="brush:java">01.04.2012 19:10:39 some.test.Main main<br />
INFO: test<br />
<&#47;pre></p>
<p>Now we can easily change the configuration of this logger and change the Loglevel. So for instance when we don't like any logging we can disable logging for this class either using a configuration file or do it directly from code like so:</p>
<pre class="brush:java">
Logger.getLogger("some.test").setLevel(Level.OFF);<&#47;pre></p>
<p>and the class will not log anymore.</p>
<p>Sometimes doing this in Nifty and using the name "de.lessvoid.nifty" for instance to shut off the logging refused to work. Some classes simply didn't stop logging at all. What's going on?</p>
<p>After a long headache we've finally found out!</p>
<p>Nifty used some special logger names for eventbus and inputevent logging. Both loggers used special names that did not relate to any class because there where several classes that would need to log those events. So a special name, like "NiftyEventBusLog" made sense for me.</p>
<p>In some places we had code like that:</p>
<pre class="brush:java">package some.test;</p>
<p>import java.util.logging.Logger;</p>
<p>public class Main {<br />
  private static Logger differentLog = Logger.getLogger("SpecialLog");</p>
<p>  public static void main(final String[] args) {<br />
    differentLog.info("test");<br />
  }<br />
}<br />
<&#47;pre></p>
<p>I somehow expected the loggername in the log to be "SpecialLog" since it's the name of the logger. But in fact we get something else:</p>
<pre class="brush:java">
01.04.2012 19:24:41 some.test.Main main<br />
INFO: test<br />
<&#47;pre></p>
<p>O_o</p>
<p>The information still shows "some.test.Main" since this is the class that actually logged!</p>
<p>If you now try to disable logging for this class, like we've seen above:</p>
<pre class="brush:java">
Logger.getLogger("some.test").setLevel(Level.OFF);<&#47;pre></p>
<p>YOU WOULD STILL SEE THE LINE IN THE LOG - even though you've disabled it (kinda) :-)</p>
<p>Of course to fix this you would need to disable the "SpecialLog" additionaly to "some.test.Main" but that's pretty odd since you usually don't know the exact names of all loggers beforehand.</p>
<p>So to make a long story short Nifty now (current git) removed all the special loggers and always only uses the logger with the name of the current class. When you now disable a logger you should be pretty sure that you really disable any output with that name ;-)</p>
<p>void</p>
<p><strong>EDIT<&#47;strong></p>
<p>I just realized that it would be very helpful to give you the actual logger names you need to disable when you still use Nifty 1.3.1:</p>
<ul>
<li>"NiftyInputEventHandlingLog"<&#47;li>
<li>"NiftyEventBusLog"<&#47;li>
<li>"NiftyImageManager"<&#47;li><br />
<&#47;ul></p>
