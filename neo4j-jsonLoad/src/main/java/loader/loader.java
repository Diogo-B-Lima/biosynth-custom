package loader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.apache.commons.io.IOUtils;
import org.neo4j.driver.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import neo4j.nodes.nodeElement;
import neo4j.nodes.nodeElementSet;
import neo4j.nodes.nodeLabels;
import neo4j.nodes.nodeProperties;
import neo4j.relationships.relationshipElement;
import neo4j.relationships.relationshipElementSet;
import neo4j.relationships.relationshipProperties;



public class loader {


	public static void main(String[] args) throws Exception {

		System.out.println("Loading operation has begun! [" + Instant.now() + "]") ;
		neo4jConnector connector = new neo4jConnector();

		System.out.println("Loading nodes ... [" + Instant.now() + "]");
		loadNodesFromJson(args[0], connector);
		System.out.println("All nodes have been loaded! [" + Instant.now() + "]\"");

		System.out.println("Loading relationships ... [" + Instant.now() + "]\"");
		loadRelationshipsFromJson(args[1], connector);
		System.out.println("All relationships have been loaded! [" + Instant.now() + "]\"");

		connector.close();

	}

	public static void loadNodesFromJson(String jsonAbsolutePath, neo4jConnector connector) throws Exception {

		nodeElementSet treeRoot = convertJsonNodesToTree(jsonAbsolutePath);
		int treeNodeIndex;
		int treeSize = treeRoot.getNodeElementSet().size();

		Session session = connector.getDriver().session();

		for (treeNodeIndex = 0; treeNodeIndex < treeSize; treeNodeIndex++) { 		

			/* Convert tree nodes to Java objects */

			nodeElement nodeElement = treeRoot.getNodeElementSet().get(treeNodeIndex);
			nodeLabels nodeLabels = nodeElement.getNodeLabels();
			nodeProperties nodeProperties = nodeElement.getNodeProperties();

			if(treeNodeIndex % 1000 == 0) {
				session.close();
				session = connector.getDriver().session();	
			}

			nodeFactory.loadNodeToDatabase(session, nodeLabels, nodeProperties);
		}

		if(session.isOpen())
			session.close();
	}

	private static void loadRelationshipsFromJson(String jsonAbsolutePath, neo4jConnector connector) throws IOException {

		relationshipElementSet treeRoot = convertJsonRelationshipsToTree(jsonAbsolutePath);
		int treeNodeIndex;
		int treeSize = treeRoot.getRelationshipElementSet().size();

		Session session = connector.getDriver().session();

		for (treeNodeIndex = 0; treeNodeIndex < treeSize; treeNodeIndex++) { 		

			/* Convert tree nodes to Java objects */

			relationshipElement relationshipElement = treeRoot.getRelationshipElementSet().get(treeNodeIndex);
			String relationshipType = relationshipElement.getRelationshipType().getRelationshipType();
			relationshipProperties relationshipProperties = relationshipElement.getRelationshipProperties();
			long startNodeId = relationshipElement.getStartNodeId();
			long endNodeId = relationshipElement.getEndNodeId();

			if(treeNodeIndex % 1000 == 0) {
				session.close();
				session = connector.getDriver().session();	
			}
			relationshipFactory.loadRelationshipToDatabase(session, relationshipType, relationshipProperties, startNodeId, endNodeId);
		}

		if(session.isOpen())
			session.close();
	}

	private static nodeElementSet convertJsonNodesToTree(String jsonAbsolutePath) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		/* Read JSON file as a String  */
		FileInputStream inputStream = new FileInputStream(jsonAbsolutePath);
		String jsonAsString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		inputStream.close();

		/* Read String and build Tree of nodes where each node represents an entry of the JSON file */
		JsonNode tree = mapper.readTree(jsonAsString);

		nodeElementSet treeRoot = mapper.treeToValue(tree, nodeElementSet.class);
		return treeRoot;
	}

	private static relationshipElementSet convertJsonRelationshipsToTree(String jsonAbsolutePath) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		/* Read JSON file as a String  */
		FileInputStream inputStream = new FileInputStream(jsonAbsolutePath);
		String jsonAsString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		inputStream.close();

		/* Read String and build Tree of nodes where each node represents an entry of the JSON file */
		JsonNode tree = mapper.readTree(jsonAsString);

		relationshipElementSet treeRoot = mapper.treeToValue(tree, relationshipElementSet.class);
		return treeRoot;
	}



}





