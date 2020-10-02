package neo4j.nodes;

public class nodeIdentifier {

	private long nodeId;

	public nodeIdentifier(long nodeId) {
		this.nodeId = nodeId;
	}

	public nodeIdentifier() {
	}

	public long getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

}
