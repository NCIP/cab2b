<DCQLQuery xmlns="http://caGrid.caBIG/1.0/gov.nih.nci.cagrid.dcql">
    <TargetObject name="srinath.mocks.query.class_1" serviceURL="http://targetUrl">
        <ForeignAssociation>
            <JoinCondition>
                <LeftJoin>
                    <Object>srinath.mocks.query.class_1</Object>
                    <Property>attr_c1a1</Property>
                </LeftJoin>
                <RightJoin>
                    <Object>srinath.mocks.query.class_2</Object>
                    <Property>attr_c2a1</Property>
                </RightJoin>
            </JoinCondition>
            <ForeignObject name="srinath.mocks.query.class_2" serviceURL="targetUrl"/>
        </ForeignAssociation>
    </TargetObject>
</DCQLQuery>