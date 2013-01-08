/*L
 * Copyright Georgetown University.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cab2b/LICENSE.txt for details.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.08.23 at 10:44:28 GMT+05:30 
//


package edu.wustl.common.cde.xml.impl;

public class XMLCDEImpl implements edu.wustl.common.cde.xml.XMLCDE, com.sun.xml.bind.JAXBObject, edu.wustl.common.cde.xml.impl.runtime.UnmarshallableObject, edu.wustl.common.cde.xml.impl.runtime.XMLSerializable, edu.wustl.common.cde.xml.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _XMLPermissibleValues;
    protected boolean has_Cache;
    protected boolean _Cache;
    protected java.lang.String _Name;
    protected java.lang.String _PublicId;
    protected boolean has_LazyLoading;
    protected boolean _LazyLoading;
    public final static java.lang.Class version = (edu.wustl.common.cde.xml.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.wustl.common.cde.xml.XMLCDE.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getXMLPermissibleValues() {
        if (_XMLPermissibleValues == null) {
            _XMLPermissibleValues = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _XMLPermissibleValues;
    }

    public java.util.List getXMLPermissibleValues() {
        return _getXMLPermissibleValues();
    }

    public boolean isCache() {
        return _Cache;
    }

    public void setCache(boolean value) {
        _Cache = value;
        has_Cache = true;
    }

    public java.lang.String getName() {
        return _Name;
    }

    public void setName(java.lang.String value) {
        _Name = value;
    }

    public java.lang.String getPublicId() {
        return _PublicId;
    }

    public void setPublicId(java.lang.String value) {
        _PublicId = value;
    }

    public boolean isLazyLoading() {
        return _LazyLoading;
    }

    public void setLazyLoading(boolean value) {
        _LazyLoading = value;
        has_LazyLoading = true;
    }

    public edu.wustl.common.cde.xml.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.wustl.common.cde.xml.impl.runtime.UnmarshallingContext context) {
        return new edu.wustl.common.cde.xml.impl.XMLCDEImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.wustl.common.cde.xml.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_XMLPermissibleValues == null)? 0 :_XMLPermissibleValues.size());
        context.startElement("", "name");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Name), "Name");
        } catch (java.lang.Exception e) {
            edu.wustl.common.cde.xml.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "publicId");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _PublicId), "PublicId");
        } catch (java.lang.Exception e) {
            edu.wustl.common.cde.xml.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        while (idx1 != len1) {
            context.startElement("", "XMLPermissibleValues");
            int idx_4 = idx1;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _XMLPermissibleValues.get(idx_4 ++)), "XMLPermissibleValues");
            context.endNamespaceDecls();
            int idx_5 = idx1;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _XMLPermissibleValues.get(idx_5 ++)), "XMLPermissibleValues");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _XMLPermissibleValues.get(idx1 ++)), "XMLPermissibleValues");
            context.endElement();
        }
    }

    public void serializeAttributes(edu.wustl.common.cde.xml.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_XMLPermissibleValues == null)? 0 :_XMLPermissibleValues.size());
        if (has_Cache) {
            context.startAttribute("", "cache");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _Cache)), "Cache");
            } catch (java.lang.Exception e) {
                edu.wustl.common.cde.xml.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (has_LazyLoading) {
            context.startAttribute("", "lazyLoading");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _LazyLoading)), "LazyLoading");
            } catch (java.lang.Exception e) {
                edu.wustl.common.cde.xml.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public void serializeURIs(edu.wustl.common.cde.xml.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_XMLPermissibleValues == null)? 0 :_XMLPermissibleValues.size());
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.wustl.common.cde.xml.XMLCDE.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv."
+"grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/su"
+"n/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq"
+"\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002"
+"dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001d"
+"Lcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000#com.sun.msv.datatyp"
+"e.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.d"
+"atatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.data"
+"type.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd"
+".XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/Strin"
+"g;L\u0000\btypeNameq\u0000~\u0000\u0016L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/"
+"WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006"
+"stringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Prese"
+"rve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcess"
+"or\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetEx"
+"pression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t"
+"\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0016L\u0000\fnamespaceURIq\u0000~\u0000\u0016xpq\u0000~\u0000\u001aq\u0000~\u0000\u0019sr"
+"\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.su"
+"n.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClass"
+"q\u0000~\u0000\nxq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000"
+"\u000eppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0013q\u0000"
+"~\u0000\u0019t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$"
+"Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001cq\u0000~\u0000\u001fsq\u0000~\u0000 q\u0000~\u0000+q\u0000~\u0000\u0019sr\u0000#com.sun.ms"
+"v.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0016L\u0000\fname"
+"spaceURIq\u0000~\u0000\u0016xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt"
+"\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com.su"
+"n.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003s"
+"q\u0000~\u0000&\u0001psq\u0000~\u0000/t\u0000\u0004namet\u0000\u0000sq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0011sq\u0000~\u0000\"ppsq\u0000~\u0000$q"
+"\u0000~\u0000\'pq\u0000~\u0000(q\u0000~\u00001q\u0000~\u00005sq\u0000~\u0000/t\u0000\bpublicIdq\u0000~\u00009sr\u0000 com.sun.msv.gr"
+"ammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003ppsq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\tpp\u0000sq\u0000"
+"~\u0000\"ppsq\u0000~\u0000@q\u0000~\u0000\'psq\u0000~\u0000$q\u0000~\u0000\'psr\u00002com.sun.msv.grammar.Express"
+"ion$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00006psr\u0000 com.sun.ms"
+"v.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00000q\u0000~\u00005sq\u0000~\u0000/t\u00000edu.wus"
+"tl.common.cde.xml.XMLPermissibleValueTypet\u0000+http://java.sun."
+"com/jaxb/xjc/dummy-elementssq\u0000~\u0000\"ppsq\u0000~\u0000$q\u0000~\u0000\'pq\u0000~\u0000(q\u0000~\u00001q\u0000~"
+"\u00005sq\u0000~\u0000/t\u0000\u0014XMLPermissibleValuesq\u0000~\u00009sq\u0000~\u0000\"ppsq\u0000~\u0000$q\u0000~\u0000\'psq\u0000~"
+"\u0000\u000eppsr\u0000$com.sun.msv.datatype.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0013q\u0000~\u0000\u0019t\u0000\u0007booleanq\u0000~\u0000-q\u0000~\u0000\u001fsq\u0000~\u0000 q\u0000~\u0000Yq\u0000~\u0000\u0019sq\u0000~\u0000/t\u0000\u0005cacheq\u0000~\u0000"
+"9q\u0000~\u00005sq\u0000~\u0000\"ppsq\u0000~\u0000$q\u0000~\u0000\'pq\u0000~\u0000Vsq\u0000~\u0000/t\u0000\u000blazyLoadingq\u0000~\u00009q\u0000~\u0000"
+"5sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTabl"
+"et\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com"
+".sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005coun"
+"tB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Expression"
+"Pool;xp\u0000\u0000\u0000\u000f\u0001pq\u0000~\u0000Pq\u0000~\u0000\bq\u0000~\u0000Fq\u0000~\u0000]q\u0000~\u0000\u0006q\u0000~\u0000Gq\u0000~\u0000\rq\u0000~\u0000;q\u0000~\u0000\u0005q\u0000"
+"~\u0000\u0007q\u0000~\u0000Bq\u0000~\u0000Tq\u0000~\u0000Dq\u0000~\u0000#q\u0000~\u0000<x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.wustl.common.cde.xml.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.wustl.common.cde.xml.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(edu.wustl.common.cde.xml.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.wustl.common.cde.xml.impl.XMLCDEImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  15 :
                        if (("XMLPermissibleValues" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 13;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  9 :
                        if (("publicId" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  13 :
                        if (("evsTerminology" == ___local)&&("" == ___uri)) {
                            _getXMLPermissibleValues().add(((edu.wustl.common.cde.xml.impl.XMLPermissibleValueTypeImpl) spawnChildFromEnterElement((edu.wustl.common.cde.xml.impl.XMLPermissibleValueTypeImpl.class), 14, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "cache");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        if (("name" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("XMLPermissibleValues" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 13;
                            return ;
                        }
                        break;
                    case  3 :
                        attIdx = context.getAttribute("", "lazyLoading");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Cache = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Cache = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _LazyLoading = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_LazyLoading = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  15 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("", "cache");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  11 :
                        if (("publicId" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  3 :
                        attIdx = context.getAttribute("", "lazyLoading");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case  8 :
                        if (("name" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("XMLPermissibleValues" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  15 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        if (("cache" == ___local)&&("" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  3 :
                        if (("lazyLoading" == ___local)&&("" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  15 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("lazyLoading" == ___local)&&("" == ___uri)) {
                            state = 6;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("cache" == ___local)&&("" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "cache");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  3 :
                        attIdx = context.getAttribute("", "lazyLoading");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  15 :
                            revertToParentFromText(value);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "cache");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  7 :
                            eatText3(value);
                            state = 8;
                            return ;
                        case  10 :
                            eatText4(value);
                            state = 11;
                            return ;
                        case  4 :
                            eatText2(value);
                            state = 5;
                            return ;
                        case  3 :
                            attIdx = context.getAttribute("", "lazyLoading");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 6;
                                continue outer;
                            }
                            state = 6;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Name = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PublicId = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
