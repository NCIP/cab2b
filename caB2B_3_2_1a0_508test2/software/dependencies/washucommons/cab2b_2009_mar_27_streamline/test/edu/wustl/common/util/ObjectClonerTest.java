package edu.wustl.common.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class ObjectClonerTest extends TestCase {
    public void testClone() throws Exception {
        List<Integer> orig = newList();
        ObjectCloner cloner = new ObjectCloner();

        List<Integer> clone1 = cloner.clone(orig);
        check(orig, clone1);
        List<Integer> clone2 = cloner.clone(orig);
        check(orig, clone2);
        check(clone1, clone2);
    }

    private void check(List<Integer> list1, List<Integer> list2) {
        assertNotSame(list1, list2);
        assertNotSame(list1.get(0), list2.get(0));
        assertEquals(list1, list2);
    }

    private List<Integer> newList() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(null);

        return list;
    }
}
