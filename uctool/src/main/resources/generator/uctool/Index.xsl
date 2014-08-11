<?xml version="1.0" encoding="UTF-8"?>
<stylesheet xmlns="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:uct="http://uctool.sf.net/"
	version="2.0">
<import href="functions/UctTemplates.xsl" />
<output method="text" />

<param name="ucs-name" />
<param name="ucs-version" />
<param name="ucs-description" />

<variable name="merged-file" select="/" />
<variable name="link-type" select="'multi'" />

<template match="/">
<value-of select="uct:page-start('#index.page.title#',$ucs-name,'','home',false())" />
&lt;h1>#index.page.title#&lt;/h1>
&lt;p class="lead">#ucs.name#: <value-of select="$ucs-name" />&lt;br/>
#ucs.version#: <value-of select="$ucs-version" />&lt;/p>
&lt;p class="lead"><value-of select="$ucs-description" />&lt;/p>
&lt;p>#entry.point#&lt;/p>
&lt;div class="offset1">#start.here# &lt;i class="icon-arrow-right">&lt;/i> &amp;nbsp;
	&lt;a class="btn btn-success btn-large" href="summary/entry-point-list.html">#entry.point.list.page.title#
 &amp;nbsp; &lt;i class="icon-chevron-right">&lt;/i>&lt;/a>&lt;/div>
&lt;h3>#sections#&lt;/h3>
&lt;ul class="nav nav-tabs nav-stacked">
&lt;li>&lt;a href="summary/index.html">&lt;i class="icon-sitemap">&lt;/i> #overview.pages#&lt;/a>&lt;/li><text/>
<if test="//uct:actor">
&lt;li>&lt;a href="actor/index.html">&lt;i class="icon-user">&lt;/i> #actors#&lt;/a>&lt;/li><text/>
</if>
<if test="//uct:use-case">
&lt;li>&lt;a href="uc/index.html">&lt;i class="icon-file">&lt;/i> #use.cases#&lt;/a>&lt;/li><text/>
</if>
<if test="//uct:data">
&lt;li>&lt;a href="data/index.html">&lt;i class="icon-table">&lt;/i> #data.structures#&lt;/a>&lt;/li><text/>
</if>
<if test="//uct:requirement">
&lt;li>&lt;a href="req/index.html">&lt;i class="icon-list">&lt;/i> #requirements#&lt;/a>&lt;/li><text/>
</if>
<if test="//uct:attachment-group">
&lt;li>&lt;a href="attachment/index.html">&lt;i class="icon-picture">&lt;/i> #attachments#&lt;/a>&lt;/li><text/>
</if>
<if test="//uct:term">
&lt;li>&lt;a href="glossary/index.html">&lt;i class="icon-search">&lt;/i> #glossary#&lt;/a>&lt;/li><text/>
</if>
&lt;/ul>
&lt;div>&lt;a class="btn" href="single/index.html">&lt;i class="icon-print">&lt;/i> #single.page#&lt;/a>&lt;/div>
&lt;hr/>
&lt;a href="../index.html">&lt;i class="icon-chevron-left">&lt;/i> #back.to.project#&lt;/a><text/>
<value-of select="uct:page-end('')" />
</template>

</stylesheet>
