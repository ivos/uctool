<?xml version="1.0" encoding="UTF-8"?>
<stylesheet
	xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<import href="functions/UctFunctions.xsl" />
<output method="xml" indent="yes" />

<template name="items-template">
	<apply-templates select="uct:item|uct:text" mode="copy-as-text" />
</template>

<template name="step-template">
	<apply-templates select="node()" mode="copy-as-text" />
</template>

<template name="text-template">
	<apply-templates select="@*|node()" mode="copy-as-text" />
</template>

<template match="uct:item" mode="copy-as-text">
	<text>&lt;li></text>
	<apply-templates select="@*|node()" mode="copy-as-text" />
	<text>&lt;/li></text>
</template>

<template match="uct:text" mode="copy-as-text">
	<apply-templates select="@*|node()" mode="copy-as-text" />
</template>

<template match="uct:step-ref" mode="copy-as-text">
	<call-template name="step-ref-template" />
</template>

<template match="uct:uc-ref" mode="copy-as-text">
	<value-of select="uct:uc-ref(@code,.,$merged-file,$link-type,$link-type)"/>
</template>

<template match="uct:data-ref" mode="copy-as-text">
	<value-of select="uct:data-ref(@code,.,$merged-file,$link-type)"/>
</template>

<template match="uct:req-ref" mode="copy-as-text">
	<value-of select="uct:req-ref(@code,.,$merged-file,$link-type)"/>
</template>

<template match="uct:attachment-ref" mode="copy-as-text">
	<value-of select="uct:attachment-ref(@code,.,$merged-file,$link-type)"/>
</template>

<template match="text()" mode="copy-as-text">
	<value-of select="." />
</template>

<template match="attribute()" mode="copy-as-text">
	<text> </text>
	<value-of select="name(.)" />
	<text>="</text>
	<value-of select="." />
	<text>"</text>
</template>

<template match="element()" mode="copy-as-text">
	<text>&lt;</text>
	<value-of select="local-name(.)" />
		<apply-templates select="@*" mode="copy-as-text" />
	<text>></text>
		<apply-templates select="node()" mode="copy-as-text" />
	<text>&lt;/</text>
	<value-of select="local-name(.)" />
	<text>></text>
</template>

<template name="step-ref-template">
	<variable name="step-handle" select="@handle|@step-ref"/>
	<choose>
		<when test="$step-handle='*'">*</when>
		<when test="contains($step-handle,'-')">
			<value-of select="uct:step-ref(.,substring-before($step-handle,'-'))"/>
			<text/>&#8209;<text/>
			<value-of select="uct:step-ref(.,substring-after($step-handle,'-'))"/>
		</when>
		<otherwise>
			<value-of select="uct:step-ref(.,$step-handle)"/>
		</otherwise>
	</choose>
</template>

</stylesheet>
