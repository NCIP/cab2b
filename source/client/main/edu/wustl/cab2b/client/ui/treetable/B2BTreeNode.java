package edu.wustl.cab2b.client.ui.treetable;


import java.util.Vector;

public class B2BTreeNode{
    
    Vector<Object> children = new Vector();
    
    String strDisplayName;
    
    Object value;
    
    
    protected Object[] getChildren()
    {
        Object[] childrenObj = new Object[children.size()];
        
        for (int i = 0; i < children.size(); i++)
            childrenObj[i] = children.get(i);    

        return childrenObj;        
    }
        
    public String toString()
    {
        return strDisplayName;
    }
    
    public Object getValue()
    {
        return value;
    }
    
    
    public void addChild(B2BTreeNode child)
    {
        this.children.add(child);      
    }
    
    
    public void setChildren(Vector<Object> children)
    {
        this.children = children;
        
    }
    
    public void setDisplayName(String strDisplayName)
    {
        this.strDisplayName = strDisplayName;
    
    }
    
    
    public void setValue(Object value){
        
        this.value = value;
        
    } 
    
    
}
