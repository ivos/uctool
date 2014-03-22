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
<value-of select="uct:page-start('#requirement.index.page.title#',$ucs-name,'../','req',true())" />
<call-template name="requirement-index-template" />
<value-of select="uct:page-end('../')" />
</template>

<template name="requirement-index-template">

&lt;h2>#requirement.index.page.title#&lt;/h2><text />
<call-template name="req-index-start-template" />
<for-each select="distinct-values($merged-file//uct:requirement/@code)">
	<call-template name="req-index-body-template" />
</for-each>
<call-template name="req-index-end-template" />

</template>

</stylesheet>
