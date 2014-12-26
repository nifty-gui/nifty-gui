---
layout: post
status: publish
published: true
title: XML-Validation and Text Rendering with In-Text change of the Textcolor
author:
  display_name: void
  login: admin
  email: void@lessvoid.com
  url: http://nifty-gui.lessvoid.com/
author_login: admin
author_email: void@lessvoid.com
author_url: http://nifty-gui.lessvoid.com/
wordpress_id: 144
wordpress_url: http://nifty-gui.lessvoid.com/?p=144
date: '2010-05-18 14:42:01 +0200'
date_gmt: '2010-05-18 13:42:01 +0200'
categories:
- Uncategorized
- sightings
tags: []
comments:
- id: 385
  author: GaRzY
  author_email: dagarill@gmail.com
  author_url: http://servergarzy.com
  date: '2010-05-27 17:35:47 +0200'
  date_gmt: '2010-05-27 16:35:47 +0200'
  content: "Thank you very much for the .xsd schema definition. I'm learning to use
    nifty and is easier to write the xml using the CTRL + Space in Eclipse.\r\n\r\nCould
    be great add documentation in the .xsd schema, to show this info in the Eclipse
    IDE when write an xml file.\r\n\r\nCool OpenGL GUI LIB!!"
- id: 1912
  author: Dmitriy
  author_email: dmitriy-ltv@yandex.ru
  author_url: ''
  date: '2013-06-14 08:59:19 +0200'
  date_gmt: '2013-06-14 07:59:19 +0200'
  content: "Guys! Help me please!\r\nI'm trying the following thing:\r\n\r\n\r\nand
    got errors from SAXParser. Below are several tests with results:\r\n\r\n\r\nValue
    '#9932cc' is not facet-valid... And expected color IS applied!\r\nValue '\\#9932cc'
    is not facet-valid... And expected color is NOT applied!\r\nValue '\\#9932cc#'
    is not facet-valid... And expected color is NOT applied!\r\nValue '\\#9932cc\\#'
    is not facet-valid... And expected color is NOT applied!"
- id: 1920
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2013-06-15 13:37:57 +0200'
  date_gmt: '2013-06-15 12:37:57 +0200'
  content: |-
    Can you provide more of the error message and a piece of your XML-File? There is no constraint on the text attribute in the XSD. It's just a plain "xs:string" which should allow the colored encoded magic. The correct value would be "\#ff0000#". What version of Nifty do you use?

    Here is the complete quote from the manual with the color encoded specification: "This string starts with &bdquo;\#&ldquo; followed by three values, one for red, green and blue as hexadecimal values (optionally followed by an alpha value). The string has to end with a single &bdquo;#&ldquo; character."
- id: 2761
  author: Hammer
  author_email: roxxzorz1@gmail.com
  author_url: ''
  date: '2014-01-23 19:46:19 +0100'
  date_gmt: '2014-01-23 18:46:19 +0100'
  content: Hey, I'm having a problem loading the xml file, as I recieve a nullPointerException
    at load time, however I am quite sure the file is available and at the correct
    location.
- id: 2762
  author: Hammer
  author_email: roxxzorz1@gmail.com
  author_url: ''
  date: '2014-01-23 19:47:05 +0100'
  date_gmt: '2014-01-23 18:47:05 +0100'
  content: Also, tried loading it as an InputStream but that did not help.
- id: 2764
  author: void
  author_email: void@lessvoid.com
  author_url: http://nifty-gui.lessvoid.com/
  date: '2014-01-24 22:13:58 +0100'
  date_gmt: '2014-01-24 21:13:58 +0100'
  content: What about sending the whole Stacktrace and especially the version of Nifty
    you're using together with a code snippet and put it all into a github issue https:&#47;&#47;github.com&#47;void256&#47;nifty-gui
    - I'd be happy to help then :)
---
<p><strong>XML-Validation<&#47;strong></p>
<p>The actual parsing of Nifty-XML files is still using a XPP3 based parser and does not require a XML-Schema Definition. The nifty.xsd was only added to validate Nifty XML-Files ... as well as for special support of the jMonkeyEngine3 SDK but that will be explained somewhere else :)</p>
<p>XML-Validation is now build into Nifty but is still an optional step you can use to ensure your Nifty XML is well formed and valid. To perform the actual validation two new methods have been added to the Nifty class:</p>
<pre class="brush:java">public void validateXml(final String filename) throws Exception<br />
public void validateXml(final InputStream stream) throws Exception<&#47;pre><br />
Both methods will simply return or will throw an Exception when any errors have been detected.</p>
<p>To successfully validate the nifty xml file, your xml file should start with the two following lines:</p>
<pre class="brush:xml"><?xml version="1.0" encoding="UTF-8"?><br />
<nifty xmlns="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty.xsd" xmlns:xsi="http:&#47;&#47;www.w3.org&#47;2001&#47;XMLSchema-instance" xsi:schemaLocation="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty.xsd http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty.xsd"><&#47;pre><br />
This way the XML-File is using the correct Nifty Namespace. If you would omit these declarations your XML-File can still be parsed but it will probably not validate against the XSD.</p>
<p>The namespace that has been definied for nifty is actually the URL where you can download the xsd too: <a href="http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty.xsd">http:&#47;&#47;nifty-gui.sourceforge.net&#47;nifty.xsd<&#47;a></p>
<p><strong>In-Text changes of Textcolor<&#47;strong></p>
<p>Up until now Nifty uses the "binary" character value 0x01 directly in the text to indicate that the next three bytes in the text will contain red, green and blue values that represent the color of the text that follows after the color definition.</p>
<p>So you could write a red word, example:</p>
<pre class="brush:xml"><text text="a word in &amp;#01;&amp;#255;&amp;#0;&amp;#0;red"&#47;><&#47;pre><br />
This works but is unfortunatly not a valid XML file when trying to validate it with an XML-Schema Definition :)  The problem is, that a valid XML attribute value must not contain values below binary 0x20 (with only a couple of exceptions, like the tab character 0x09). 0x01 is not allowed and will always create a validation error!  So the current solution is to use a new format that doesn't use 0x01 as a indicator anymore. Instead Nifty will now watch for the special String "\#" to mark the beginning of a color definition and "#" as the end. So instead of the xml example given above you can now write:</p>
<pre class="brush:xml"><text text="a word in \#F00#red"&#47;><&#47;pre><br />
Which is more readable too!</p>
<p>There is a short Version supported "\#F00#" as well as a long version "\#FF0000#". Both are not case sensitive so you could write "\#fa9#" too. But remember the trailing # :)</p>
<p>Have fun,<br />
void</p>
