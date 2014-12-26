---
layout: post
status: publish
published: true
title: Correctly configure jdk14-logging (guest blog post by Ben)
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 532
wordpress_url: http://nifty-gui.lessvoid.com/?p=532
date: '2013-03-16 01:33:28 +0100'
date_gmt: '2013-03-16 00:33:28 +0100'
categories:
- Uncategorized
tags: []
comments:
- id: 1726
  author: Dionysius
  author_email: dragon.dionysius@gmail.com
  author_url: ''
  date: '2013-04-30 17:32:32 +0200'
  date_gmt: '2013-04-30 16:32:32 +0200'
  content: "In my case I want to reduce nifty logging output, so I made:\r\n\r\n\tprivate
    static final java.util.logging.Logger LOGGERNIFTY = java.util.logging.Logger.getLogger(\"de.lessvoid.nifty\");\r\n\r\nin
    combination with\r\n\r\n\tLOGGERNIFTY.setLevel(java.util.logging.Level.WARNING);\r\n\r\nBut
    I always get when switching screens:\r\n\r\nApr 30, 2013 6:31:08 PM de.lessvoid.nifty.screen.Screen$EndScreenEndNotify
    perform\r\nINFO: onEndScreen has ended"
- id: 2334
  author: Homsi
  author_email: insanomania911@hotmail.com
  author_url: ''
  date: '2013-10-02 20:45:04 +0200'
  date_gmt: '2013-10-02 19:45:04 +0200'
  content: Same thing as above comment.  I am trying to get rid of the onEndScreen
    window!!!
- id: 2335
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-10-03 23:18:39 +0200'
  date_gmt: '2013-10-03 22:18:39 +0200'
  content: |-
    The log level of the quoted log output was changed 8 months ago by this commit https:&#47;&#47;github.com&#47;void256&#47;nifty-gui&#47;commit&#47;cf06fc203fe5d7c33b6464becad61ac8e8f5fca3 from INFO to FINE which should make it disappear with the default configuration of the jdk14 logger IF you use Nifty 1.3.3 or Nifty 1.4.x.

    If you use an older version of Nifty you need to follow the EXACT steps that this blog post described to shut the logging off.
---
<p>After being puzzled for a while why he wasn't able to configure logging in jME-Nifty properly Ben ("ben dot foxmoore at gmail dot com") set aside some time to figure it out. He was kind enough to share his findings with us in this guest blog post :)</p>
<p>So here is Ben explaining a proper way to configure Nifty logging:</p>
<p>It seems that most people who have issues with Nifty's logs want to cut down on the number shown, but I, on the other hand, wanted to see more. Unfortunately, setting the global logging level to Info caused JME3 to also log lots of information that wasn't relevant to the problem at hand. I attempted a quick fix using the following code:</p>
<pre class="brush:java">public static void main(String[] args) {<br />
        Logger.getLogger("").setLevel(Level.WARNING);<br />
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.INFO);<br />
        SimpleApplication app = new SimpleApplication() {<br />
            public void simpleInitApp() {<br />
                NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(<br />
                     assetManager, inputManager, audioRenderer, guiViewPort);<br />
                Nifty nifty = niftyDisplay.getNifty();<br />
                nifty.loadStyleFile("nifty-default-styles.xml");<br />
                nifty.loadControlFile("nifty-default-controls.xml");<br />
                guiViewPort.addProcessor(niftyDisplay);<br />
            }<br />
        };<br />
        app.start();<br />
    }<&#47;pre></p>
<p>However, you'll quickly realise that this doesn't actually cause Nifty to display any extra logs. After many hours trying to work out why, I discovered that it's all down to an intricacy in the way LogManager keeps track of the Loggers in your program. LogManager only keeps a weakReference to each Logger, allowing any that are no longer being used to be garbage collected. Because of this, in the time between line 3 and line 6, the JRE garbage collects the original "de.lessvoid.nifty" Logger created at line 3. When the "de.lessvoid.nifty.Nifty" Logger is retrieved in the Nifty constructor (called by the NiftyJmeDisplay constructor at line 6), a new one is created and it inherits its Level from the rootLogger, which is set to Warning at line 2. (The LogManager keeps a strong reference to the rootLogger so it is never garbage collected.)</p>
<p>A quick (but not very nifty) solution to this problem is to just keep a static (strong) reference to the "de.lessvoid.nifty" Logger in your class, as such:</p>
<pre class="brush:java">    private static Logger logger;</p>
<p>    public static void main(String[] args) {<br />
        Logger.getLogger("").setLevel(Level.WARNING);<br />
        logger = Logger.getLogger("de.lessvoid.nifty");<br />
        logger.setLevel(Level.INFO);<br />
        SimpleApplication app = new SimpleApplication() {<br />
            public void simpleInitApp() {<br />
                ...<br />
            }<br />
        };<br />
        app.start();<br />
    }<&#47;pre></p>
<p>Ultimately though, the niftiest solution to this problem is to use a custom properties file which specifies to the LogManager how to initialize all of the Loggers. The following "logging.properties" file will initialize the Logging system in the same way as the code above:</p>
<pre class="brush:java">handlers=java.util.logging.ConsoleHandler<br />
.level=WARNING<br />
de.lessvoid.nifty.level=INFO<&#47;pre></p>
<p>There are two ways to tell the LogManager to use this file. Either use the</p>
<pre class="brush:java">"-Djava.util.logging.config.file=pathToLogging.properties"<&#47;pre></p>
<p>flag when running your program, or, alternatively, use the following code:</p>
<pre class="brush:java">    public static void main(String[] args) throws Exception {<br />
        InputStream inputStream =<br />
             ClassLoader.getSystemResourceAsStream("logging.properties");<br />
        LogManager.getLogManager().readConfiguration(inputStream);<br />
        SimpleApplication app = new SimpleApplication() {<br />
            public void simpleInitApp() {<br />
                ...<br />
            }<br />
        };<br />
        app.start();<br />
    }<&#47;pre></p>
<p>Hopefully this should help anyone who comes across the same issue that I did!</p>
<p>(As a small side note, there is one final alternative: LogManager can also use the "java.util.logging.config.class" property. If present, the given class will be loaded, an object will be instantiated, and that object's constructor is responsible for providing the initial configuration as an InputStream, just as above.)</p>
<p>So that's it! Finally you can configure logging correctly! THANKS A LOT BEN! :D</p>
<p>void</p>
<p>PS: Nifty 1.3.3 and Nifty 1.4 have already been configured to log less by default. So most people should now be more happy with the new defaults. But if you really need the logging to debug an issue you now can do it properly - thanks to Ben :)</p>
