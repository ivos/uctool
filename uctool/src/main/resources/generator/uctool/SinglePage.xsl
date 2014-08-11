<?xml version="1.0" encoding="UTF-8"?>
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<import href="Requirement.xsl" />	
<import href="RequirementIndex.xsl" />
<import href="ActorGoalList.xsl" />
<import href="Actor.xsl" />
<import href="ActorIndex.xsl" />
<import href="UseCase.xsl" />
<import href="UseCaseIndex.xsl" />
<import href="Data.xsl" />
<import href="DataIndex.xsl" />
<import href="AttachmentIndex.xsl" />
<import href="GlossaryIndex.xsl" />
<output method="text" />

<param name="ucs-name" />
<param name="ucs-version" />
<param name="ucs-description" />

<variable name="merged-file" select="/" />
<variable name="link-type" select="'single'" />

<template match="/">
<text/>&lt;!DOCTYPE HTML>
&lt;html>
&lt;head>
&lt;title>#single.page.title#&lt;/title>
&lt;meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
&lt;meta http-equiv="cache-control" content="max-age=0" />
&lt;meta http-equiv="cache-control" content="no-cache" />
&lt;meta http-equiv="expires" content="0" />
&lt;meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
&lt;meta http-equiv="pragma" content="no-cache" />
&lt;link rel="stylesheet" type="text/css" href="../resources/css/styles.css"/>
&lt;link rel="shortcut icon" href="../resources/favicon.ico" />
&lt;meta name="viewport" content="width=device-width, initial-scale=1.0" />
&lt;/head>
&lt;body>
&lt;div class="container">
&lt;h1>#single.page.title#&lt;/h1>
&lt;p class="lead">#ucs.name#: <value-of select="$ucs-name" />&lt;br/>
#ucs.version#: <value-of select="$ucs-version" />&lt;/p>
&lt;p class="lead"><value-of select="$ucs-description" />&lt;/p><text />

<call-template name="requirement-index-template" />

&lt;h2>#requirement.details.section#&lt;/h2><text />
<for-each select="//uct:requirement">
<call-template name="requirement-template" />
</for-each>

<call-template name="actor-index-template" />

&lt;h2>#actor.details.section#&lt;/h2><text />
<for-each select="//uct:actor">
<text>
</text>
<call-template name="actor-template" />
</for-each>

<call-template name="use-case-index-template" />

&lt;h2>#use.case.details.section#&lt;/h2><text />
<for-each select="//uct:use-case[@code]">
<call-template name="use-case-template" />
</for-each>

<call-template name="data-index-template" />

&lt;h2>#data.structure.details.section#&lt;/h2><text />
<for-each select="//uct:data">
<call-template name="data-template" />
</for-each>

<call-template name="attachment-index-template" />

<call-template name="glossary-index-template" />

<text/>
&lt;/p/><text/>
<value-of select="uct:uctool-link()" />
&lt;/div>
&lt;script src="../resources/js/scripts.js" type="text/javascript">&lt;/script>
&lt;/body>
&lt;/html>
<text/>
</template>

</stylesheet>
