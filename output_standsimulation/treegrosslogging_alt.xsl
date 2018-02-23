<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">






<xsl:template match="/">
	<html>
	<body>
	<h1><u>Nutzungen und Totholz</u></h1>

        <p>Tabelle 1: Verwendete Sortimente</p>
        <table border="1">

         <tr><td>Sortiment</td><td>Art von</td><td>Art bis</td>
             <td>Laenge min</td><td>Laenge max</td>
             <td>D min</td><td>D max</td><td>Zopf min</td><td>Zopf max</td>
         </tr>
         <tr><td></td><td>m</td><td>m</td>
             <td>cm</td><td>cm</td><td>cm</td><td>cm</td>
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
            </tr>

          </xsl:for-each>
       </table>


       <p>Tabelle 2: Übersicht über das Volumen nach Baumart</p>

	<table border="1">
             <tr>
		<td></td><td>Lebend</td><td>Entnahme</td><td>Totholz</td>
             </tr>
             <tr>
		<td>Art</td><td>Volumen</td><td>Volumen</td><td>Volumen</td>
             </tr>
             <tr>
		<td></td><td>m³/ha</td><td>m³/ha</td><td>m³/ha</td>
            </tr>
             <xsl:for-each select="Stoffhaushalt/Art">
             <tr>
               <td><xsl:value-of select="Code"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/v_lebend),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/v_entnommen),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/v_toth),'######.#')"/></td>
             </tr>

             </xsl:for-each>

                <tr>
                   <td>Gesamt</td>
 		   <td><xsl:value-of select="format-number(sum(//v_lebend),'######.#')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//v_entnommen),'######.#')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//v_toth),'######.#')"/></td>
		</tr>
	</table>

       <p>Tabelle 3: Übersicht über die Biomasse nach Baumart</p>

	<table border="1">
             <tr>
                
		<td></td><td COLSPAN="3" > Lebend</td><td COLSPAN="3" >Entnahme</td><td>Totholz </td>
             </tr>
             <tr>
		<td>Art</td><td>Volumen</td><td>Zweige</td><td>Blätter</td><td>Volumen</td>
                 <td>Zweige</td><td>Blätter</td><td>Volumen</td>
             </tr>
             <tr>
		<td></td><td>t/ha</td><td>t/ha</td><td>t/ha</td><td>t/ha</td><td>t/ha</td><td>t/ha</td><td>t/ha</td>
            </tr>
             <xsl:for-each select="Stoffhaushalt/Art">
             <tr>
               <td><xsl:value-of select="Code"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Biomasse_lebend),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Biomasse_Zweig),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Biomasse_Blatt),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Biomasse_entn),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Biomasse_Zweig_ent),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Biomasse_Blatt_ent),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Biomasse_toth),'######.#')"/></td>
             </tr>

             </xsl:for-each>

                <tr>
                   <td>Gesamt</td>
 		   <td><xsl:value-of select="format-number(sum(//Biomasse_lebend),'######.#')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Biomasse_Zweig),'######.###')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Biomasse_Blatt),'######.###')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//Biomasse_entn),'######.#')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Biomasse_Zweig_ent),'######.###')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Biomasse_Blatt_ent),'######.###')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//Biomasse_toth),'######.#')"/></td>
		</tr>
	</table>

       <p>Tabelle 4: Übersicht über die Calciumgehalte nach Baumart</p>

	<table border="1">
             <tr>
                
		<td></td><td COLSPAN="3" > Lebend</td><td COLSPAN="3" >Entnahme</td><td>Totholz </td>
             </tr>
             <tr>
		<td>Art</td><td>Volumen</td><td>Zweige</td><td>Blätter</td><td>Volumen</td>
                 <td>Zweige</td><td>Blätter</td><td>Volumen</td>
             </tr>
             <tr>
		<td></td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td>
            </tr>
             <xsl:for-each select="Stoffhaushalt/Art">
             <tr>
               <td><xsl:value-of select="Code"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Ca_lebend),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Ca_Zweig),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Ca_Blatt),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Ca_entn),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Ca_Zweig_ent),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Ca_Blatt_ent),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Ca_toth),'######.#')"/></td>
             </tr>

             </xsl:for-each>

                <tr>
                   <td>Gesamt</td>
 		   <td><xsl:value-of select="format-number(sum(//Ca_lebend),'######.#')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Ca_Zweig),'######.###')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Ca_Blatt),'######.###')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//Ca_entn),'######.#')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Ca_Zweig_ent),'######.###')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Ca_Blatt_ent),'######.###')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//Ca_toth),'######.#')"/></td>
		</tr>
	</table>

      <p>Tabelle 5: Übersicht über die Magnesiumgehalte nach Baumart</p>

	<table border="1">
             <tr>
                
		<td></td><td COLSPAN="3" > Lebend</td><td COLSPAN="3" >Entnahme</td><td>Totholz </td>
             </tr>
             <tr>
		<td>Art</td><td>Volumen</td><td>Zweige</td><td>Blätter</td><td>Volumen</td>
                 <td>Zweige</td><td>Blätter</td><td>Volumen</td>
             </tr>
             <tr>
		<td></td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td>
            </tr>
             <xsl:for-each select="Stoffhaushalt/Art">
             <tr>
               <td><xsl:value-of select="Code"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Mg_lebend),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Mg_Zweig),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Mg_Blatt),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Mg_entn),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Mg_Zweig_ent),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Mg_Blatt_ent),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/Mg_toth),'######.#')"/></td>
             </tr>

             </xsl:for-each>

                <tr>
                   <td>Gesamt</td>
 		   <td><xsl:value-of select="format-number(sum(//Mg_lebend),'######.#')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Mg_Zweig),'######.###')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Mg_Blatt),'######.###')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//Mg_entn),'######.#')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Mg_Zweig_ent),'######.###')"/></td>
 			<td><xsl:value-of select="format-number(sum(//Mg_Blatt_ent),'######.###')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//Mg_toth),'######.#')"/></td>
		</tr>
	</table>
      <p>Tabelle 6: Übersicht über die Kaliumgehalte nach Baumart</p>

	<table border="1">
             <tr>
                
		<td></td><td COLSPAN="3" > Lebend</td><td COLSPAN="3" >Entnahme</td><td>Totholz </td>
             </tr>
             <tr>
		<td>Art</td><td>Volumen</td><td>Zweige</td><td>Blätter</td><td>Volumen</td>
                 <td>Zweige</td><td>Blätter</td><td>Volumen</td>
             </tr>
             <tr>
		<td></td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td><td>kg/ha</td>
            </tr>
             <xsl:for-each select="Stoffhaushalt/Art">
             <tr>
               <td><xsl:value-of select="Code"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/K_lebend),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/K_Zweig),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/K_Blatt),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/K_entn),'######.#')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/K_Zweig_ent),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/K_Blatt_ent),'######.###')"/></td>
               <td><xsl:value-of select="format-number(sum(Baum/K_toth),'######.#')"/></td>
             </tr>

             </xsl:for-each>

                <tr>
                   <td>Gesamt</td>
 		   <td><xsl:value-of select="format-number(sum(//K_lebend),'######.#')"/></td>
 			<td><xsl:value-of select="format-number(sum(//K_Zweig),'######.###')"/></td>
 			<td><xsl:value-of select="format-number(sum(//K_Blatt),'######.###')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//K_entn),'######.#')"/></td>
 			<td><xsl:value-of select="format-number(sum(//K_Zweig_ent),'######.###')"/></td>
 			<td><xsl:value-of select="format-number(sum(//K_Blatt_ent),'######.###')"/></td>
 		   <td><xsl:value-of select="format-number(sum(//K_toth),'######.#')"/></td>
		</tr>
	</table>


      <p>Tabelle 7: Zeitbedarf der Holzernte</p>

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





        <p>Tabelle 8: Liste der einzelnen Stücke</p>

	<table border="1">
             <tr>
		<td>Art</td><td>Nr</td><td>BHD</td>
		<td>Sortiment</td><td>Jahr</td>
		<td>Typ</td><td>Starthoehe</td>
                <td>Laenge</td><td>Mittendurchm. m.R.</td>
                <td>Volumen m.R.</td><td>Volumen o.R.</td>
             </tr>
             <tr>
		<td></td><td></td><td>cm</td><td></td><td></td>
		<td></td><td>m</td><td>m</td><td>cm</td>
                <td>m³/ha</td><td>m³/ha</td>
             </tr>
             <xsl:for-each select="Stoffhaushalt/Sortiment">
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
             </tr>

             </xsl:for-each>

	</table>




			
	</body>
	</html>
</xsl:template>

</xsl:stylesheet>
		
