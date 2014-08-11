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
<value-of select="uct:page-start('#data.index.page.title#',$ucs-name,'../','data',true())" />
<call-template name="data-index-template" />
<value-of select="uct:page-end('../')" />
</template>

<template name="data-index-template">

&lt;h2>#data.index.page.title#&lt;/h2><text />
<call-template name="data-index-start-template" />
<for-each select="$merged-file//uct:data">
	<call-template name="data-index-data-template" />
</for-each>
<call-template name="data-index-end-template" />
</template>

</stylesheet>
