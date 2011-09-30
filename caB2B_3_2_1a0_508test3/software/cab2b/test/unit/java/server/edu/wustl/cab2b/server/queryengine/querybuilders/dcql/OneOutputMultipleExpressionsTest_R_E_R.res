<DCQLQuery xmlns="http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.dcql">
    <TargetObject name="srinath.mocks.query.class_1" serviceURL="http://targetUrl">
        <Group logicRelation="AND">
            <Attribute name="attr_c1a1" predicate="GREATER_THAN" value="val12"/>
            <Group logicRelation="OR">
                <Association roleName="villain" name="srinath.mocks.query.class_2">
                    <Attribute name="attr_c2a1" predicate="LESS_THAN" value="val21"/>
                </Association>
                <Attribute name="attr_c1a1" predicate="EQUAL_TO" value="val11"/>
            </Group>
        </Group>
    </TargetObject>
</DCQLQuery>