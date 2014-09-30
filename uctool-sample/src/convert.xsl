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

	<!-- attribute-ref to value -->
	<xsl:template match="uct:attribute-ref">
		<xsl:element name="value">
			<xsl:apply-templates select="@*|node()|text()" />
		</xsl:element>
	</xsl:template>

	<!-- attribute-ref/@type to of -->
	<xsl:template match="uct:attribute-ref/@type">
		<xsl:attribute name="of">
		<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>

	<!-- attribute-ref/@description to sub-element -->
	<xsl:template match="uct:attribute-ref/@description">
		<xsl:element name="{name()}">
			<xsl:value-of select="." />
		</xsl:element>
	</xsl:template>

</xsl:stylesheet>
