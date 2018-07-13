Dim DSI_Projection As Projection
Dim ltmEVariables As EnvironVariables
Dim a5YmEVariables As EnvironVariables

' VariablesType_Defination

Type Regression ' data type of regression
     
   nParameter     As Integer ' the number of parameters
   rxy  As Single  ' correlation coefficient
   iParameter(1 To 10) As Double ' the parameter value

End Type

Type SIDynamic ' Site index

   Year  As Integer
   SI0   As Single ' initial site index
   SIi(100)   As Single ' it. site index ' maximum 100 years
   SIn   As Single ' end site index

End Type

Type EnvironVariables

   TMean As Single ' average growing season temperature
   PSum  As Single ' average growing season precipitation sum
   AI    As Single ' Aridity index
   NOTot As Single ' total annual nitrogen deposition
   
End Type


Type Projection
   
   StandName   As String
   TreeSpecies As String
   
   VegBegin    As Integer
   VegEnd      As Integer
   
   RCP   As String
   Length  As Integer
   
End Type

Sub BasicInfoSpecification()

'basic information

' Stand name
DSI_Projection.StandName = "fi 150/   b"

DSI_Projection.TreeSpecies = "Fi"

' Vegetation Beginning (March)
DSI_Projection.VegBegin = 3

' Vegetation End (August)
DSI_Projection.VegEnd = 8

' RCP -> rcp4.5 and rcp 8.5
DSI_Projection.RCP = "rcp85"

End Sub

Sub DataVariableInput(xt() As Single, xP() As Single, xNO() As Single)

' read climatic and environmental data from database
' vegetation period from March to August

' xT() - Monthly mean Temperature
' xP() - Monthly Precipitation
' xNO()- annual deposition

Dim iYear As Integer
Dim rstModel As New ADODB.Recordset


      sql$ = " SELECT SceTM.Jahr, SceTM.MM1 AS TM1, SceTM.MM2 AS TM2, SceTM.MM3 AS TM3, SceTM.MM4 AS TM4, SceTM.MM5 AS TM5, SceTM.MM6 AS TM6, SceTM.MM7 AS TM7, SceTM.MM8 AS TM8, SceTM.MM9 AS TM9, SceTM.MM10 AS TM10, SceTM.MM11 AS TM11, SceTM.MM12 AS TM12, ScePS.MM1 AS PS1, ScePS.MM2 AS PS2, ScePS.MM3 AS PS3, ScePS.MM4 AS PS4, ScePS.MM5 AS PS5, ScePS.MM6 AS PS6, ScePS.MM7 AS PS7, ScePS.MM8 AS PS8, ScePS.MM9 AS PS9, ScePS.MM10 AS PS10, ScePS.MM11 AS PS11, ScePS.MM12 AS PS12" _
           & " FROM ScePS INNER JOIN SceTM ON (ScePS.Jahr = SceTM.Jahr) AND (ScePS.SCEN = SceTM.SCEN) AND (ScePS.VflName = SceTM.VflName) AND (ScePS.IDVfl = SceTM.IDVfl)" _
           & " Where (((SceTM.VflName) = '" & DSI_Projection.StandName & "') And ((SceTM.SCEN) = '" & DSI_Projection.RCP & "'))" _
           & " GROUP BY SceTM.Jahr, SceTM.MM1, SceTM.MM2, SceTM.MM3, SceTM.MM4, SceTM.MM5, SceTM.MM6, SceTM.MM7, SceTM.MM8, SceTM.MM9, SceTM.MM10, SceTM.MM11, SceTM.MM12, ScePS.MM1, ScePS.MM2, ScePS.MM3, ScePS.MM4, ScePS.MM5, ScePS.MM6, ScePS.MM7, ScePS.MM8, ScePS.MM9, ScePS.MM10, ScePS.MM11, ScePS.MM12;"
  
   
      Set rstModel = RSTOpenReadOnly(DataEnv1.ConndbClimate, sql$)

      iYear = 0
      With rstModel

      If .RecordCount > 0 Then
      
         ReDim xt(12, .RecordCount)
         ReDim xP(12, .RecordCount)
         ReDim xNO(.RecordCount)
         
         .MoveFirst
         Do While Not rstModel.EOF
         
            iYear = iYear + 1
            
             xt(3, iYear) = rstModel("TM3")
             xt(4, iYear) = rstModel("TM4")
             xt(5, iYear) = rstModel("TM5")
             xt(6, iYear) = rstModel("TM6")
             xt(7, iYear) = rstModel("TM7")
             xt(8, iYear) = rstModel("TM8")
             
             xP(3, iYear) = rstModel("PS3")
             xP(4, iYear) = rstModel("PS4")
             xP(5, iYear) = rstModel("PS5")
             xP(6, iYear) = rstModel("PS6")
             xP(7, iYear) = rstModel("PS7")
             xP(8, iYear) = rstModel("PS8")

            
         .MoveNext
      Loop
   End If
   
   .Close
End With

DSI_Projection.Length = iYear

'read annual deposition data from database

'xNO()

End Sub


Sub a5YmEnvironVariablesGet()

'read 5-year mean Temperature and precipitation from database
' calculation of aridity index

Dim sql$
Dim tbName As String
Dim rstModel As New ADODB.Recordset

      sql$ = " SELECT Avg(SceTM.MM3) AS TM3, Avg(SceTM.MM4) AS TM4, Avg(SceTM.MM5) AS TM5, Avg(SceTM.MM6) AS TM6, Avg(SceTM.MM7) AS TM7, Avg(SceTM.MM8) AS TM8, Avg(ScePS.MM3) AS PS3, Avg(ScePS.MM4) AS PS4, Avg(ScePS.MM5) AS PS5, Avg(ScePS.MM6) AS PS6, Avg(ScePS.MM7) AS PS7, Avg(ScePS.MM8) AS PS8" _
           & " FROM ScePS INNER JOIN SceTM ON (ScePS.VflName = SceTM.VflName) AND (ScePS.SCEN = SceTM.SCEN) AND (ScePS.Jahr = SceTM.Jahr)" _
           & " WHERE (((SceTM.Jahr)<=2014) AND ((SceTM.VflName)='" & DSI_Projection.StandName & "'))"

Set rstModel = RSTOpenReadOnly(DataEnv1.ConndbClimate, sql$)

      
      With rstModel

      If .RecordCount > 0 Then
         
         .MoveFirst
         Do While Not rstModel.EOF
         
            a5YmEVariables.TMean = (rstModel("TM3") + rstModel("TM4") + rstModel("TM5") + rstModel("TM6") + rstModel("TM7") + rstModel("TM8")) / 6
            a5YmEVariables.PSum = (rstModel("PS3") + rstModel("PS4") + rstModel("PS5") + rstModel("PS6") + rstModel("PS7") + rstModel("PS8")) / 6
            
            'aridity index
            a5YmEVariables.AI = 12 * a5YmEVariables.PSum / (10 + a5YmEVariables.TMean)
            
         .MoveNext
      Loop
   End If
   
   .Close
End With

' read a 5 year mean deposition from database

a5YmEVariables.NOTot = 100

End Sub
Sub ltmEnvironVariablesGet()

 'Long-term mean of Environmental Variables (Climate and deposition data)

' Long-term mean of Temperature

ltmEVariables.TMean = 0.9859 * a5YmEVariables.TMean - 0.8488

' Long-term mean of precipitation

ltmEVariables.PSum = 1.0799 * a5YmEVariables.PSum - 10.691
' Long-term mean of Aridity

ltmEVariables.AI = 1.1187 * a5YmEVariables.AI - 5.7999

'Long-term mean of deposition

ltmEVariables.NOTot = 0.8947 * a5YmEVariables.NOTot - 4.355
'ltmEVariables.NOTot = -0.00002 * a5YmEVariables.NOTot ^ 3 + 0.0082 * a5YmEVariables.NOTot ^ 2 + 0.0216 * a5YmEVariables.NOTot + 22.562

End Sub
Sub ModelParameterInput(xSIFunction As Regression)

' read model parameters from database

Select Case DSI_Projection.TreeSpecies

   Case "Fi"
   
  
      xSIFunction.iParameter(1) = 1.00003571186121
      xSIFunction.iParameter(2) = 0.99998833695419
      xSIFunction.iParameter(3) = -9.02561907643062E-03
      xSIFunction.iParameter(4) = 4.76764480882665E-04
      xSIFunction.iParameter(5) = -2.57562425678908E-03
      xSIFunction.iParameter(6) = 1.00564825578297E-03
      xSIFunction.iParameter(7) = 1.37988034418277E-02

   
  
   Case "Ta"
   
      xSIFunction.iParameter(1) = 1
      xSIFunction.iParameter(2) = 1
      xSIFunction.iParameter(3) = 1
      xSIFunction.iParameter(4) = 1
      xSIFunction.iParameter(5) = 1
      xSIFunction.iParameter(6) = 1
      xSIFunction.iParameter(7) = 1
   
   Case "Dgl"
      xSIFunction.iParameter(1) = 1
      xSIFunction.iParameter(2) = 1
      xSIFunction.iParameter(3) = 1
      xSIFunction.iParameter(4) = 1
      xSIFunction.iParameter(5) = 1
      xSIFunction.iParameter(6) = 1
      xSIFunction.iParameter(7) = 1
      
      
   Case "Bu"


      xSIFunction.iParameter(1) = 1
      xSIFunction.iParameter(2) = 1
      xSIFunction.iParameter(3) = 1
      xSIFunction.iParameter(4) = 1
      xSIFunction.iParameter(5) = 1
      xSIFunction.iParameter(6) = 1
      xSIFunction.iParameter(7) = 1
      
   
   Case "Kie"
   

      xSIFunction.iParameter(1) = 1
      xSIFunction.iParameter(2) = 1
      xSIFunction.iParameter(3) = 1
      xSIFunction.iParameter(4) = 1
      xSIFunction.iParameter(5) = 1
      xSIFunction.iParameter(6) = 1
      xSIFunction.iParameter(7) = 1
      
   
   Case "Ei"
      xSIFunction.iParameter(1) = 1
      xSIFunction.iParameter(2) = 1
      xSIFunction.iParameter(3) = 1
      xSIFunction.iParameter(4) = 1
      xSIFunction.iParameter(5) = 1
      xSIFunction.iParameter(6) = 1
      xSIFunction.iParameter(7) = 1

End Select


End Sub
Sub ClimateDataCombination(xt() As Single, xP() As Single, xNO() As Single, zEVariables() As EnvironVariables)

ReDim zEVariables(DSI_Projection.Length)

' combination of temperature and precipitation ( March - August)

For iYear = 1 To DSI_Projection.Length
      
   zEVariables(iYear).TMean = 0
   zEVariables(iYear).PSum = 0
   
   For iMonth = DSI_Projection.VegBegin To DSI_Projection.VegEnd
      zEVariables(iYear).TMean = zEVariables(iYear).TMean + xt(iMonth, iYear)
      zEVariables(iYear).PSum = zEVariables(iYear).PSum + xP(iMonth, iYear)
   Next iMonth
   
   zEVariables(iYear).TMean = zEVariables(iYear).TMean / 6
   zEVariables(iYear).PSum = zEVariables(iYear).PSum / 6

'   Aridity index
   zEVariables(iYear).AI = 12 * zEVariables(iYear).PSum / (zEVariables(iYear).TMean + 10)

'  annual deposition
   zEVariables(iYear).NOTot = xNO(iYear)
      
Next iYear

End Sub
Sub DataAccumulation(yCum() As Single)

Dim nLag As Integer
Dim iYear As Integer
Dim jYear As Integer
Dim dYear As Integer
Dim sYear As Integer
Dim Periodl As Integer

ReDim mCum(DSI_Projection.Length) As Single

nLag = 5
       
For iYear = 1 To DSI_Projection.Length

   If iYear <= nLag Then
      Periodl = iYear
   Else
      Periodl = nLag
   End If
                    
   sYear = 0
   'mCum(iYear) = 0
   
   For jYear = 1 To Periodl
      
      dYear = iYear - jYear + 1
      If dYear > 0 Then
         sYear = sYear + dYear
         mCum(iYear) = mCum(iYear) + dYear * yCum(dYear)
      End If
   Next jYear
   
   If sYear > 0 Then
      mCum(iYear) = mCum(iYear) / sYear
   End If
Next iYear

ReDim yCum(DSI_Projection.Length)
yCum = mCum

Erase mCum

End Sub

Sub ClimateDataAccum(xClimateVariables() As EnvironVariables)

Dim nLag As Integer
Dim iYear As Integer
Dim jYear As Integer
Dim dYear As Integer
Dim sYear As Integer
Dim Periodl As Integer
Dim xCum() As Single


' Temperature
ReDim xCum(DSI_Projection.Length)

For iYear = 1 To DSI_Projection.Length
   xCum(iYear) = xClimateVariables(iYear).TMean
Next iYear
Call DataAccumulation(xCum())

For iYear = 1 To DSI_Projection.Length
   xClimateVariables(iYear).TMean = xCum(iYear)
Next iYear

' Precipitation
ReDim xCum(DSI_Projection.Length)

For iYear = 1 To DSI_Projection.Length
   xCum(iYear) = xClimateVariables(iYear).PSum
Next iYear
Call DataAccumulation(xCum())

For iYear = 1 To DSI_Projection.Length
   xClimateVariables(iYear).PSum = xCum(iYear)
Next iYear

' Ariditiy
ReDim xCum(DSI_Projection.Length)

For iYear = 1 To DSI_Projection.Length
   xCum(iYear) = xClimateVariables(iYear).AI
Next iYear
Call DataAccumulation(xCum())

For iYear = 1 To DSI_Projection.Length
   xClimateVariables(iYear).AI = xCum(iYear)
Next iYear


End Sub
Sub EnvironVariablestandardization(xEVar() As EnvironVariables)

Dim iYear As Integer

' data standardization

' calculation of the 5-year mean of environmental variables
Call a5YmEnvironVariablesGet

' the long-term mean of environmental variables
Call ltmEnvironVariablesGet


For iYear = 1 To DSI_Projection.Length

   xEVar(iYear).TMean = (xEVar(iYear).TMean - ltmEVariables.TMean) / ltmEVariables.TMean
   xEVar(iYear).PSum = (xEVar(iYear).PSum - ltmEVariables.PSum) / ltmEVariables.PSum
   xEVar(iYear).AI = (xEVar(iYear).AI - ltmEVariables.AI) / ltmEVariables.AI
   
   xEVar(iYear).NOTot = (xEVar(iYear).NOTot - ltmEVariables.NOTot) / ltmEVariables.NOTot
   'xEVar(iYear).SO = (xEVar(iYear).SO - ltmEVariables.SO) / ltmEVariables.SO
   
Next iYear

End Sub

Sub VariablesPreTreatment(xt() As Single, xP() As Single, xNO() As Single, zEVaraibles() As EnvironVariables)

' Mulit-step PreTreatment of environmental variables

' xT() - Monthly mean Temperature
' xP() - Monthly Precipitation
' xNO() - annual deposition

' data combination
Call ClimateDataCombination(xt(), xP(), xNO(), zEVaraibles())

' data accumulation
Call ClimateDataAccum(zEVaraibles())

' data standardization
Call EnvironVariablestandardization(zEVaraibles())


End Sub
Function InitialSiteIndexInput() As Single

' read Initial Site Index from database

InitialSiteIndexInput = 39.80825
   

End Function

Sub SiteIndexRecursiveProjection(DSIModel As Regression, zDSIVariables() As EnvironVariables, DSI As SIDynamic)


Dim iYear As Integer
Dim itSI0 As Single

itSI0 = DSI.SI0
For iYear = 1 To DSI_Projection.Length
   
   DSI.SIi(iYear) = DSIModel.iParameter(1) * itSI0 ^ DSIModel.iParameter(2) * Exp(DSIModel.iParameter(3) * zDSIVariables(iYear).TMean + DSIModel.iParameter(4) * zDSIVariables(iYear).PSum + DSIModel.iParameter(5) * zDSIVariables(iYear).AI + DSIModel.iParameter(6) * zDSIVariables(iYear).NOTot + DSIModel.iParameter(7) * zDSIVariables(iYear).PSum * zDSIVariables(iYear).NOTot)
      
   itSI0 = DSI.SIi(iYear)
   
   If iYear = DSI_Projection.Length Then
      DSI.SIn = DSI.SIi(iYear)
   End If
   
Next iYear
   
End Sub


Sub SiteIndexProjectionOutput(DSIVariables() As EnvironVariables, DSI As SIDynamic)

' save the site index changes in the database

End Sub
Sub DynamicSiteIndexMain()

' short VB-Code for the dynamic environment-sennstive Site index model

' variables defination

Dim xt() As Single ' xT() - Monthly mean Temperature
Dim xP() As Single ' xP() - Monthly Precipitation
Dim xNO() As Single ' xNO()- annual deposition


Dim DSI As SIDynamic
Dim DSI_Function As Regression
Dim DSI_EnvironVariables() As EnvironVariables

'
Call BasicInfoSpecification

' read climatic and environmental data
Call DataVariableInput(xt(), xP(), xNO())

' Mulit-step PreTreatment of environmental variables
Call VariablesPreTreatment(xt(), xP(), xNO(), DSI_EnvironVariables())

' read model parameters
Call ModelParameterInput(DSI_Function)

' read initial Site index
DSI.SI0 = InitialSiteIndexInput()

' recursive projection
Call SiteIndexRecursiveProjection(DSI_Function, DSI_EnvironVariables(), DSI)
   
' results output
Call SiteIndexProjectionOutput(DSI_EnvironVariables(), DSI)

End Sub
