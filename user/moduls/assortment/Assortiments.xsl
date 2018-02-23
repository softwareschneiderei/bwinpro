<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">






<xsl:template match="/">
	<html>
	<body>
	<h1><u>Treegross: Definierte und verwendbare Sortimente</u></h1>

        <p>Tabelle 1: Verwendete Sortimente</p>
        <table border="1">

         <tr><td>Sortiment</td><td>Art von</td><td>Art bis</td>
             <td>Laenge min</td><td>Laenge max</td>
             <td>D min</td><td>D max</td><td>Zopf min</td><td>Zopf max</td>
             <td>Gewicht</td><td>Preis</td><td>Wahrscheinlichkeit</td><td>nur ZBaum</td><td>mehrfach</td>
             <td>bis KA</td><td>ausgewaehlt</td><td>Zugabe</td><td>Zugabe</td><td>Zeitbedarfsfunktion</td>
         </tr>
         <tr><td></td><td></td><td></td><td>m</td><td>m</td>
             <td>cm</td><td>cm</td><td>cm</td><td>cm</td><td></td><td>EUR/m³</td>
             <td></td><td></td><td></td><td></td><td></td><td>cm</td><td>%</td><td></td>
         </tr>
         <xsl:for-each select="Sortimente/Sortiment">
            <tr>
             <td><xsl:value-of select="Name"/></td>
             <td><xsl:value-of select="Art_von"/></td>
             <td><xsl:value-of select="Art_bis"/></td>
             <td><xsl:value-of select="minH"/></td>
             <td><xsl:value-of select="maxH"/></td>
             <td><xsl:value-of select="minD"/></td>
             <td><xsl:value-of select="maxD"/></td>
             <td><xsl:value-of select="minTop"/></td>
             <td><xsl:value-of select="Gewicht"/></td>
             <td><xsl:value-of select="Preis"/></td>
             <td><xsl:value-of select="Wahrscheinlichkeit"/></td>
             <td><xsl:value-of select="nurZBaum"/></td>
             <td><xsl:value-of select="mehrfach"/></td>
             <td><xsl:value-of select="Entnahme"/></td>
             <td><xsl:value-of select="bisKA"/></td>
             <td><xsl:value-of select="ausgewaehlt"/></td>
             <td><xsl:value-of select="ZugabeCm"/></td>
             <td><xsl:value-of select="ZugabeProzent"/></td>
             <td><xsl:value-of select="Zeitbedarfsfunktion"/></td>
            </tr>

          </xsl:for-each>
       </table>



			
	</body>
	</html>
</xsl:template>

</xsl:stylesheet>
		
