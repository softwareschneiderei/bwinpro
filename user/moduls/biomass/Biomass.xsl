<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">






<xsl:template match="/">
	<html>
	<body>
	<h2><u>Treegross: Baumarteneinstellungen für die Berechnung der Biomasse</u></h2>

 

         <xsl:for-each select="NutrientBalanceSettings/Species">
            <P>Baumart: "<xsl:value-of select="Code"/>" </P>
            <table border="1">
            <tr><th>Element</th><th>Wert</th><th>Typ</th><th>Beschreibung</th></tr>
            <tr><td>Code</td><td><xsl:value-of select="Code"/></td><td>Integer</td><td>Nds. Baumartencode</td></tr>
            <tr><td>SpeciesList</td><td><xsl:value-of select="Specieslist"/></td><td>Text</td><td>gültig für Baumartencodes</td></tr>
            <tr><td>LeafBM</td><td><xsl:value-of select="LeafBM"/></td><td>Double</td><td>Anteil des Reisigs</td></tr>
            <tr><td>WoodDensity</td><td><xsl:value-of select="WoodDensity"/></td><td>Double</td><td>Holzdichte [kg/m³]</td></tr>
            <tr><td>WoodFacC</td><td><xsl:value-of select="WoodFacC"/></td><td>Double</td><td>C Anteil an der Holzbiomasse [kg/t]</td></tr>
            <tr><td>WoodFacN</td><td><xsl:value-of select="WoodFacN"/></td><td>Double</td><td>N Anteil an der Holzbiomasse [kg/t]</td></tr>
            <tr><td>WoodFacS</td><td><xsl:value-of select="WoodFacS"/></td><td>Double</td><td>S Anteil an der Holzbiomasse [kg/t]</td></tr>
            <tr><td>WoodFacP</td><td><xsl:value-of select="WoodFacP"/></td><td>Double</td><td>P Anteil an der Holzbiomasse [kg/t]</td></tr>
            <tr><td>WoodFacK</td><td><xsl:value-of select="WoodFacK"/></td><td>Double</td><td>K Anteil an der Holzbiomasse [kg/t]</td></tr>
            <tr><td>WoodFacCa</td><td><xsl:value-of select="WoodFacCa"/></td><td>Double</td><td>Ca Anteil an der Holzbiomasse [kg/t]</td></tr>
            <tr><td>WoodFacMg</td><td><xsl:value-of select="WoodFacMg"/></td><td>Double</td><td>Mg Anteil an der Holzbiomasse [kg/t]</td></tr>
            <tr><td>WoodFacMn</td><td><xsl:value-of select="WoodFacMn"/></td><td>Double</td><td>Mn Anteil an der Holzbiomasse [kg/t]</td></tr>
            <tr><td>WoodFacFe</td><td><xsl:value-of select="WoodFacFe"/></td><td>Double</td><td>Fe Anteil an der Holzbiomasse [kg/t]</td></tr>
            <tr><td>WoodFacBOup</td><td><xsl:value-of select="WoodFacBOup"/></td><td>Double</td><td>BOup Anteil an der Holzbiomasse </td></tr>
            <tr><td>WoodFacBNH4</td><td><xsl:value-of select="WoodFacBNH4"/></td><td>Double</td><td>BNH4 Anteil an der Holzbiomasse </td></tr>
            <tr><td>WoodFacBN03</td><td><xsl:value-of select="WoodFacBNO3"/></td><td>Double</td><td>BNO3 Anteil an der Holzbiomasse </td></tr>
            <tr><td>BarkDensity</td><td><xsl:value-of select="BarkDensity"/></td><td>Double</td><td>Rindendichte [kg/m³]</td></tr>
            <tr><td>BarkFacC</td><td><xsl:value-of select="BarkFacC"/></td><td>Double</td><td>C Anteil an der Rindenbiomasse [kg/t]</td></tr>
            <tr><td>BarkFacN</td><td><xsl:value-of select="BarkFacN"/></td><td>Double</td><td>N Anteil an der Rindenbiomasse [kg/t]</td></tr>
            <tr><td>BarkFacS</td><td><xsl:value-of select="BarkFacS"/></td><td>Double</td><td>S Anteil an der Rindenbiomasse [kg/t]</td></tr>
            <tr><td>BarkFacP</td><td><xsl:value-of select="BarkFacP"/></td><td>Double</td><td>P Anteil an der Rindenbiomasse [kg/t]</td></tr>
            <tr><td>BarkFacK</td><td><xsl:value-of select="BarkFacK"/></td><td>Double</td><td>K Anteil an der Rindenbiomasse [kg/t]</td></tr>
            <tr><td>BarkFacCa</td><td><xsl:value-of select="BarkFacCa"/></td><td>Double</td><td>Ca Anteil an der Rindenbiomasse [kg/t]</td></tr>
            <tr><td>BarkFacMg</td><td><xsl:value-of select="BarkFacMg"/></td><td>Double</td><td>Mg Anteil an der Rindenbiomasse [kg/t]</td></tr>
            <tr><td>BarkFacMn</td><td><xsl:value-of select="BarkFacMn"/></td><td>Double</td><td>Mn Anteil an der Rindenbiomasse [kg/t]</td></tr>
            <tr><td>BarkFacFe</td><td><xsl:value-of select="BarkFacFe"/></td><td>Double</td><td>Fe Anteil an der Rindenbiomasse [kg/t]</td></tr>
            <tr><td>BarkFacBOup</td><td><xsl:value-of select="BarkFacBOup"/></td><td>Double</td><td>BOup Anteil an der Rindenbiomasse </td></tr>
            <tr><td>BarkFacBNH4</td><td><xsl:value-of select="BarkFacBNH4"/></td><td>Double</td><td>BNH4 Anteil an der Rindenbiomasse </td></tr>
            <tr><td>BarkFacBN03</td><td><xsl:value-of select="BarkFacBNO3"/></td><td>Double</td><td>BNO3 Anteil an der Rindenbiomasse </td></tr>
            <tr><td>BranchDensity</td><td><xsl:value-of select="BranchDensity"/></td><td>Double</td><td>Astdichte</td></tr>
            <tr><td>BranchFacC</td><td><xsl:value-of select="BranchFacC"/></td><td>Double</td><td>C Anteil an der Astbiomasse [kg/t]</td></tr>
            <tr><td>BranchFacN</td><td><xsl:value-of select="BranchFacN"/></td><td>Double</td><td>N Anteil an der Astbiomasse [kg/t]</td></tr>
            <tr><td>BranchFacS</td><td><xsl:value-of select="BranchFacS"/></td><td>Double</td><td>S Anteil an der Astbiomasse [kg/t]</td></tr>
            <tr><td>BranchFacP</td><td><xsl:value-of select="BranchFacP"/></td><td>Double</td><td>P Anteil an der Astbiomasse [kg/t]</td></tr>
            <tr><td>BranchFacK</td><td><xsl:value-of select="BranchFacK"/></td><td>Double</td><td>K Anteil an der Astbiomasse [kg/t]</td></tr>
            <tr><td>BranchFacCa</td><td><xsl:value-of select="BranchFacCa"/></td><td>Double</td><td>Ca Anteil an der Astbiomasse [kg/t]</td></tr>
            <tr><td>BranchFacMg</td><td><xsl:value-of select="BranchFacMg"/></td><td>Double</td><td>Mg Anteil an der Astbiomasse [kg/t]</td></tr>
            <tr><td>BranchFacMn</td><td><xsl:value-of select="BranchFacMn"/></td><td>Double</td><td>Mn Anteil an der Astbiomasse [kg/t]</td></tr>
            <tr><td>BranchFacFe</td><td><xsl:value-of select="BranchFacFe"/></td><td>Double</td><td>Fe Anteil an der Astbiomasse [kg/t]</td></tr>
            <tr><td>BranchFacBOup</td><td><xsl:value-of select="BranchFacBOup"/></td><td>Double</td><td>BOup Anteil an der Astbiomasse </td></tr>
            <tr><td>BranchFacBNH4</td><td><xsl:value-of select="BranchFacBNH4"/></td><td>Double</td><td>BNH4 Anteil an der Astbiomasse </td></tr>
            <tr><td>BranchFacBN03</td><td><xsl:value-of select="BranchFacBNO3"/></td><td>Double</td><td>BNO3 Anteil an der Astbiomasse </td></tr>
            <tr><td>ReisigFacC</td><td><xsl:value-of select="ReisigFacC"/></td><td>Double</td><td>C Anteil an der Reisigbiomasse [kg/t]</td></tr>
            <tr><td>ReisigFacN</td><td><xsl:value-of select="ReisigFacN"/></td><td>Double</td><td>N Anteil an der Reisigbiomasse [kg/t]</td></tr>
            <tr><td>ReisigFacS</td><td><xsl:value-of select="ReisigFacS"/></td><td>Double</td><td>S Anteil an der Reisigbiomasse [kg/t]</td></tr>
            <tr><td>ReisigFacP</td><td><xsl:value-of select="ReisigFacP"/></td><td>Double</td><td>P Anteil an der Reisigbiomasse [kg/t]</td></tr>
            <tr><td>ReisigFacK</td><td><xsl:value-of select="ReisigFacK"/></td><td>Double</td><td>K Anteil an der Reisigbiomasse [kg/t]</td></tr>
            <tr><td>ReisigFacCa</td><td><xsl:value-of select="ReisigFacCa"/></td><td>Double</td><td>Ca Anteil an der Reisigbiomasse [kg/t]</td></tr>
            <tr><td>ReisigFacMg</td><td><xsl:value-of select="ReisigFacMg"/></td><td>Double</td><td>Mg Anteil an der Reisigbiomasse [kg/t]</td></tr>
            <tr><td>ReisigFacMn</td><td><xsl:value-of select="ReisigFacMn"/></td><td>Double</td><td>Mn Anteil an der Reisigbiomasse [kg/t]</td></tr>
            <tr><td>ReisigFacFe</td><td><xsl:value-of select="ReisigFacFe"/></td><td>Double</td><td>Fe Anteil an der Reisigbiomasse [kg/t]</td></tr>
            <tr><td>ReisigFacBOup</td><td><xsl:value-of select="ReisigFacBOup"/></td><td>Double</td><td>BOup Anteil an der Reisigbiomasse </td></tr>
            <tr><td>ReisigFacBNH4</td><td><xsl:value-of select="ReisigFacBNH4"/></td><td>Double</td><td>BNH4 Anteil an der Reisigbiomasse </td></tr>
            <tr><td>ReisigFacBN03</td><td><xsl:value-of select="ReisigFacBNO3"/></td><td>Double</td><td>BNO3 Anteil an der Reisigbiomasse </td></tr>
            <tr><td>LeafFacC</td><td><xsl:value-of select="LeafFacC"/></td><td>Double</td><td>C Anteil an der Blattbiomasse [kg/t]</td></tr>
            <tr><td>LeafFacN</td><td><xsl:value-of select="LeafFacN"/></td><td>Double</td><td>N Anteil an der Blattbiomasse [kg/t]</td></tr>
            <tr><td>LeafFacS</td><td><xsl:value-of select="LeafFacS"/></td><td>Double</td><td>S Anteil an der Blattbiomasse [kg/t]</td></tr>
            <tr><td>LeafFacP</td><td><xsl:value-of select="LeafFacP"/></td><td>Double</td><td>P Anteil an der Blattbiomasse [kg/t]</td></tr>
            <tr><td>LeafFacK</td><td><xsl:value-of select="LeafFacK"/></td><td>Double</td><td>K Anteil an der Blattbiomasse [kg/t]</td></tr>
            <tr><td>LeafFacCa</td><td><xsl:value-of select="LeafFacCa"/></td><td>Double</td><td>Ca Anteil an der Blattbiomasse [kg/t]</td></tr>
            <tr><td>LeafFacMg</td><td><xsl:value-of select="LeafFacMg"/></td><td>Double</td><td>Mg Anteil an der Blattbiomasse [kg/t]</td></tr>
            <tr><td>LeafFacMn</td><td><xsl:value-of select="LeafFacMn"/></td><td>Double</td><td>Mn Anteil an der Blattbiomasse [kg/t]</td></tr>
            <tr><td>LeafFacFe</td><td><xsl:value-of select="LeafFacFe"/></td><td>Double</td><td>Fe Anteil an der Blattbiomasse [kg/t]</td></tr>
            <tr><td>LeafFacBOup</td><td><xsl:value-of select="LeafFacBOup"/></td><td>Double</td><td>BOup Anteil an der Blattbiomasse </td></tr>
            <tr><td>LeafFacBNH4</td><td><xsl:value-of select="LeafFacBNH4"/></td><td>Double</td><td>BNH4 Anteil an der Blattbiomasse </td></tr>
            <tr><td>LeafFacBN03</td><td><xsl:value-of select="LeafFacBNO3"/></td><td>Double</td><td>BNO3 Anteil an der Blattbiomasse </td></tr>
            <tr><td>TaperClass</td><td><xsl:value-of select="TaperClass"/></td><td>Text</td><td>TreeGrOSS Schaftform Klasse in Java</td></tr>
            <tr><td>TaperFunctionNumber</td><td><xsl:value-of select="TaperFunctionNumber"/></td><td>Integer</td><td>Schaftfromfunktionsnummer der TaperClass</td></tr>
            <tr><td>StemVolume</td><td><xsl:value-of select="StemVolume"/></td><td>Text</td><td>Stammvolumen [m³]</td></tr>
            <tr><td>StemBMkg</td><td><xsl:value-of select="StemBMkg"/></td><td>Text</td><td>Stammbiomasse [t]</td></tr>
            <tr><td>BarkBMkg</td><td><xsl:value-of select="BarkBMkg"/></td><td>Text</td><td>Rindenbiomasse [t]</td></tr>
            <tr><td>BranchBMkg</td><td><xsl:value-of select="BranchBMkg"/></td><td>Text</td><td>Astbiomasse [t]</td></tr>
            <tr><td>ReisigBMkg</td><td><xsl:value-of select="ReisigBMkg"/></td><td>Text</td><td>Reisigbiomasse [t]</td></tr>

            </table>
 
          </xsl:for-each>



			
	</body>
	</html>
</xsl:template>

</xsl:stylesheet>
		
