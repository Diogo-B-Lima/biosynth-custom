package loader;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.driver.Session;

import neo4j.relationships.relationshipProperties;

public class relationshipFactory {


	public static void loadRelationshipToDatabase(Session session, String relationshipType, relationshipProperties relationshipProperties, long startNodeId, long endNodeId) {

		String relationshipPropertiesAsString = "{";

		Map<String, Object> relationshipPropertiesMap = relationshipProperties.getRelationshipProperties();

		for(Entry<String, Object> property:relationshipPropertiesMap.entrySet()) {

			String propertyName = property.getKey();
			String propertyValue = property.getValue().toString();
			if(propertyName.contains("-"))
				propertyName = utils.parseHyphen(propertyName);
			else if(propertyValue.contains("\""))
				propertyValue = utils.parseQuotes(propertyValue);
			else if(propertyValue.contains("\\"))
				propertyValue = utils.parseBackslash(propertyValue); // The Java compiler replaces the backslash with a double backslash and then, the Neo4j compiler converts it back to a single backslash
			relationshipPropertiesAsString += propertyName + ":" + '"' + propertyValue  + '"' + ", ";
		}

		// replace last comma of the node properties by a bracket
		relationshipPropertiesAsString = relationshipPropertiesAsString.replaceAll(", $", "}");

		String matchQuery = "MATCH (n1), (n2) WHERE ID(n1) = $startId and ID(n2) = $endId ";
		String relQuery = "CREATE (n1)-[:" + relationshipType + " " + relationshipPropertiesAsString + "]->(n2)";

		HashMap<String, Object> queryParameters = new HashMap<String, Object>();
		queryParameters.put("startId", startNodeId);
		queryParameters.put("endId", endNodeId);
		String finalQuery = matchQuery + relQuery;
		session.run(finalQuery, queryParameters);
	}

}
