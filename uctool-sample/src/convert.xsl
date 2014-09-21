<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns="http://uctool.sf.net/" xmlns:uct="http://uctool.sf.net/" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

	<xsl:template match="@*|node()|text()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|text()" />
		</xsl:copy>
	</xsl:template>

	<!-- data-structure to data -->
	<xsl:template match="uct:data-structure">
		<xsl:element name="data">
			<xsl:apply-templates select="@*|node()|text()" />
		</xsl:element>
	</xsl:template>

	<!-- attribute/@description to sub-element -->
	<xsl:template match="uct:attribute/@description">
		<xsl:element name="{name()}">
			<xsl:value-of select="." />
		</xsl:element>
	</xsl:template>

</xsl:stylesheet>
