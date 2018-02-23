<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">






<xsl:template match="/">
	<html>
	<body>
	<h1><u>Nutzungen und Totholz</u></h1>

        <p><b>Tabelle 1: Verwendete Sortimente</b></p>
        <table border="1">

         <tr><td>Sortiment</td><td>Art von</td><td>Art bis</td>
             <td>Laenge min</td><td>Laenge max</td>
             <td>D min</td><td>D max</td><td>Zopf min</td><td>Zopf max</td>
             <td>Zugabe</td><td>Zugabe</td>
         </tr>
         <tr><td></td><td></td><td></td><td>m</td><td>m</td>
             <td>cm</td><td>cm</td><td>cm</td><td>cm</td>
             <td>%</td><td>cm</td>
         </tr>
         <xsl:for-each select="Stoffhaushalt/Sortiment_gesucht">
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
             <td><xsl:value-of select="ZugP"/></td>
             <td><xsl:value-of select="ZugCm"/></td>
            </tr>

          </xsl:for-each>
       </table>


       <p><b>Tabelle 2: Übersicht über das Volumen nach Jahr und Baumart.</b> </p>

	<table border="1">
             <tr>
		<td></td><td></td><td>Lebend</td><td>Entnahme</td><td>Totholz</td>
             </tr>
             <tr>
		<td>Jahr</td><td>Art</td><td>Volumen</td><td>Volumen</td><td>Volumen</td>
             </tr>
             <tr>
		<td></td><td></td><td>m³/ha</td><td>m³/ha</td><td>m³/ha</td>
            </tr>
             <xsl:for-each select="Stoffhaushalt/Jahr/Art">
             <tr>
               <td><xsl:value-of select="../Year"/></td>
               <td><xsl:value-of select="Code"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/v_lebend),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/v_entnommen),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/v_toth),'######.#')"/></td>
             </tr>

             </xsl:for-each>
                <tr>
                   <td><xsl:value-of select="../Year"/></td>
                   <td>Gesamt</td>
 		   <td><xsl:value-of select="format-number(sum(//v_lebend),'######.#')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//v_entnommen),'######.#')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//v_toth),'######.#')"/></td>
		</tr>
	</table>
      <p> Legende und Hinweise: Lebend = Lebende Bäume im Bestand; Entnahme = Volumen der entnommenen Sortimente inklusive Zugabe und Rinde; Totholz = nicht entnommenes Restderbholz plus Stubben. Trennschnitte werden nicht berücksichtigt. Höhe des Stubbens wird aus der Starthöhe ersichtlich (Tab.4).</p>
 


      <p><b>Tabelle 3: Zeitbedarf der Holzernte</b></p>

	<table border="1">
             <tr>
		<td>Zeitbedarf</td>
             </tr>
             <tr>
		<td>min/ha</td>
             </tr>
             <xsl:for-each select="Stoffhaushalt">
             <tr>
               <td><xsl:value-of select="format-number(sum(Sortiment/Zeitbedarf_minha),'#########.#')"/></td>
             </tr>
             </xsl:for-each>


	</table>





        <p><b>Tabelle 4: Liste der entnommen Sortimente geordnet nach Jahr und Baumart</b></p>

	<table border="1">
             <tr>
		<td>Art</td><td>Nr</td><td>BHD</td>
		<td>Sortiment</td><td>Jahr</td>
		<td>Typ</td><td>Starthoehe</td>
                <td>Laenge</td><td>Mittendurchm. m.R.</td>
                <td>Vol Huber m.R.</td><td>Vol Huber o.R.</td>
                <td>Vol +Zugabe m.R.</td><td>Vol + Zugabe o.R.</td>
             </tr>
             <tr>
		<td></td><td></td><td>cm</td><td></td><td></td>
		<td></td><td>m</td><td>m</td><td>cm</td>
                <td>m³/ha</td><td>m³/ha</td>
                <td>m³/ha</td><td>m³/ha</td>
             </tr>

             <xsl:for-each select="Stoffhaushalt/Jahr/Art/Sortiment">
             <tr>
               <td><xsl:value-of select="Art"/></td>
               <td><xsl:value-of select="Baum_Nr"/></td>
               <td><xsl:value-of select="BHD"/></td>
               <td><xsl:value-of select="Name"/></td>
               <td><xsl:value-of select="Entnahmejahr"/></td>
               <td><xsl:value-of select="Entnahmetyp"/></td>
               <td><xsl:value-of select="format-number(Starthoehe,'####.#')"/></td>
               <td><xsl:value-of select="format-number(Laenge,'####.#')"/></td>
               <td><xsl:value-of select="format-number(D_mR,'####.#')"/></td>
               <td><xsl:value-of select="format-number(VolHuber_mR,'####.##')"/></td>
               <td><xsl:value-of select="format-number(VolHuber_oR,'####.##')"/></td>
               <td><xsl:value-of select="format-number(VoluZug_mR,'####.##')"/></td>
               <td><xsl:value-of select="format-number(VoluZug_oR,'####.##')"/></td>             </tr>

             </xsl:for-each>
 

	</table>
      <p> Legende und Hinweise: Entnahmetyp 2 = Durchforstung, 3 = Zielstärkennutzung; Starthöhe = Beginn des Sortiments bei Baumhöhe; Länge = Länge des Sortiments ohne Zugabe (SoZ); Mittendurchm m.R. = Mittendurchmesser mit Rinde des SoZ; Vol Huber m.R. = Volumen nach Huber mit Rinde des SoZ; Vol Huber o.R. = Volumen nach Huber ohne Rinde des SoZ; Vol + Zugabe m.R. = Volumen nach Schaftformfunktion mit Rinde des Sortiments mit Zugabe; Vol + Zugabe o.R. = Volumen nach Schaftformfunktion ohne Rinde des Sortiments mit Zugabe</p>




			
	</body>
	</html>
</xsl:template>

</xsl:stylesheet>
		
