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

<template match="//uct:use-case[@code]">
	<variable name="file-name" select="@code" />
	<variable name="page-title" select="concat('#use.case.page.title# ',@code,' - ',@goal)" />
<result-document href="{$file-name}.html">

<value-of select="uct:page-start($page-title,$ucs-name,'../','uc',false())" />
<call-template name="use-case-template" />
<value-of select="uct:page-end('../')" />

</result-document>
</template>

<template name="use-case-template">
	<variable name="uc-code" select="@code" />
	<variable name="type" select="ancestor::uct:uc-group/@type" />
	<variable name="visibility" select="ancestor::uct:uc-group/@visibility" />
	<variable name="scope" select="ancestor::uct:uc-group/@scope" />
	<variable name="primary-actor" select="ancestor::uct:uc-group/@primary-actor" />
&lt;a id="uc_<value-of select="@code"/>">&lt;/a>
&lt;h3>&lt;i class="icon-file">&lt;/i> &lt;span class="muted">#use.case#&lt;/span> <text />
	<value-of select="@code"/> - <text/>
	<if test="$visibility and $type">
		<value-of select="uct:img(concat($visibility,'-',$type),
			concat(uct:visibility($visibility),', ',uct:uc-type($type)))" />
		<text/>&amp;nbsp;<text/>
	</if>
	<value-of select="@goal"/>
	<if test="@level">
		<text/>&amp;nbsp;<text/>
		<value-of select="uct:img(@level,uct:level(@level))" />
	</if>
	<text/>&lt;/h3>
&lt;table><text />
<if test="$scope">
	&lt;tr>&lt;td>&lt;strong>#scope#:&lt;/strong> <text />
		<value-of select="$scope"/>&lt;/td>&lt;/tr><text />
</if>
&lt;tr>&lt;td><text />
<if test="$type">
	&lt;strong>#uc.type#:&lt;/strong> <text />
		<value-of select="uct:uc-type($type)"/>&amp;nbsp;&amp;nbsp;<text />
</if>
<if test="$visibility">
	&lt;strong>#visibility#:&lt;/strong> <text />
		<value-of select="uct:visibility($visibility)"/>&amp;nbsp;&amp;nbsp;<text />
</if>
<if test="@level">
	&lt;strong>#level#:&lt;/strong> <value-of select="uct:level(@level)"/>
</if>
&lt;/td>&lt;/tr><text />
<if test="$primary-actor">
	&lt;tr>&lt;td>&lt;strong>#primary.actor#:&lt;/strong> <text />
		<for-each select="$primary-actor">
			<value-of select="uct:actor-ref(.,$merged-file,$link-type)" />
		</for-each>
		<text />&lt;/td>&lt;/tr><text />
</if>
<if test="uct:description">
	&lt;tr>&lt;td>&lt;strong>#description#:&lt;/strong>&lt;br/><text />
<for-each select="uct:description">
	<if test="not(position()=1)">&lt;br/>
</if>
	<call-template name="text-template" />
</for-each>
	&lt;/td>&lt;/tr><text />
</if>
<if test="uct:stakeholders-interests">
&lt;tr>&lt;td>&lt;strong>#stakeholders.interests#:&lt;/strong><text />
	<text />&lt;/td>&lt;/tr><text />
<apply-templates select="uct:stakeholders-interests/uct:interest"/>
</if>
<if test="uct:preconditions">
	&lt;tr>&lt;td>&lt;strong>#preconditions#:&lt;/strong>
	<apply-templates select="uct:preconditions"/>
	&lt;/td>&lt;/tr><text />
</if>
<if test="uct:minimal-guarantees">
	&lt;tr>&lt;td>&lt;strong>#minimal-guarantees#:&lt;/strong>
	<apply-templates select="uct:minimal-guarantees"/>
	&lt;/td>&lt;/tr><text />
</if>
<if test="uct:success-guarantees">
	&lt;tr>&lt;td>&lt;strong>#success-guarantees#:&lt;/strong>
	<apply-templates select="uct:success-guarantees"/>
	&lt;/td>&lt;/tr><text />
</if>
<if test="uct:trigger">
	&lt;tr>&lt;td>&lt;strong>#trigger#:&lt;/strong> <text />
		<value-of select="uct:trigger"/>&lt;/td>&lt;/tr><text />
</if>
<if test="uct:success">
&lt;tr>&lt;td>&lt;strong>#main.success.scenario#:&lt;/strong>&lt;/td>&lt;/tr>
&lt;tr>&lt;td class="layout"><text />
<apply-templates select="uct:success/*">
	<with-param name="indent" select="''"/>
</apply-templates>
&lt;/td>&lt;/tr><text />
</if>
<if test="uct:extensions">
&lt;tr>&lt;td>&lt;strong>#extensions#:&lt;/strong>&lt;/td>&lt;/tr>
&lt;tr>&lt;td class="layout"><text />
<apply-templates select="uct:extensions/uct:condition"/>
&lt;/td>&lt;/tr><text />
</if>
<if test="uct:notes">
&lt;tr>&lt;td>&lt;strong>#notes#:&lt;/strong>&lt;br/><text />
<for-each select="uct:notes">
	<call-template name="text-template" />
</for-each>
&lt;/td>&lt;/tr><text />
</if>
<value-of select="uct:uc-references($merged-file,$uc-code,$link-type)"/>
&lt;/table><text />
</template>

<template match="uct:preconditions|uct:minimal-guarantees|uct:success-guarantees">
&lt;ul><text />
	<call-template name="items-template" />
&lt;/ul><text />
</template>

<template match="uct:step">
<param name="indent"/>
<param name="prefix"/>
<variable name="number">
	<choose>
		<when test="local-name(..)='condition'"><number count="uct:step" from="uct:condition"/></when>
		<when test="local-name(..)='step-condition'">
			<number count="uct:step" from="uct:step-condition"/>
		</when>
		<otherwise><number count="uct:step" from="uct:success"/></otherwise>
	</choose>
</variable>
<variable name="full-number" select="concat($prefix,$number)"/>
<call-template name="uc-line-start-template">
	<with-param name="indent" select="$indent" />
	<with-param name="number" select="$full-number" />
</call-template>
<call-template name="step-template" />
<call-template name="uc-line-end-template" />
</template>

<template match="uct:step-extensions">
<param name="indent"/>
<param name="prefix"/>
<variable name="extra-indent">
	<if test="not(local-name(..)='use-case')"><value-of select="uct:indent()"/></if>
</variable>
<variable name="number" select="count((preceding-sibling::uct:step|preceding-sibling::uct:ref-step))"/>
<apply-templates select="uct:step-condition">
	<with-param name="indent" select="concat($indent,$extra-indent)"/>
	<with-param name="prefix" select="concat($prefix,$number)"/>
</apply-templates>
</template>

<template match="uct:condition|uct:step-condition">
<param name="indent"/>
<param name="prefix"/>
<variable name="step-ref" select="@step-ref"/>
<variable name="number">
	<choose>
		<when test="@step-ref">
			<number count="uct:condition[@step-ref=$step-ref]" from="uct:extensions" format="a"/>
		</when>
		<otherwise><number from="uct:step-extensions" format="a"/></otherwise>
	</choose>
</variable>
<variable name="reference">
	<if test="@step-ref"><call-template name="step-ref-template"/></if>
</variable>
<variable name="full-number" select="concat($reference,$prefix,$number)"/>
<call-template name="uc-line-start-template">
	<with-param name="indent" select="$indent" />
	<with-param name="number" select="$full-number" />
</call-template>
<call-template name="when-template" />:<text/>
<if test="@inline-step"><text> </text><value-of select="@inline-step"/></if>
<call-template name="uc-line-end-template" />
<apply-templates select="uct:step | uct:step-extensions">
	<with-param name="indent" select="concat($indent,uct:indent())"/>
	<with-param name="prefix" select="$full-number"/>
</apply-templates>
</template>

<template name="when-template">
<choose>
<when test="@when and not(uct:when)"><value-of select="@when"/></when>
<when test="uct:when and not(@when)">
	<for-each select="uct:when">
		<call-template name="step-template" />
	</for-each>
</when>
<otherwise>
	<message>
		<text>Error processing use case with code </text>
		<value-of select="ancestor::uct:use-case/@code" />
		<if test="local-name()='condition'">
			<text>, condition to step </text>
			<value-of select="@step-ref" />
		</if>
	</message>
	<message terminate="yes">
		<text>Either a "when" attribute or a "when" element expected, but found none or both.</text>
	</message>
</otherwise>
</choose>
</template>

<template match="uct:interest">
&lt;tr>&lt;td><text/>
<value-of select="@stakeholder"/>
<text/>: <text/>
<call-template name="step-template" />
<text/>&lt;/td>&lt;/tr><text />
</template>

<template name="uc-line-start-template">
<param name="indent"/>
<param name="number"/>
&lt;table>&lt;tr>&lt;td class="layout"><text/>
<value-of select="$indent"/>
<value-of select="$number"/>
<text/>.&amp;nbsp;&lt;/td>&lt;td class="layout"><text/>
</template>

<template name="uc-line-end-template">
<text/>&lt;/td>&lt;/tr>&lt;/table><text />
</template>

</stylesheet>
