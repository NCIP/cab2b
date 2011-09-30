<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:UML = 'org.omg.xmi.namespace.UML' 
                version="1.0"
                exclude-result-prefixes="#default">

<!-- Copy Entire XML-->
 <xsl:template match="*|@*|comment()|processing-instruction()|text()">
    <xsl:copy>
      <xsl:apply-templates select="*|@*|comment()|processing-instruction()|text()"/>
    </xsl:copy>
  </xsl:template>
  
<!--XMI Version -->
<!--Change XMI version to 1.1 if 1.2 -->
<xsl:template match="XMI/@xmi.version[.=1.2]">
	<xsl:attribute name="xmi.version">
		<xsl:value-of select="1.1"/>
          </xsl:attribute>
    </xsl:template>

<!--END XMI Version-->

<!--Tagged Values-->
<xsl:template match='UML:TaggedValue'>
       <xsl:variable name="tagname"  select='@name'/>
      <xsl:for-each select="UML:TaggedValue.dataValue[position()=1]">
      
	 <xsl:variable name="tagvalue" select='text()'/>
     		<UML:TaggedValue tag='{$tagname}' value = '{$tagvalue}' />	
	</xsl:for-each>
</xsl:template>

<!--Datatypes-->

<xsl:template match='UML:StructuralFeature.type/UML:DataType'>
      <xsl:variable name="idref"  select='@xmi.idref'/>
      	<UML:Classifier xmi.idref="{$idref}"/>
</xsl:template>



<!--Stereotypes -->
<!--Assign names to stereotypes-->
<xsl:variable name="stereotypes" select="XMI/XMI.content/UML:Model/UML:Namespace.ownedElement/UML:Stereotype"/>

<!--Match when stereotype reference found -->
<xsl:template match='UML:ModelElement.stereotype/UML:Stereotype'>
  <xsl:variable name="stereoTypeId" select="@xmi.idref"/>
     <xsl:for-each select="$stereotypes">
      <xsl:variable name="id" select="@xmi.id"/>
      <xsl:variable name="name" select="@name"/>
       <xsl:if test="$stereoTypeId = $id">
      		 <UML:Stereotype name="{$name}" />
      	</xsl:if>
    </xsl:for-each>
</xsl:template>
<!--Remove stereotype declaration tags-->
 <xsl:template match="UML:Namespace.ownedElement/UML:Stereotype" />

<!--Association Multiplicity attribute-->

<xsl:template match="UML:AssociationEnd">
	<xsl:variable name="lowerMultRange"  select='UML:AssociationEnd.multiplicity/UML:Multiplicity/UML:Multiplicity.range/UML:MultiplicityRange/@lower'/>
	<xsl:variable name="higherMultRange"  select='UML:AssociationEnd.multiplicity/UML:Multiplicity/UML:Multiplicity.range/UML:MultiplicityRange/@upper'/>
	<xsl:variable name="participantIdRef"  select='UML:AssociationEnd.participant/UML:Class/@xmi.idref'/>
    <xsl:copy>
       
    <!-- Multiplicity-->
      <xsl:attribute name="multiplicity">
      <xsl:choose>
      	<xsl:when test="$lowerMultRange = $higherMultRange">
      		<xsl:value-of select="$lowerMultRange"/>
      	</xsl:when> 	
      	<xsl:when test="$higherMultRange >1">
	      	<xsl:value-of select="concat($lowerMultRange,'..','*')"/>
      	</xsl:when> 	
      	<xsl:otherwise> 
      		<xsl:value-of select="concat($lowerMultRange,'..',$higherMultRange)"/>
      	</xsl:otherwise> 
        </xsl:choose>	
      </xsl:attribute>
    <!--participant id-->  
      <xsl:attribute name="type">
             <xsl:value-of select="$participantIdRef"/>
      </xsl:attribute>
      
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
</xsl:template>

 
 <!--Participant end for associations to be removed-->
 <xsl:template match="UML:AssociationEnd.participant" />

</xsl:stylesheet>

