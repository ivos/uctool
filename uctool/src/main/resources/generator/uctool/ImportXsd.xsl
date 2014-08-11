<?xml version="1.0" encoding="UTF-8"?>
<stylesheet
	xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<output method="text" />

<param name="uctool-version" />

<template match="/">
<text />&lt;?xml version="1.0" encoding="UTF-8"?>
&lt;uct xmlns="http://uctool.sf.net/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:h="http://www.w3.org/1999/xhtml"
	xsi:schemaLocation="
		http://uctool.sf.net/ http://uctool.sf.net/xsd/<value-of select="$uctool-version" />/uct.xsd
		http://www.w3.org/1999/xhtml http://www.w3.org/2002/08/xhtml/xhtml1-transitional.xsd"><text />
	<apply-templates select="/xs:schema/xs:simpleType"/>
	<apply-templates select="/xs:schema/xs:complexType"/>
&lt;/uct>
</template>

<template match="xs:simpleType">
	<variable name="name" select="uct:name(.)" />
	<variable name="code" select="uct:code(@name)" />
	&lt;data name="<value-of select="$name" />" code="<value-of select="$code" />"><text />
	<for-each select="xs:annotation/xs:documentation">
		<if test="not(position()=1)">
		&lt;description><value-of select="." />&lt;/description><text />
		</if>
	</for-each>
	<if test="xs:restriction">
		<variable name="type" select="uct:stripNamespace(xs:restriction/@base)" />
		&lt;description>#type#: <value-of select="$type" />.&lt;/description><text />
		<for-each select="xs:restriction/xs:enumeration">
		&lt;description>#enumeration.value#: <value-of select="@value" />.<text />
			<if test="xs:annotation/xs:documentation">
				<text> </text>
				<value-of select="normalize-space(string-join(xs:annotation/xs:documentation,' '))" />
			</if>
			<text>&lt;/description></text>
		</for-each>
		<for-each select="xs:restriction/xs:length">
		&lt;description>#length#: <value-of select="@value" />.<text />
			<if test="xs:annotation/xs:documentation">
				<text> </text>
				<value-of select="normalize-space(string-join(xs:annotation/xs:documentation,' '))" />
			</if>
			<text>&lt;/description></text>
		</for-each>
		<for-each select="xs:restriction/xs:minLength">
		&lt;description>#min.length#: <value-of select="@value" />.<text />
			<if test="xs:annotation/xs:documentation">
				<text> </text>
				<value-of select="normalize-space(string-join(xs:annotation/xs:documentation,' '))" />
			</if>
			<text>&lt;/description></text>
		</for-each>
		<for-each select="xs:restriction/xs:maxLength">
		&lt;description>#max.length#: <value-of select="@value" />.<text />
			<if test="xs:annotation/xs:documentation">
				<text> </text>
				<value-of select="normalize-space(string-join(xs:annotation/xs:documentation,' '))" />
			</if>
			<text>&lt;/description></text>
		</for-each>
		<for-each select="xs:restriction/xs:pattern">
		&lt;description>#pattern#: <value-of select="@value" />.<text />
			<if test="xs:annotation/xs:documentation">
				<text> </text>
				<value-of select="normalize-space(string-join(xs:annotation/xs:documentation,' '))" />
			</if>
			<text>&lt;/description></text>
		</for-each>
	</if>
	&lt;/data><text />
</template>

<template match="xs:complexType">
	<variable name="name" select="uct:name(.)" />
	<variable name="code" select="uct:code(@name)" />
	&lt;data name="<value-of select="$name" />" code="<value-of select="$code" />"><text />
<for-each select="xs:annotation/xs:documentation">
	<if test="not(position()=1)">
		&lt;description><value-of select="." />&lt;/description><text />
	</if>
</for-each>
		<apply-templates select=".//xs:element" />
	&lt;/data><text />
</template>

<template match="xs:element">
	<variable name="name" select="uct:name(.)" />
		&lt;attribute name="<value-of select="$name" />" code="<value-of select="@name" />"<text />
	<choose>
		<when test="@minOccurs eq '0'"> status="optional"</when>
		<otherwise> status="mandatory"</otherwise>
	</choose>
	<variable name="type" select="uct:type(.)" />
	<if test="($type)">
		<text> type="</text>
		<value-of select="$type" />
		<text>"</text>
	</if>
	<if test="(@type) and not($type)">
		<text> type="</text>
		<value-of select="uct:code(@type)" />
		<text>"</text>
	</if>
	<if test="@maxOccurs='unbounded'">
		<text> collection="true"</text>
	</if>
	<if test="(@maxOccurs) and (not(@maxOccurs='unbounded')) and (@maxOccurs > 1)">
		<text> collection="true" length="</text>
		<value-of select="@maxOccurs" />
		<text>"</text>
	</if>
	<if test="(xs:simpleType/xs:restriction/xs:maxLength/@value) and not((@maxOccurs) and (not(@maxOccurs='unbounded')))">
		<text> length="</text>
		<value-of select="xs:simpleType/xs:restriction/xs:maxLength/@value" />
		<text>"</text>
	</if>
	<if test="count(xs:annotation/xs:documentation) gt 1">
		<text> description="</text>
		<value-of select="normalize-space(string-join(xs:annotation/xs:documentation[position() gt 1],' '))" />
		<text>"</text>
	</if>
	<text> /></text>
</template>

<function name="uct:name" as="xs:string">
	<param name="this" as="node()" />
	<variable name="result">
		<choose>
			<when test="$this/xs:annotation/xs:documentation">
				<value-of select="normalize-space($this/xs:annotation/xs:documentation[1])" />
			</when>
			<otherwise>
				<value-of select="$this/@name" />
			</otherwise>
		</choose>
	</variable>
	<value-of select="$result" />
</function>

<function name="uct:stripNamespace" as="xs:string">
	<param name="type" as="xs:string" />
	<variable name="result">
		<choose>
			<when test="contains($type,':')">
				<value-of select="substring-after($type,':')" />
			</when>
			<otherwise>
				<value-of select="$type" />
			</otherwise>
		</choose>
	</variable>
	<value-of select="$result" />
</function>

<function name="uct:code" as="xs:string">
	<param name="type" as="xs:string" />
	<variable name="result">
		<choose>
			<when test="ends-with($type,'Type')">
				<value-of select="substring($type,0,string-length($type)-3)" />
			</when>
			<otherwise>
				<value-of select="$type" />
			</otherwise>
		</choose>
	</variable>
	<value-of select="uct:stripNamespace($result)" />
</function>

<function name="uct:type" as="xs:string">
	<param name="this" as="node()" />
	<variable name="result">
		<choose>
			<when test="($this/@type) and (uct:typeFromString($this/@type))">
				<value-of select="uct:typeFromString($this/@type)" />
			</when>
			<when test="($this/xs:simpleType/xs:restriction)">
				<value-of select="uct:typeFromString($this/xs:simpleType/xs:restriction/@base)" />
			</when>
		</choose>
	</variable>
	<value-of select="$result" />
</function>

<function name="uct:typeFromString" as="xs:string">
	<param name="type" as="xs:string" />
	<variable name="stripped" select="uct:stripNamespace($type)" />
	<variable name="result">
		<choose>
			<when test="$stripped='integer'">integer</when>
			<when test="$stripped='byte'">integer</when>
			<when test="$stripped='int'">integer</when>
			<when test="$stripped='long'">integer</when>
			<when test="$stripped='nonNegativeInteger'">integer</when>
			<when test="$stripped='nonPositiveInteger'">integer</when>
			<when test="$stripped='positiveInteger'">integer</when>
			<when test="$stripped='short'">integer</when>
			<when test="$stripped='unsignedByte'">integer</when>
			<when test="$stripped='unsignedInt'">integer</when>
			<when test="$stripped='unsignedLong'">integer</when>
			<when test="$stripped='unsignedShort'">integer</when>
			<when test="$stripped='float'">float</when>
			<when test="$stripped='decimal'">decimal</when>
			<when test="$stripped='double'">decimal</when>
			<when test="$stripped='boolean'">boolean</when>
			<when test="$stripped='date'">date</when>
			<when test="$stripped='time'">time</when>
			<when test="$stripped='dateTime'">timestamp</when>
			<when test="$stripped='duration'">timestamp</when>
			<when test="$stripped='base64Binary'">binary</when>
			<when test="$stripped='string'">string</when>
			<when test="$stripped='normalizedString'">string</when>
			<when test="$stripped='anyURI'">string</when>
			<when test="$stripped='anySimpleType'">string</when>
		</choose>
	</variable>
	<value-of select="$result" />
</function>

</stylesheet>
