<DCQLQuery xmlns="http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.dcql">
    <TargetObject name="srinath.mocks.query.class_1" serviceURL="http://targetUrl">
        <Group logicRelation="AND">
            <Attribute name="attr_3" predicate="EQUAL_TO" value="val3"/>
            <Group logicRelation="OR">
                <Attribute name="attr_2" predicate="EQUAL_TO" value="val2"/>
                <Group logicRelation="AND">
                    <Attribute name="attr_0" predicate="EQUAL_TO" value="val0"/>
                    <Attribute name="attr_1" predicate="EQUAL_TO" value="val1"/>
                </Group>
            </Group>
        </Group>
    </TargetObject>
</DCQLQuery>