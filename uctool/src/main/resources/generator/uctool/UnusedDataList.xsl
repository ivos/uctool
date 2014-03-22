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
<value-of select="uct:page-start('#unused.data.list.page.title#',$ucs-name,'../','summary',false())" />
<call-template name="unused-data-list-template" />
<value-of select="uct:page-end('../')" />
</template>

<template name="unused-data-list-template">
&lt;h2>#unused.data.list.page.title#&lt;/h2><text />

<call-template name="data-index-start-template" />
<for-each select="//uct:data-structure">
	<variable name="code" select="@code" />
	<variable name="ref-codes" 
		select="uct:data-references-data-codes($code,$merged-file)" />
	<if test="empty($ref-codes) and not(//uct:data-ref[@code=$code])">
		<call-template name="data-index-data-template" />
	</if>
</for-each>
<call-template name="data-index-end-template" />

</template>

</stylesheet>
