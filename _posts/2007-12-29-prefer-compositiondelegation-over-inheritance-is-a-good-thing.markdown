---
layout: post
status: publish
published: true
title: Prefer composition&#47;delegation over inheritance is a good thing!
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
excerpt: "I am refactoring Nifty-GUI's internal workings. Trying to make it less inheritance
  dependent.\r\n\r\nTo manage all the Elements (Panels, Images, Text and so on) I
  have quickly and with not much thought introduced a Element class hierarchy as shown
  in figure 1.\r\n\r\n<img src=\"http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;remove-inheritance-before.png\"
  alt=\"figure 1: reasonable (?) class hierachie\" border=\"0\" height=\"128\" hspace=\"2\"
  vspace=\"2\" width=\"141\" &#47;>\r\n\r\n<strong>figure 1: reasonable (?) class
  hierachie <&#47;strong>\r\n\r\nWell, each Element has an \"void abstract render()\"
  method to render itself. This method is overwritten in the subclasses. This is reasonable
  because the steps you'll need to render an Image are probably different to f.i.
  a TextElement. You have seen this kind of solution a million times before. So why
  should this be a bad thing?\r\n\r\n"
wordpress_id: 6
wordpress_url: http://nifty-gui.lessvoid.com/archives/6
date: '2007-12-29 23:31:34 +0100'
date_gmt: '2007-12-29 22:31:34 +0100'
categories:
- design
tags: []
comments: []
---
<p>I am refactoring Nifty-GUI's internal workings. Trying to make it less inheritance dependent.</p>
<p>To manage all the Elements (Panels, Images, Text and so on) I have quickly and with not much thought introduced a Element class hierarchy as shown in figure 1.</p>
<p><img src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;remove-inheritance-before.png" alt="figure 1: reasonable (?) class hierachie" border="0" height="128" hspace="2" vspace="2" width="141" &#47;></p>
<p><strong>figure 1: reasonable (?) class hierachie <&#47;strong></p>
<p>Well, each Element has an "void abstract render()" method to render itself. This method is overwritten in the subclasses. This is reasonable because the steps you'll need to render an Image are probably different to f.i. a TextElement. You have seen this kind of solution a million times before. So why should this be a bad thing?</p>
<p><a id="more"></a><a id="more-6"></a>Alex Miller points in his Blog the drawbacks of the Template Method out (see his very cool entry here: <a href="http:&#47;&#47;tech.puredanger.com&#47;2007&#47;07&#47;03&#47;pattern-hate-template&#47;" title="Alex Miller - Patters I Hate #2: Template Pattern">Alex Miller - Pattern I Hate #2: Template Method<&#47;a>). And although this is not really a Template Pattern in it's purest form it sure has most of the drawbacks as Alex points out, for instance:</p>
<ul>
<li><strong>Difficult to compose functionality:<&#47;strong> Say I want to change more behaviour not only rendering. Maybe layout the element, mouse events, key events and so on. If I'd introduce for every behaviour a new (abstract) method that is supposed to be overwritten in a sub class things get messy and complicated pretty soon.<&#47;li><br />
<&#47;ul><br />
I noticed that the only difference my Elements had until now, was the way they rendered themself on the screen. As I am not really a friend of inheritance at all ;) I ask myself: Why not remove the whole Element hierarchy?</p>
<p>And it is easily possible. I can extract the functionality of "rendering an element" into an Interface. I call this an "ElementRenderer" Interface with only one Method: "render":</p>
<pre>public interface ElementRenderer {<&#47;pre></p>
<pre>  &#47;**<&#47;pre></p>
<pre>   * render the element.<&#47;pre></p>
<pre>   * @param w the Element<&#47;pre></p>
<pre>   * @param r the RenderDevice for output.<&#47;pre></p>
<pre>   *&#47;<&#47;pre></p>
<pre>  void render(Element w, RenderDevice r);<&#47;pre></p>
<pre>}<&#47;pre><br />
Doing it this way, I can remove the whole Element hierarchy. I am left with a single class, the Element class and I can "inject" different behaviour into this class using different "ElementRenderer" implementations. One way to achieve this is "constructor injection" and simply give the Element constructor an additional parameter, the ElementRenderer this Element should use.</p>
<pre> public Element(final ElementRenderer newElementRenderer) {  ... }<&#47;pre><br />
So implementing the ElementRenderer is easy and I don't need to care about the whole Element hierachy anymore. Furthermore I noticed that at the moment some elements can share the same ElementRenderer. For instance my MenuItems and Text-Elements really use the same TextRenderer implementation!</p>
<p>You can see in figure 2 the removed hierachy and the new ElementRenderer interface I've come up with as well as the first three implementations.</p>
<p><img src="http:&#47;&#47;nifty-gui.lessvoid.com&#47;images&#47;remove-inheritance-after.png" alt="figure 2: inject some interface is better" border="0" height="128" hspace="2" vspace="2" width="355" &#47;></p>
<p><strong>figure 2: inject some interface is better<&#47;strong></p>
<p>There is one drawback however. All state information that an ElementRenderer needs to render an Element have to be somehow public accessible. In my case that was not much of a problem, because the Element already had a Postition on Screen. All other Information is part of the Renderer. For instance to render an Image the filename of the image is part of the ImageRenderer and not of the Element (because it has something to do with the visual representation of the element, so there's no need to "polute" the Element class with that information).</p>
<p>I'm pretty happy now :)</p>
<p>Reference:</p>
<ul>
<li><a href="http:&#47;&#47;www.javaworld.com&#47;javaworld&#47;jw-08-2003&#47;jw-0801-toolbox.html" title="Why extends is evil">Java World article: Why extends is evil<&#47;a><&#47;li>
<li><a href="http:&#47;&#47;tech.puredanger.com&#47;2007&#47;07&#47;03&#47;pattern-hate-template&#47;" title="Alex Miller - Patters I Hate #2: Template Pattern">Alex Miller Blog: Pattern I Hate #2: Template Method)<&#47;a><&#47;li><br />
<&#47;ul><br />
<a href="http:&#47;&#47;www.javaworld.com&#47;javaworld&#47;jw-08-2003&#47;jw-0801-toolbox.html" title="Why extends is evil"><br />
<&#47;a></p>
