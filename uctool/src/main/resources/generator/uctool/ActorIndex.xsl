<?xml version="1.0" encoding="UTF-8"?>
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<import href="functions/UctTemplates.xsl" />
<output method="text" />

<param name="ucs-name" />
<variable name="merged-file" select="/" />
<variable name="link-type" select="'multi'" />

<template match="/">
<value-of select="uct:page-start('#actor.index.page.title#',$ucs-name,'../','actor',true())" />
<call-template name="actor-index-template" />
<value-of select="uct:page-end('../')" />
</template>

<template name="actor-index-template">

&lt;h2>#actor.index.page.title#&lt;/h2><text />
&lt;ul class="nav nav-tabs nav-stacked">
<for-each select="//uct:actor">
&lt;li><value-of select="uct:actor-ref(@code,$merged-file,$link-type)" />&lt;/li><text />
</for-each>
&lt;/ul><text />
</template>

</stylesheet>
