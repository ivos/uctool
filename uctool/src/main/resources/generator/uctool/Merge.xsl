<?xml version="1.0" encoding="UTF-8"?>
<stylesheet
	xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<import href="functions/UctFunctions.xsl" />
<output method="xml" indent="yes" />

<param name="source-file-names" />
<variable name="names-sequence" select="tokenize($source-file-names,'\|')" />
<variable name="cfg-files" select="document($names-sequence)" />

<template match="/root">
	<copy>
		<apply-templates select="$cfg-files/*" mode="copy" />
	</copy>
</template>

</stylesheet>
