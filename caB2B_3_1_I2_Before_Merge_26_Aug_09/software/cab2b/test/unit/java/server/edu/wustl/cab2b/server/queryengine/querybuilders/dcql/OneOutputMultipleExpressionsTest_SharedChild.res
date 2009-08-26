<DCQLQuery xmlns="http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.dcql">
    <TargetObject name="srinath.mocks.query.class_root" serviceURL="http://targetUrl">
        <Group logicRelation="AND">
            <Association roleName="root_int1" name="srinath.mocks.query.class_int1">
                <Association roleName="int1_leaf" name="srinath.mocks.query.class_leaf">
                    <Attribute name="attr_leaf" predicate="EQUAL_TO" value="val"/>
                </Association>
            </Association>
            <Association roleName="root_int2" name="srinath.mocks.query.class_int2">
                <Association roleName="int2_leaf" name="srinath.mocks.query.class_leaf">
                    <Attribute name="attr_leaf" predicate="EQUAL_TO" value="val"/>
                </Association>
            </Association>
        </Group>
    </TargetObject>
</DCQLQuery>