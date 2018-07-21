<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.w3.org/1999/xhtml">

	<xsl:output method="xml" indent="yes" omit-xml-declaration="yes"
		media-type="application/xhtml+xml" encoding="iso-8859-1"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>

	<xsl:param name="SORTBY">name</xsl:param>

	<xsl:template match="/">
		<html>
			<head>
				<title>Site Map</title>
				<meta http-equiv="content-type" content="application/xhtml+xml; charset=iso-8859-1"/>
			</head>
			<body>
			<div>
				<a href="index.php">Home</a> > Site Map
				<h1>Site Map</h1>
				<p>sort: <a href="?page=sitemap&amp;sortby=type">type</a> | <a href="?page=sitemap&amp;sortby=name">name</a></p>
				<xsl:apply-templates/>
			</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template match="sitemap">
	<ul>
	     <xsl:choose>
	        <xsl:when test="$SORTBY='type'">
             <xsl:apply-templates>
                 <xsl:sort select="type" />
		         </xsl:apply-templates>
          </xsl:when>
          
          <xsl:when test="$SORTBY='date'">
                <xsl:apply-templates>
                    <xsl:sort select="created" />
                </xsl:apply-templates>
          </xsl:when>
                      
          <xsl:otherwise>
              <xsl:apply-templates>
                  <xsl:sort select="name" />
	            </xsl:apply-templates>
	        </xsl:otherwise>
      </xsl:choose>
  </ul>
  
		<p><small>sorting by: <u><xsl:value-of select="$SORTBY"/></u></small></p>
	</xsl:template>

	<xsl:template match="content">
		<li><a href="{@id}"><xsl:apply-templates select="name"/></a> (<xsl:value-of select="type"/>)</li>
	</xsl:template>
	
</xsl:stylesheet>
