package edu.wustl.cab2b.client.ui.searchDataWizard.dag;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.undo.UndoableEdit;

import org.netbeans.graph.api.IGraphEventHandler;
import org.netbeans.graph.api.model.GraphEvent;
import org.netbeans.graph.api.model.IGraphLink;
import org.netbeans.graph.api.model.IGraphNode;
import org.netbeans.graph.api.model.IGraphPort;


public class EventHandler extends IGraphEventHandler 
{
	private MainDagPanel m_dagPanel;
	
	public EventHandler(MainDagPanel panel)
	{
		m_dagPanel = panel;
	}
	@Override
	public void accept(IGraphNode arg0, Transferable arg1) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void componentsSelected(GraphEvent arg0)
	{
		m_dagPanel.getDocument().selectComponents(arg0);
	}

	@Override
	public void createLink(IGraphPort sourcePort, IGraphPort targetPort)
	{

	}

	@Override
	public boolean isAcceptable(IGraphNode arg0, DataFlavor[] arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLinkCreateable(IGraphPort arg0, IGraphPort arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void notifyModified()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setSourcePort(IGraphLink arg0, IGraphPort arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setTargetPort(IGraphLink arg0, IGraphPort arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void undoableEditHappened(UndoableEdit arg0) 
	{
		// TODO Auto-generated method stub

	}

}
