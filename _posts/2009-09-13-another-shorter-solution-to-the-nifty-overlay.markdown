---
layout: post
status: publish
published: true
title: Another shorter solution to the Nifty Overlay
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "Nifty and Slick User Tobse pointed out that you can use the NiftyGameState
  to create overlays too.\r\n\r\nI like his version because it is shorter and much
  of the details of how the integration with Nifty works (calling render and forwarding
  input events to Nifty) are hidden from you. However the class NiftyGameState has
  been designed as a base class for a real Nifty GUI State and was not really planned
  for overlay rendering.\r\n\r\nSo I think although both versions work there is still
  something missing (in both versions). I like the idea of hidding the details on
  one hand but I'm not sure if using the NiftyGameState is really the way to achieve
  this.\r\n\r\nWhat do you think?\r\n\r\nClick the \"Read More\" link to see the short
  version of TestState that Tobse wrote and which is less code than my initial version
  :)\r\n\r\n"
wordpress_id: 96
wordpress_url: http://nifty-gui.lessvoid.com/?p=96
date: '2009-09-13 22:33:48 +0200'
date_gmt: '2009-09-13 21:33:48 +0200'
categories:
- Uncategorized
tags: []
comments:
- id: 339
  author: Murilo
  author_email: murilovmachado@gmail.com
  author_url: ''
  date: '2009-09-14 18:36:50 +0200'
  date_gmt: '2009-09-14 17:36:50 +0200'
  content: Thank both of you for the alternative solution!
- id: 340
  author: Shiring
  author_email: wthiel@shiring.de
  author_url: http://blog.shiring.de
  date: '2009-09-17 22:54:32 +0200'
  date_gmt: '2009-09-17 21:54:32 +0200'
  content: "I use something like that in my project:\r\n\r\npublic class Test extends
    NiftyGameState implements ScreenController {\r\n\r\n\tprivate GameContainer container;\r\n\tprivate
    Font font;\r\n\tprivate Color currentColor;\r\n\r\n\tpublic Test() {\r\n\r\n\t\tsuper(1);\r\n\t}\r\n
    \r\n\tpublic void init(GameContainer container, StateBasedGame game) throws SlickException
    {\r\n\t\t\r\n\t\tthis.init(container, game);\r\n\t\tthis.fromXml(\"tests&#47;overlay.xml\",
    this);\r\n\t\tthis.container = container;\r\n\t\tfont = new AngelCodeFont(\"menu.fnt\",
    \"menu.png\");\r\n\t\tcurrentColor = Color.white;\r\n\t}\r\n \r\n\tpublic void
    render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException
    {\r\n\t\tg.setFont(font);\r\n\t\tg.setColor(currentColor);\r\n\t\tg.drawString(\"State
    Based Game Test\", 100, 100);\r\n\t\tg.drawString(\"1-3 will switch between colors\",
    100, 300);\r\n\t\tg.drawString(\"(this is all slick rendering!)\", 100, 400);\r\n\t\tg.drawString(\"and
    this is more slick text\", 360, 650);\r\n\t\tg.drawString(\"below (!) a nifty-gui
    overlay\", 360, 700);\r\n \r\n\t\tsuper.render(container, game, g);\r\n\t}\r\n
    \r\n\tpublic void update(GameContainer container, StateBasedGame game, int delta)
    throws SlickException {\r\n\t\t\r\n\t\tsuper.update(container, game, delta);\r\n
    \   }\r\n \r\n\t@Override\r\n\tpublic void keyReleased(int key, char c) {\r\n\t\tif
    (key == Input.KEY_1) {\r\n\t\t\tcurrentColor = Color.red;\r\n\t\t\tgetElement(\"red\").startEffect(EffectEventId.onCustom);\r\n\t\t}\r\n\t\tif
    (key == Input.KEY_2) {\r\n\t\t\tcurrentColor = Color.green;\r\n\t\t\tgetElement(\"green\").startEffect(EffectEventId.onCustom);\r\n\t\t}\r\n\t\tif
    (key == Input.KEY_3) {\r\n\t\t\tcurrentColor = Color.blue;\r\n\t\t\tgetElement(\"blue\").startEffect(EffectEventId.onCustom);\r\n\t\t}\r\n\t}\r\n
    \r\n\tprivate Element getElement(final String id) {\r\n\t\treturn nifty.getCurrentScreen().findElementByName(id);\r\n\t}\r\n
    \r\n\tpublic void bind(Nifty nifty, Screen screen) {}\r\n \r\n\tpublic void onEndScreen()
    {}\r\n \r\n\tpublic void onStartScreen() {}\r\n \r\n\tpublic void quit() {\r\n\t\tnifty.getCurrentScreen().endScreen(new
    EndNotify() {\r\n \r\n\t\t\tpublic void perform() {\r\n\t\t\t\tcontainer.exit();\r\n\t\t\t}\r\n\t\t});\r\n\t}\r\n}"
- id: 348
  author: MrKanuster
  author_email: mrkanister@hvl.ch
  author_url: ''
  date: '2010-01-09 20:28:19 +0100'
  date_gmt: '2010-01-09 19:28:19 +0100'
  content: "Hi\r\n\r\nI need Slick with Nifty. My problem is to switch between different
    gamestates. \r\nThis code doesen't work. The nifty gui doesen't work anymore,
    if I switch back.\r\n\r\n\"  public void goBack() {\r\n\t  \r\n\t  nifty.getCurrentScreen().endScreen(new
    EndNotify() {\r\n\t\t  \r\n\t      public void perform() {\r\n\t        game.enterState(2);\r\n\t
    \     }\r\n\t    });\r\n  }\""
---
<p>Nifty and Slick User Tobse pointed out that you can use the NiftyGameState to create overlays too.</p>
<p>I like his version because it is shorter and much of the details of how the integration with Nifty works (calling render and forwarding input events to Nifty) are hidden from you. However the class NiftyGameState has been designed as a base class for a real Nifty GUI State and was not really planned for overlay rendering.</p>
<p>So I think although both versions work there is still something missing (in both versions). I like the idea of hidding the details on one hand but I'm not sure if using the NiftyGameState is really the way to achieve this.</p>
<p>What do you think?</p>
<p>Click the "Read More" link to see the short version of TestState that Tobse wrote and which is less code than my initial version :)</p>
<p><a id="more"></a><a id="more-96"></a></p>
<pre lang="java5">package tests;</p>
<p>import org.newdawn.slick.*;<br />
import org.newdawn.slick.state.*;<br />
import de.lessvoid.nifty.*;<br />
import de.lessvoid.nifty.effects.EffectEventId;<br />
import de.lessvoid.nifty.elements.Element;<br />
import de.lessvoid.nifty.screen.*;<br />
import de.lessvoid.nifty.slick.NiftyGameState;</p>
<p>&#47;**<br />
 * This is the original TestState1 from slick tests extended to a nifty gui overlay. This only<br />
 * implements from ScreenController because we have a quit() onClick action definied in the<br />
 * nifty xml file that is handled in here to quit the demo.<br />
 *<br />
 * @author void<br />
 *&#47;<br />
public class TestState2 extends BasicGameState implements ScreenController {<br />
	public static final int ID = 1;<br />
	private GameContainer container;<br />
	private Font font;<br />
	private Color currentColor;<br />
	private Nifty nifty;<br />
	private NiftyGameState niftyGameState;</p>
<p>	@Override<br />
	public int getID() {<br />
		return ID;<br />
	}</p>
<p>	public void init(GameContainer container, StateBasedGame game) throws SlickException {<br />
		this.container = container;<br />
		font = new AngelCodeFont("menu.fnt", "menu.png");<br />
		currentColor = Color.white;<br />
		niftyGameState = new NiftyGameState(2);<br />
		niftyGameState.fromXml("tests&#47;overlay.xml", this);<br />
		container.getInput().addListener(niftyGameState);<br />
		niftyGameState.init(container, game);<br />
		nifty = niftyGameState.getNifty();<br />
	}</p>
<p>	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {<br />
		g.setFont(font);<br />
		g.setColor(currentColor);<br />
		g.drawString("State Based Game Test", 100, 100);<br />
		g.drawString("1-3 will switch between colors", 100, 300);<br />
		g.drawString("(this is all slick rendering!)", 100, 400);<br />
		g.drawString("and this is more slick text", 360, 650);<br />
		g.drawString("below (!) a nifty-gui overlay", 360, 700);</p>
<p>		niftyGameState.render(container, game, g);<br />
	}</p>
<p>	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {<br />
        }</p>
<p>	@Override<br />
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {<br />
		niftyGameState.enter(container, game);<br />
	}</p>
<p>	@Override<br />
	public void keyReleased(int key, char c) {<br />
		if (key == Input.KEY_1) {<br />
			currentColor = Color.red;<br />
			getElement("red").startEffect(EffectEventId.onCustom);<br />
		}<br />
		if (key == Input.KEY_2) {<br />
			currentColor = Color.green;<br />
			getElement("green").startEffect(EffectEventId.onCustom);<br />
		}<br />
		if (key == Input.KEY_3) {<br />
			currentColor = Color.blue;<br />
			getElement("blue").startEffect(EffectEventId.onCustom);<br />
		}<br />
	}</p>
<p>	private Element getElement(final String id) {<br />
		return nifty.getCurrentScreen().findElementByName(id);<br />
	}</p>
<p>	public void bind(Nifty nifty, Screen screen) {}</p>
<p>	public void onEndScreen() {}</p>
<p>	public void onStartScreen() {}</p>
<p>	public void quit() {<br />
		nifty.getCurrentScreen().endScreen(new EndNotify() {</p>
<p>			public void perform() {<br />
				container.exit();<br />
			}<br />
		});<br />
	}<br />
}<&#47;pre><br />
void</p>
