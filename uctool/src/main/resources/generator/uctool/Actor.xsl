<?xml version="1.0" encoding="UTF-8"?>
<stylesheet
	xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<import href="functions/UctTemplates.xsl" />
<import href="Text.xsl" />
<output method="text" />

<param name="ucs-name" />
<param name="merged-file-name" />
<variable name="merged-file" select="document($merged-file-name)" />
<variable name="link-type" select="'multi'" />

<template match="//uct:actor">
	<variable name="file-name" select="@code" />
<result-document href="{$file-name}.html">

<value-of select="uct:page-start(concat('#actor.page.title# ',@name),$ucs-name,'../','actor',false())" />
<call-template name="actor-template" />
<value-of select="uct:page-end('../')" />

</result-document>
</template>

<template name="actor-template">
&lt;a id="actor_<value-of select="@code"/>">&lt;/a>
&lt;h3>&lt;i class="icon-user">&lt;/i> &lt;span class="muted">#actor#&lt;/span> <text />
	<value-of select="@name"/>&lt;/h3><text />
<if test="uct:description">
&lt;div>&lt;strong>#description#:&lt;/strong>&lt;/div><text />
<for-each select="uct:description">
	<if test="not(position()=1)">&lt;br/>
</if>
	<call-template name="text-template" />
</for-each>
</if>
<call-template name="actor-extends-template" />
<call-template name="actor-extended-by-template" />
<call-template name="actor-goals-template" />
<call-template name="actor-inherited-actors-template" />
<call-template name="actor-inherited-goals-template" />
</template>

<template name="actor-extends-template">
	<variable name="actor-codes" select="uct:extends-actor/@code" />
<if test="$actor-codes">
&lt;div>&lt;strong>#extends.actors#:&lt;/strong>&lt;/div>
&lt;ul><text />
	<for-each select="$actor-codes">
&lt;li><value-of select="uct:actor-ref(.,$merged-file,$link-type)" />&lt;/li><text />
	</for-each>
&lt;/ul><text />
</if>
</template>

<template name="actor-extended-by-template">
	<variable name="code" select="@code" />
	<variable name="actor-codes" select="$merged-file//uct:actor[uct:extends-actor/@code=$code]/@code" />
<if test="$actor-codes">
&lt;div>&lt;strong>#extended.by.actors#:&lt;/strong>&lt;/div>
&lt;ul><text />
	<for-each select="$actor-codes">
&lt;li><value-of select="uct:actor-ref(.,$merged-file,$link-type)" />&lt;/li><text />
	</for-each>
&lt;/ul><text />
</if>
</template>

<template name="actor-inherited-actors-template">
	<variable name="actor-codes" select="uct:inherited-actor-codes(@code,$merged-file)" />
<if test="not(empty($actor-codes))">
&lt;div>&lt;strong>#inherited.actors#:&lt;/strong>&lt;/div>
&lt;ul><text />
	<for-each select="$actor-codes">
&lt;li><value-of select="uct:actor-ref(.,$merged-file,$link-type)" />&lt;/li><text />
	</for-each>
&lt;/ul><text />
</if>
</template>

<template name="actor-inherited-goals-template">
	<variable name="actor-codes" select="uct:inherited-actor-codes(@code,$merged-file)" />
	<variable name="ucs" select="$merged-file//uct:uc-group
		[not(empty(index-of($actor-codes,@primary-actor)))]/uct:use-case" />
<if test="$ucs">
&lt;div>&lt;strong>#inherited.goals#:&lt;/strong>&lt;/div>
&lt;ul><text />
	<for-each select="$ucs">
&lt;li><value-of select="uct:goal-ref(.,$link-type)" />&lt;/li><text />
	</for-each>
&lt;/ul><text />
</if>
</template>

</stylesheet>
