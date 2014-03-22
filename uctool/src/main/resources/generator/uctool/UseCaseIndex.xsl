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
<value-of select="uct:page-start('#use.case.index.page.title#',$ucs-name,'../','uc',true())" />
<call-template name="use-case-index-template" />
<value-of select="uct:page-end('../')" />
</template>

<template name="use-case-index-template">

&lt;h2>#use.case.index.page.title#&lt;/h2><text />
<call-template name="use-case-index-start-template" />
<for-each select="$merged-file//uct:use-case">
	<call-template name="use-case-index-uc-template" />
</for-each>
<call-template name="use-case-index-end-template" />
</template>

</stylesheet>
