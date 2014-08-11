<?xml version="1.0" encoding="UTF-8"?>
<stylesheet
	xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<import href="Text.xsl" />
<output method="text" />

<param name="ucs-name" />
<param name="merged-file-name" />
<variable name="merged-file" select="document($merged-file-name)" />
<variable name="link-type" select="'multi'" />

<template match="//uct:data">
	<variable name="file-name" select="@code" />
<result-document href="{$file-name}.html">

<value-of select="uct:page-start(concat('#data.structure.page.title# ',@name),$ucs-name,'../','data',false())" />
<call-template name="data-template" />
<value-of select="uct:page-end('../')" />

</result-document>
</template>

<template name="data-template">
&lt;a id="data_<value-of select="@code"/>">&lt;/a>
&lt;h3>&lt;i class="icon-table">&lt;/i> &lt;span class="muted">#data.structure#&lt;/span> <text />
	<value-of select="@name"/>&lt;/h3>
&lt;strong>#code#:&lt;/strong> <value-of select="@code"/>
<if test="@category">
&lt;div>&lt;strong>#category#:&lt;/strong> <text />
	<value-of select="uct:data-category(@category)"/>&lt;/div><text />
</if>
<if test="uct:description">
&lt;div>&lt;strong>#description#:&lt;/strong>&lt;/div><text />
<for-each select="uct:description">
	<if test="not(position()=1)">&lt;br/>
</if>
	<call-template name="text-template" />
</for-each>
</if>
<if test="uct:attribute | uct:attribute-ref">
&lt;div>&lt;strong>#data.attributes#:&lt;/strong>&lt;/div><text />
<call-template name="data-att-table-template" />
</if>
<value-of select="uct:data-references-data($merged-file,@code,$link-type)"/>
<value-of select="uct:data-references-uc($merged-file,@code,$link-type)"/>
<call-template name="data-expansion-template" />
</template>

<template match="uct:attribute">
&lt;tr>&lt;td><value-of select="@name"/>&lt;/td>&lt;td><text />
	<value-of select="uct:cell(@code)"/>&lt;/td>&lt;td<text />
	<value-of select="uct:status-hint(@status)"/>><text />
	<value-of select="uct:cell(uct:status(@status))"/>&lt;/td>&lt;td><text />
	<value-of select="uct:cell(uct:data-type(
		@type,@length,@collection,$merged-file,$link-type))"/>
	<text />&lt;/td>&lt;td><value-of select="uct:cell(@description)"/>&lt;/td><text />
<text />&lt;/tr><text />
</template>

<template match="uct:attribute-ref">
	<variable name="ref-structure" select="uct:data-att-ref-structure(@type,$merged-file)" />
	<variable name="ref-att" select="uct:data-att-ref(@type,$merged-file)" />
	<variable name="status" select="uct:value-with-default(@status,$ref-att/@status)" />
	<variable name="type" select="uct:data-type(
		$ref-att/@type,uct:value-with-default(@length,$ref-att/@length),
		$ref-att/@collection,$merged-file,$link-type)" />
&lt;tr>&lt;td><value-of select="uct:value-with-default(@name,$ref-att/@name)"/>&lt;/td>&lt;td><text />
	<value-of select="uct:value-with-default(@code,$ref-att/@code)"/>&lt;/td>&lt;td<text />
	<value-of select="uct:status-hint($status)"/>><text />
	<value-of select="uct:cell(uct:status($status))"/>&lt;/td>&lt;td><text />
	<value-of select="uct:data-att-ref-type($ref-structure,@type,$merged-file,$link-type)"/>
	<if test="$type!=''">
		<text> (</text>
		<value-of select="$type" />
		<text>)</text>
	</if>
	<text />&lt;/td>&lt;td><value-of select="uct:cell(
		uct:value-with-default(@description,$ref-att/@description))"/>&lt;/td><text />
<text />&lt;/tr><text />
</template>

<template match="uct:attribute | uct:attribute-ref" mode="expand">
	<variable name="type" select="@type" />
	<choose>
	<when test="$merged-file//uct:data[@code=$type and @expand=true()]">
		<apply-templates 
			select="$merged-file//uct:data[@code=$type]/(uct:attribute | uct:attribute-ref)" 
			mode="expand" />
	</when>
	<otherwise>
		<apply-templates select="." />
	</otherwise>
	</choose>
</template>

<template name="data-att-table-template">
<param name="mode" />
&lt;table class="table table-striped table-bordered table-hover table-condensed">
&lt;thead>
&lt;tr>&lt;th>#name#&lt;/th>&lt;th>#code#&lt;/th><text />
<text />&lt;th>#status#&lt;/th><text />
<text />&lt;th>#type#&lt;/th><text />
<text />&lt;th>#description#&lt;/th>&lt;/tr>
&lt;/thead>&lt;tbody><text />
<choose>
<when test="$mode='expand'">
	<apply-templates select="uct:attribute | uct:attribute-ref" mode="expand" />
</when>
<otherwise>
	<apply-templates select="uct:attribute | uct:attribute-ref" />
</otherwise>
</choose>
&lt;/tbody>
&lt;/table><text />
</template>

<template name="data-expansion-template">
	<if test="for $att in(uct:attribute) return 
		$merged-file//uct:data[@code=$att/@type and @expand=true()]">
&lt;br/>
&lt;div>&lt;strong>#data.structure.expansion#:&lt;/strong><text />
		<call-template name="data-att-table-template">
			<with-param name="mode" select="'expand'" />
		</call-template>
&lt;/div><text />
	</if>
</template>

</stylesheet>
