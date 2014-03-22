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
<value-of select="uct:page-start('#attachment.index.page.title#',$ucs-name,'../','attachment',true())" />
<call-template name="attachment-index-template" />
<value-of select="uct:page-end('../')" />
</template>

<template name="attachment-index-template">

&lt;h2>#attachment.index.page.title#&lt;/h2><text />
<for-each select="//uct:attachment-group">
&lt;h3><value-of select="@name" />&lt;/h3><text />
<if test="uct:attachment">
&lt;ul><text />
<for-each select="uct:attachment">
&lt;li><value-of select="uct:attachment-ref(@code,'',$merged-file,$link-type)" />&lt;/li><text />
</for-each>
&lt;/ul><text />
</if>
</for-each>
</template>

</stylesheet>
