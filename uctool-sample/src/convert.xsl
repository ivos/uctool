<?xml version="1.0" encoding="UTF-8"?>
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/" version="2.0">

	<template match="@*|node()|text()">
		<copy>
			<apply-templates select="@*|node()|text()" />
		</copy>
	</template>

	<template match="//uct:data-structure">
		<copy>
			<apply-templates select="@*|node()|text()" />
		</copy>
	</template>

	<!-- <template match="@*|node()|text()"> <copy> <apply-templates select="@*|node()|text()" mode="copy" /> </copy> </template> 
		<template match="@*|node()|text()" mode="copy"> < ! - - Copy template - - > <copy> <apply-templates select="@*|node()|text()" 
		mode="copy" /> </copy> </template> -->

</stylesheet>
