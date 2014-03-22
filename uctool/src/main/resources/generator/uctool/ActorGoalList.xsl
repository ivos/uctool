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
<value-of select="uct:page-start('#actor.goal.list.page.title#',$ucs-name,'../','summary',false())" />
<call-template name="actor-goal-list-template" />
<value-of select="uct:page-end('../')" />
</template>

<template name="actor-goal-list-template">
	<variable name="primary-actor-codes" 
		select="distinct-values(//uct:uc-group/@primary-actor)" />
&lt;h2>#actor.goal.list.page.title#&lt;/h2><text />
	<for-each select="$primary-actor-codes">
		<call-template name="primary-actor-template" />
	</for-each>
</template>

<template name="primary-actor-template">
	<variable name="code" select="." />

&lt;h3>&lt;i class="icon-user">&lt;/i> &lt;span class="muted">#primary.actor#&lt;/span> <text />
	<value-of select="uct:actor-name-ref(.,$merged-file)" />
	<text />&lt;/h3><text />
<for-each select="$merged-file//uct:actor[@code=$code]">
	<call-template name="actor-goals-template" />
</for-each>
</template>

</stylesheet>
