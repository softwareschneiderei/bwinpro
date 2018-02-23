<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">






<xsl:template match="/">
	<html>
	<body>
	<h1><u>Totholzbericht</u></h1>

        <p>Tabelle 1: Verwendete Sortimente</p>
        <table border="1">

         <tr><td>Sortiment</td><td>Art von</td><td>Art bis</td>
             <td>Laenge min</td><td>Laenge max</td>
             <td>D min</td><td>D max</td><td>Zopf min</td><td>Zopf max</td>
             <td>Entnahme</td>
         </tr>
         <tr><td></td><td></td><td></td>
             <td>m</td><td>m</td><td>cm</td><td>cm</td><td>cm</td><td>cm</td><td></td>
         </tr>
         <xsl:for-each select="Totholz/Sortiment_gesucht">
            <tr>
             <td><xsl:value-of select="Code"/></td>
             <td><xsl:value-of select="Art_von"/></td>
             <td><xsl:value-of select="Art_bis"/></td>
             <td><xsl:value-of select="L_min"/></td>
             <td><xsl:value-of select="L_max"/></td>
             <td><xsl:value-of select="D_min"/></td>
             <td><xsl:value-of select="D_max"/></td>
             <td><xsl:value-of select="T_min"/></td>
             <td><xsl:value-of select="T_max"/></td>
             <td><xsl:value-of select="removed"/></td>
            </tr>

          </xsl:for-each>
       </table>


       <p>Tabelle 2: Übersicht über das Totholz nach Jahren und</p>

	<table border="1">
             <tr>
		<td>Jahr</td><td>Art</td><td>Stehend</td><td>Stubben</td>
                <td>Naturschutz</td><td>Totholz</td>
             </tr>
             <tr>
		<td></td><td></td><td>BHD>20cm</td><td>0-30cm</td>
                <td>dm>20cm</td><td>Gesamt >7cm</td>
             </tr>
              <tr>
		<td></td><td></td><td>m³/ha</td><td>m³/ha</td>
                <td>m³/ha</td><td>m³/ha</td>
             </tr>
             <xsl:for-each select="Totholz/Year">
             <tr>
               <td><xsl:value-of select="Jahr"/></td>
               <td><xsl:value-of select="Code"/></td>
               <td><xsl:value-of select="format-number(Stehend,'######.#')"/></td>
               <td><xsl:value-of select="format-number(Stubben,'######.#')"/></td>
               <td><xsl:value-of select="format-number(Naturschutz,'######.#')"/></td>
               <td><xsl:value-of select="format-number(Gesamt,'######.#')"/></td>
             </tr>
             </xsl:for-each>
 	</table>

        <p>Erstellt mit Tree Growth Open Source Software (TreeGrOSS) <A href="http://www.nw-fva.de">http://www.nw-fva.de </A></p>



			
	</body>
	</html>
</xsl:template>

</xsl:stylesheet>
		
