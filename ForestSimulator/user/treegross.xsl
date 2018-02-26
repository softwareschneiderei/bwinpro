<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
	<html>
	<body>
		<table>
			<tr valign="top">
			<td>
				<h1><u>Bestandsdaten</u></h1>
				<table>
					<xsl:for-each select="Bestand">
						<tr>
							<td>
								Name:
							</td>	
							<td>
								<xsl:value-of select="Kennung"/>
							</td>
						</tr>
						<tr>
							<td>
								Bestandsfläche[m²]:
							</td>
							<td>
								<xsl:value-of select="Flaechengroesse_m2"/>
							</td>
						</tr>
						<tr>
							<td>
								Jahr:
							</td>
							<td>
								<xsl:value-of select="AufnahmeJahr"/>
							</td>
						</tr>
						<tr>
							<td>
								Monat:
							</td>
							<td>
								<xsl:value-of select="AufnahmeMonat"/>
							</td>
						</tr>
						<tr>
							<td>
								Datenherkunft:
							</td>
							<td>
								<xsl:value-of select="DatenHerkunft"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<h3>Baumarten</h3>
				<table border="1">
						<tr>
							<td>
								Code
							</td>
							<td>
								lat.Name
							</td>
							<td>
								dt.Name
							</td>
						</tr>
						<xsl:for-each select="Bestand/Baumartencode">
						<tr>
							<td>
								<xsl:value-of select="Code"/>
							</td>
							<td>
								<xsl:value-of select="lateinischerName"/>
							</td>
							<td>
								<xsl:value-of select="deutscherName"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<h3>Eckpunkte</h3>			
				<table border="1">
					<tr>
						<td>
							Nummer
						</td>
						<td>
							rel.x[m]
						</td>
						<td>
							rel.y[m]
						</td>
						<td>
							Bodenhöhe[m]
						</td>
					</tr>
					<xsl:for-each select="Bestand/Eckpunkt">
					<tr>
						<td>
							<xsl:value-of select="Nr"/>
						</td>
						<td>
							<xsl:value-of select="RelativeXKoordinate_m"/>
						</td>
						<td>
							<xsl:value-of select="RelativeYKoordinate_m"/>
						</td>
						<td>
							<xsl:value-of select="RelativeBodenhoehe_m"/>
						</td>
					</tr>
					</xsl:for-each>
				</table>
			</td>	
			<td valign="top">
				<h3>Bäume</h3>
				<table border="1">
					<tr>
						<td>
							Nr.
						</td>
						<td>
							Code
						</td>
						<td>
							Alter
						</td>
						<td>
							BHD[cm]
						</td>
						<td>
							Höhe[m]
						</td>
						<td>
							KA[m]
						</td>
						<td>
							KB[m]
						</td>
						<td>
							X[m]
						</td>
						<td>
							Y[m]
						</td>
						<td>
							Z[m]
						</td>
						<td>
							Si[m]
						</td>
						<td>
							ZB
						</td>
						<td>
							HB
						</td>
						<td>
							Schicht
						</td>
						<td>
							V[m³]
						</td>
						<td>
							VtotH[m³]
						</td>
						<td>
							auss.Jahr
						</td>
						<td>
							Grund
						</td>
						<td>
							Bemerk
						</td>
					</tr>
					<xsl:for-each select="Bestand/Baum">
					<xsl:sort select="Nummer"/>
					<tr>
						<td>
							<xsl:value-of select="Kennung"/>
						</td>
						<td>
							<xsl:value-of select="BaumartcodeLokal"/>
						</td>
						<td>
							<xsl:value-of select="Alter_Jahr"/>
						</td>
						<td>
							<xsl:value-of select="BHD_mR_cm"/>
						</td>
						<td>
							<xsl:value-of select="Hoehe_m"/>
						</td>
						<td>
							<xsl:value-of select="Kronenansatz_m"/>
						</td>
						<td>
							<xsl:value-of select="Kronenbreite_m"/>
						</td>
						<td>
							<xsl:value-of select="RelativeXKoordinate_m"/>
						</td>
						<td>
							<xsl:value-of select="RelativeYKoordinate_m"/>
						</td>
						<td>
							<xsl:value-of select="RelativeBodenhoehe_m"/>
						</td>
						<td>
							<xsl:value-of select="SiteIndex_m"/>
						</td>
						<td>
							<xsl:value-of select="ZBaum"/>
						</td>
						<td>
							<xsl:value-of select="Habitatbaum"/>
						</td>
						<td>
							<xsl:value-of select="Schicht"/>
						</td>
						<td>
							<xsl:value-of select="Volumen_cbm"/>
						</td>
						<td>
							<xsl:value-of select="VolumenTotholz_cbm"/>
						</td>
						<td>
							<xsl:value-of select="AusscheideJahr"/>
						</td>
						<td>
							<xsl:value-of select="GrundDesAusscheidens"/>
						</td>	
						<td>
							<xsl:value-of select="Bemerkung"/>
						</td>	
					</tr>
					</xsl:for-each>
				</table>
			</td>				
			</tr>
		</table>	
			
	</body>
	</html>
</xsl:template>

</xsl:stylesheet>
		
