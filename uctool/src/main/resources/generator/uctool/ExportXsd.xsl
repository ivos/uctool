<?xml version="1.0" encoding="UTF-8"?>
<stylesheet
	xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<output method="text" />

<param name="target-namespace-url" />

<template match="/">
<text />&lt;?xml version="1.0" encoding="UTF-8"?>
&lt;xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="<value-of select="$target-namespace-url" />"
	xmlns="<value-of select="$target-namespace-url" />" elementFormDefault="qualified"><text />
	<apply-templates select="//uct:data-structure"/>
&lt;/xs:schema>
</template>

<template match="uct:data-structure">
	&lt;xs:element name="<value-of select="@code" />" type="<value-of select="@code" />Type" />
	&lt;xs:complexType name="<value-of select="@code" />Type">
		&lt;xs:annotation>
			&lt;xs:documentation><value-of select="@name" />&lt;/xs:documentation><text />
<for-each select="uct:description">
			&lt;xs:documentation><text />
			<value-of select="." />
		<text />&lt;/xs:documentation><text />
</for-each>
		&lt;/xs:annotation>
		&lt;xs:sequence><text />
		<apply-templates select="uct:attribute | uct:attribute-ref" />
		&lt;/xs:sequence><text />
	&lt;/xs:complexType><text />
</template>

<template match="uct:attribute">
			&lt;xs:element name="<value-of select="@code" />"<text />
	<if test="not(@status) or (@status='display-only') or (@status='optional')">
		<text> minOccurs="0"</text>
	</if>
	<if test="(@collection='true') and not(@length)">
		<text> maxOccurs="unbounded"</text>
	</if>
	<if test="(@collection='true') and (@length)">
		<text> maxOccurs="</text>
		<value-of select="@length" />
		<text>"</text>
	</if>
	<variable name="type" select="uct:type(.)" />
	<if test="($type) and not((@length) and not(@collection='true') and (@type='string'))">
		<text> type="</text>
		<value-of select="$type" />
		<text>"</text>
	</if>
	<if test="(@type) and not($type)">
		<text> type="</text>
		<value-of select="@type" />
		<text>Type"</text>
	</if>
	<text>></text>
				&lt;xs:annotation>
					&lt;xs:documentation><value-of select="@name" />&lt;/xs:documentation><text />
	<if test="@description">
					&lt;xs:documentation><value-of select="@description" />&lt;/xs:documentation><text />
	</if>
				&lt;/xs:annotation><text />
	<if test="(@length) and not(@collection='true') and (@type='string')">
				&lt;xs:simpleType>
					&lt;xs:restriction base="<value-of select="$type" />">
						&lt;xs:maxLength value="<value-of select="@length" />" />
					&lt;/xs:restriction>
				&lt;/xs:simpleType><text />
	</if>
			&lt;/xs:element><text />
</template>

<template match="uct:attribute-ref">
			&lt;xs:element name="<value-of select="@type" />" /><text />
</template>

<function name="uct:type" as="xs:string">
	<param name="this" as="node()" />
	<variable name="result">
		<choose>
			<when test="$this/@type='integer'">xs:integer</when>
			<when test="$this/@type='float'">xs:float</when>
			<when test="$this/@type='decimal'">xs:decimal</when>
			<when test="$this/@type='boolean'">xs:boolean</when>
			<when test="$this/@type='date'">xs:date</when>
			<when test="$this/@type='time'">xs:time</when>
			<when test="$this/@type='date-time'">xs:dateTime</when>
			<when test="$this/@type='timestamp'">xs:dateTime</when>
			<when test="$this/@type='binary'">xs:base64Binary</when>
			<when test="$this/@type='string'">xs:string</when>
		</choose>
	</variable>
	<value-of select="$result" />
</function>

</stylesheet>
