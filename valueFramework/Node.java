package valueFramework;


import java.util.ArrayList;
import java.util.List;


public class Node
{
    private List<Node> children = null;
    private Node parent;
    private String valueName;

    /*public Node(String value)
    {
        this.children = new ArrayList<>();
        this.setValueName(value);
    }*/
    
    public Node(String value, Node parentIn)
    {
        this.children = new ArrayList<>();
        this.setValueName(value);
        this.setParent(parentIn);
    }

    public void addChild(Node child)
    {
        children.add(child);
    }

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String value) {
		this.valueName = value;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

}