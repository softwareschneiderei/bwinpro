<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
<body>

  <h1><u>Bestandsdaten</u></h1>
  <table>
          <xsl:for-each select="Rootbiomass">
	<tr><td>Name:</td>	
	<td><xsl:value-of select="Kennung"/></td></tr>
        <tr><td>Bestandsfläche[m²]:</td>
        <td><xsl:value-of select="Flaechengroesse_m2"/></td></tr>
        <tr><td>Jahr:</td>
	<td><xsl:value-of select="AufnahmeJahr"/></td></tr>
        <tr><td>Monat:</td>
	<td><xsl:value-of select="AufnahmeMonat"/></td></tr>
        <tr><td>Datenherkunft:</td>
	<td><xsl:value-of select="DatenHerkunft"/></td></tr>
	  </xsl:for-each>
   </table>

	<h1><u>Used Functions</u></h1>
	<table border="1">
	  <tr><th>Species</th>	
	      <th>Code</th>	
	      <th>Type</th>	
              <th>Functions</th>
	  </tr>
          <xsl:for-each select="Rootbiomass/Functions">
	  <tr><td><xsl:value-of select="Shortname"/></td>
		<td><xsl:value-of select="Code"/></td>
		<td><xsl:value-of select="Component"/></td>
		<td><xsl:value-of select="Function"/></td>
		</tr>
	  </xsl:for-each>
	</table>


	<h1><u>Root Biomass by Species</u></h1>
	<table border="1">
	  <tr><th>Species</th>	
	      <th>Code</th>	
	      <th>CoarseRoot</th>	
              <th>FineRoot</th>
              <th>SmallRoot</th>
              <th>TotalRoot</th>
	  </tr>
          <xsl:for-each select="Rootbiomass/Species">
	  <tr><td><xsl:value-of select="Shortname"/></td>
		<td><xsl:value-of select="Code"/></td>
		<td><xsl:value-of select="SumCoarseRoots"/></td>
		<td><xsl:value-of select="SumFineRoots"/></td>
		<td><xsl:value-of select="SumSmallRoots"/></td>
		<td><xsl:value-of select="SumTotalRoots"/></td>
		</tr>
	  </xsl:for-each>
	</table>

	<h1><u>Root Biomass of Trees</u></h1>
	<table border="1">
	  <tr><th>Species</th>	
	      <th>Code</th>	
	      <th>Age</th>	
	      <th>DBH</th>	
	      <th>Height</th>	
	      <th>CoarseRoot</th>	
              <th>FineRoot</th>
              <th>SmallRoot</th>
              <th>TotalRoot</th>
	  </tr>
          <xsl:for-each select="Rootbiomass/Tree">
	  <tr><td><xsl:value-of select="Shortname"/></td>
		<td><xsl:value-of select="Code"/></td>
		<td><xsl:value-of select="Age"/></td>
		<td><xsl:value-of select="DBH"/></td>
		<td><xsl:value-of select="Height"/></td>
		<td><xsl:value-of select="CoarseRoots"/></td>
		<td><xsl:value-of select="FineRoots"/></td>
		<td><xsl:value-of select="SmallRoots"/></td>
		<td><xsl:value-of select="TotalRoots"/></td>
		</tr>
	  </xsl:for-each>
	</table>

		
	</body>
	</html>
</xsl:template>

</xsl:stylesheet>
		
