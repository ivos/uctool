<?xml version="1.0" encoding="UTF-8"?>
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<import href="functions/UctTemplates.xsl" />
<import href="Text.xsl" />
<output method="text" />

<param name="ucs-name" />
<variable name="merged-file" select="/" />
<variable name="link-type" select="'multi'" />

<template match="/">
<value-of select="uct:page-start('#glossary.index.page.title#',$ucs-name,'../','glossary',true())" />
<call-template name="glossary-index-template" />
<value-of select="uct:page-end('../')" />
</template>

<template name="glossary-index-template">

&lt;h2>#glossary.index.page.title#&lt;/h2>
&lt;table class="table table-striped table-bordered table-hover table-condensed">
&lt;thead>
&lt;tr>
&lt;th>#term#&lt;/th>
&lt;th>#abbreviation#&lt;/th>
&lt;th>#description#&lt;/th>
&lt;/tr>
&lt;/thead>&lt;tbody><text />
<for-each select="//uct:term">
	<variable name="term" select="." />
&lt;tr>
&lt;td><value-of select="uct:cell($term/@name)" />&lt;/td>
&lt;td align="center"><value-of select="uct:cell($term/@abbreviation)" />&lt;/td>
&lt;td><text />
<for-each select="$term/uct:description">
	<call-template name="text-template" />
</for-each>     
&lt;/td><text />
</for-each>
&lt;/tbody>&lt;/table><text />
</template>

</stylesheet>
