<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>
<body>
<H1>Baumartenschlüssel</H1>
  <Table border="1">
    <TR><TH>Code</TH><TH>Kurz</TH><TH>Name</TH><TH>lat. Name</TH><TH>Einstellung wie</TH> </TR>
    <xsl:for-each select="ForestSimulatorSettings/SpeciesDefinition">
       <TR><TD><xsl:value-of select="Code"/></TD><TD><xsl:value-of select="ShortName"/></TD>
       <TD><xsl:value-of select="LongName"/></TD><TD><xsl:value-of select="LatinName"/></TD>
       <TD><xsl:value-of select="HandledLikeCode"/></TD></TR>
    </xsl:for-each>
  </Table>


<H1>Baumarteneinstellungen</H1>
<xsl:for-each select="ForestSimulatorSettings/SpeciesDefinition">
  <h3>Baumart:  <xsl:value-of select="Code"/> : <xsl:value-of select="LongName"/>   <I> (<xsl:value-of select="LatinName"/>) </I> </h3>
  <Table border="1">
    <TR><TD>Durchmessergenerierung [cm]</TD><TD>t.d = <xsl:value-of select="DiameterDistributionXML"/> </TD></TR>
    <TR><TD>Einheitshöhenkurve [m]</TD><TD>t.h = <xsl:value-of select="UniformHeightCurveXML"/> </TD></TR>
    <TR><TD>Höhenvariabilität [m]</TD><TD>t.hv = <xsl:value-of select="HeightVariation"/> </TD></TR>
    <TR><TD>Volumenfunktion [m³]</TD><TD>t.v = <xsl:value-of select="VolumeFunctionXML"/> </TD></TR>
    <TR><TD>Kronenbreite [m]</TD><TD>t.cw = <xsl:value-of select="Crownwidth"/> </TD></TR>
    <TR><TD>Kronenansatz [m]</TD><TD>t.cb = <xsl:value-of select="Crownbase"/> </TD></TR>
    <TR><TD>Kronentype in Grafik: </TD><TD>Kronentyp= <xsl:value-of select="CrownType"/>  (0=Laubholz, 1= Nadelholz) </TD></TR>
    <TR><TD>Bonität (Höhe im Alter 100) [m]</TD><TD>t.si = <xsl:value-of select="SiteIndex"/> </TD></TR>
    <TR><TD>Höhe entsprechend der Bonität [m] </TD><TD>Höhe = <xsl:value-of select="SiteIndexHeight"/> </TD></TR>
    <TR><TD>Potentieller Höhenzuwachs [m]</TD><TD>ihpot = <xsl:value-of select="PotentialHeightIncrement"/> </TD></TR>
    <TR><TD>Höhenzuwachs [m]</TD><TD>t.hinc = <xsl:value-of select="HeightIncrement"/> </TD></TR>
    <TR><TD>Höhenzuwachsstreuung [m]</TD><TD>herror = <xsl:value-of select="HeightIncrementError"/> </TD></TR>
    <TR><TD>Grundflächenzuwachs [cm²]</TD><TD>t.dinc = <xsl:value-of select="DiameterIncrement"/> </TD></TR>
    <TR><TD>Durchmesserzuwachsstreuung [cm]</TD><TD>herror = <xsl:value-of select="DiameterIncrementError"/> </TD></TR>
    <TR><TD>Maximale Dichte der Grundfläche [m²/ha]</TD><TD>MaxDichte = <xsl:value-of select="MaximumDensity"/> </TD></TR>
    <TR><TD>Maximales Alter [Jahre]</TD><TD>MaxAlter = <xsl:value-of select="MaximumAge"/> </TD></TR>
    <TR><TD>Totholzzersetzung (Faktor)</TD><TD> <xsl:value-of select="Decay"/> </TD></TR>
    <TR><TD>Zielstärkendurchmesser [cm]</TD><TD> <xsl:value-of select="TargetDiameter"/> </TD></TR>
    <TR><TD>Anzahl der Z-Bäume [St/ha]</TD><TD> <xsl:value-of select="CropTreeNumber"/> </TD></TR>
    <TR><TD>Höhe der 1. Durchforstung [m]</TD><TD> <xsl:value-of select="HeightOfThinningStart"/> </TD></TR>
    <TR><TD>Mäßige Durchforstung [m;NB°]</TD><TD> <xsl:value-of select="ModerateThinning"/> </TD></TR>
    <TR><TD>Farbe (RGB)</TD><TD> <xsl:value-of select="Color"/> </TD></TR>
    <TR><TD>Plugin: Einwuchs</TD><TD> <xsl:value-of select="Ingrowth"/> </TD></TR>
    <TR><TD>Plugin: Konkurrenzindex</TD><TD> <xsl:value-of select="Competition"/> </TD></TR>
    <TR><TD>Plugin: Schaftformfunktion</TD><TD> <xsl:value-of select="TaperFunction"/> </TD></TR>
    <TR><TD>Volumenfunktion Schaftholz [m³]</TD><TD> <xsl:value-of select="StemVolumeFunction"/> </TD></TR>

  </Table>
						
</xsl:for-each>
				
	</body>
	</html>
</xsl:template>

</xsl:stylesheet>
		
