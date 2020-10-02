package loader;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.driver.Session;

import neo4j.nodes.nodeLabels;
import neo4j.nodes.nodeProperties;

public class nodeFactory {


	public static void loadNodeToDatabase(Session session, nodeLabels nodeLabels, nodeProperties nodeProperties) {

		int numOfLabels = nodeLabels.getNodeLabels().size();
		String nodePropertiesAsString = "{";
		String nodeLabel = "";

		if(numOfLabels == 1)
			nodeLabel = nodeLabels.getNodeLabels().get(0);

		else {
			ArrayList<String> labels = nodeLabels.getNodeLabels();
			nodeLabel = String.join(":", labels);
		}

		Map<String, Object> nodePropertiesMap = nodeProperties.getNodeProperties();

		for(Entry<String, Object> property:nodePropertiesMap.entrySet()) {

			String propertyName = property.getKey();
			String propertyValue = property.getValue().toString();
			if(propertyValue.contains("\""))
				propertyValue = utils.parseQuotes(propertyValue);
			else if(propertyValue.contains("\\"))
				propertyValue = utils.parseBackslash(propertyValue); // The Java compiler replaces the backslash with a double backslash and then, the Neo4j compiler converts it back to a single backslash

			nodePropertiesAsString += propertyName + ":" + '"' + propertyValue  + '"' + ", ";
		}

		// replace last comma of the node properties by a bracket
		nodePropertiesAsString = nodePropertiesAsString.replaceAll(", $", "}");
		String query = "CREATE (n: " + nodeLabel + " " + nodePropertiesAsString + ") ";
		session.run(query);

	}

}
