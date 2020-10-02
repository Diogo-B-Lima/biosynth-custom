package neo4j.relationships;

import java.util.ArrayList;

public class relationshipElementSet {

	ArrayList<relationshipElement> relationshipElementSet;

	public relationshipElementSet(ArrayList<relationshipElement> relationshipElementSet) {

		this.relationshipElementSet = relationshipElementSet;
	}

	public relationshipElementSet() {

		this.relationshipElementSet = new ArrayList<relationshipElement>();
	}

	public ArrayList<relationshipElement> getRelationshipElementSet() {
		return relationshipElementSet;
	}
	public void setRelationshipElementSet(ArrayList<relationshipElement> relationshipElementSet) {
		this.relationshipElementSet = relationshipElementSet;
	}



}
