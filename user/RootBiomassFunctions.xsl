<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
	<html>
	<body>
	<h1><u>Biomass Root Functions of ForestSimulator</u></h1>

        <p>Table 1: Biomass Functions</p>
        <table border="1">

         <tr><td>Number</td><td>Species</td><td>Code</td>
             <td>Component</td>
             <td>Function</td><td>Region</td>
             <td>Literature</td>
         </tr>
         <xsl:for-each select="RootBiomassFunctions/Function">
         <xsl:sort select="Component"/>

            <tr>
             <td><xsl:value-of select="No"/></td>
             <td><xsl:value-of select="Species"/></td>
             <td><xsl:value-of select="Code"/></td>
             <td><xsl:value-of select="Component"/></td>
             <td><xsl:value-of select="Function"/></td>
             <td><xsl:value-of select="Region"/></td>
             <td><xsl:value-of select="Literature"/></td>
            </tr>

          </xsl:for-each>
       </table>







			
	</body>
	</html>
</xsl:template>

</xsl:stylesheet>
		
