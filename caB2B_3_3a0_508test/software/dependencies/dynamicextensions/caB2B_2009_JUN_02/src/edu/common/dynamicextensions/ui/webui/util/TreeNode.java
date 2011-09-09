package edu.common.dynamicextensions.ui.webui.util;
public class TreeNode
{
	private String text = "";
	private String href;
	private String target = "";
	private String toolTip;
	private TreeNodesList childNodes;
	private String imageUrl = "";
	private int length = 0;
	private int sequenceNumber = 0;
	private boolean showRadioBtn = false;

	/**
	 * Create a new tree node
	 *
	 */
	public TreeNode()
	{
		childNodes = new TreeNodesList();
	}
	
	public TreeNode(String text)
	{
		this(text,0);
	}
	
	/**
	 * Create a new tree node with specified text and sequence number
	 * @param text : Text for tree node 
	 * @param seqno : Sequence number
	 */
	public TreeNode(String text, int seqno)
	{
		this(text, "", seqno);
	}

	/**
	 * Create a new tree node
	 * @param text : Text for tree node
	 * @param href : URL ref for the tree node hyperlink
	 * @param seqno : Sequence number
	 */
	public TreeNode(String text, String href, int seqno)
	{
		this(text, href, "", seqno);
	}

	/**
	 * 
	 * @param text : Text for tree node
	 * @param href : URL ref for the tree node hyperlink
	 * @param toolTip : Tooltip for tree node
	 * @param seqno : Sequence number
	 */
	public TreeNode(String text, String href, String toolTip, int seqno)
	{
		this();
		this.text = text.trim();
		this.href = href;
		this.toolTip = toolTip;
		this.sequenceNumber = seqno;
	}

	/**
	 * Add subnode to tree node
	 * @param treeNode :  sub node to be added 
	 */
	public void add(TreeNode treeNode)
	{
		childNodes.add(treeNode);
		length++;
	}

	/**
	 * Add new sub node with specified text and sequence number
	 * @param text : Text for tree node 
	 * @param seqno : Sequence number
	 */
	public void add(String text, int seqno)
	{
		add(new TreeNode(text, seqno));
	}

	/**
	 * 
	 * @return Child node list
	 */
	public TreeNodesList getChildNodes()
	{
		return this.childNodes;
	}

	/**
	 * 
	 * @param childNodes Child node list
	 */
	public void setChildNodes(TreeNodesList childNodes)
	{
		this.childNodes = childNodes;
	}

	/**
	 * 
	 * @return href
	 */
	public String getHref()
	{
		return this.href;
	}

	/**
	 * 
	 * @param href href
	 */
	public void setHref(String href)
	{
		this.href = href;
	}

	/**
	 * 
	 * @return image files URL 
	 */
	public String getImageUrl()
	{
		return this.imageUrl;
	}

	/**
	 * 
	 * @param imageUrl image files URL
	 */
	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	/**
	 * 
	 * @return number of childnodes
	 */
	public int getLength()
	{
		return this.length;
	}

	/**
	 * 
	 * @param length length of child node list
	 */
	public void setLength(int length)
	{
		this.length = length;
	}

	/**
	 * @return Sequence number
	 */
	public int getSequenceNumber()
	{
		return this.sequenceNumber;
	}
	/**
	 * 
	 * @param sequenceNumber Sequence number
	 */
	public void setSequenceNumber(int sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 *
	 * @return Target
	 */
	public String getTarget()
	{
		return this.target;
	}

	/**
	 * 
	 * @param target target
	 */
	public void setTarget(String target)
	{
		this.target = target;
	}

	/**
	 * 
	 * @return text
	 */
	public String getText()
	{
		return this.text;
	}

	/**
	 * 
	 * @param text text
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * 
	 * @return tool tip
	 */
	public String getToolTip()
	{
		return this.toolTip;
	}

	/**
	 * 
	 * @param toolTip tooltip
	 */
	public void setToolTip(String toolTip)
	{
		this.toolTip = toolTip;
	}

	public boolean isShowRadioBtn()
	{
		return this.showRadioBtn;
	}

	public void setShowRadioBtn(boolean showRadioBtn)
	{
		this.showRadioBtn = showRadioBtn;
	}
}
