package neo4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import neo4j.nodes.nodeElement;
import neo4j.nodes.nodeElementSet;
import neo4j.nodes.nodeIdentifier;
import neo4j.nodes.nodeLabels;
import neo4j.nodes.nodeProperties;
import neo4j.relationships.relationshipElement;
import neo4j.relationships.relationshipElementSet;
import neo4j.relationships.relationshipIdentifier;
import neo4j.relationships.relationshipProperties;
import neo4j.relationships.relationshipType;



public class dump {

	public static void main(String[] args) {

		System.out.println("Connecting to database...");
		GraphDatabaseService graphDatabaseService = connectDB(args[0]);
		System.out.println("Successfull connection to: " + args[0]);

		System.out.println("Dumping nodes...");
		dumpNeo4jEmbeddedNodesToJson(graphDatabaseService, args[1]);
		System.out.println("Successfully dumped all nodes!");

		System.out.println("Dumping relationships...");
		dumpNeo4jEmbeddedRelationshipsToJson(graphDatabaseService, args[2]);
		System.out.println("Successfully dumped all relationships!");

		System.out.println("Shutting down connection...");
		graphDatabaseService.shutdown();
		System.out.println("Database dump operation has finished successfully!");


	}

	private static void dumpNeo4jEmbeddedNodesToJson(GraphDatabaseService graphDatabaseService, String jsonPath) {

		Transaction dataTx = graphDatabaseService.beginTx();
		ResourceIterable<Node> allNodes = graphDatabaseService.getAllNodes();

		nodeElementSet allNodeElements = new nodeElementSet();

		for(Node node:allNodes) {

			long nodeId = node.getId();
			ArrayList<String> nodeLabels = new ArrayList<String>();
			Iterable<Label> nodeLabelIter = node.getLabels();

			for(Label label:nodeLabelIter) 
				nodeLabels.add(label.toString());

			Map<String, Object> allProperties = node.getAllProperties();

			nodeIdentifier nodeIdentifierObj = new nodeIdentifier(nodeId);
			nodeLabels nodeLabelsObj = new nodeLabels(nodeLabels);
			nodeProperties nodePropertiesObj = new nodeProperties(allProperties);
			nodeElement nodeElement = new nodeElement(nodeIdentifierObj, nodeLabelsObj, nodePropertiesObj);

			allNodeElements.getNodeElementSet().add(nodeElement);
		}

		dataTx.close();
		writeObjectAsJson(allNodeElements, jsonPath);
	}


	private static void dumpNeo4jEmbeddedRelationshipsToJson(GraphDatabaseService graphDatabaseService, String jsonPath) {

		Transaction dataTx = graphDatabaseService.beginTx();
		ResourceIterable<Relationship> allRelationships = graphDatabaseService.getAllRelationships();

		relationshipElementSet allRelationshipElements = new relationshipElementSet();

		for(Relationship relationship:allRelationships) {

			long relationshipId = relationship.getId();
			RelationshipType relationshipType = relationship.getType();
			Map<String, Object> allProperties = relationship.getAllProperties();

			relationshipIdentifier relationshipIdentifierObj = new relationshipIdentifier(relationshipId);
			relationshipType relationshipTypeObj = new relationshipType(relationshipType.toString());
			relationshipProperties relationshipPropertiesObj = new relationshipProperties(allProperties);
			relationshipElement relationshipElement = new relationshipElement(relationshipIdentifierObj, relationshipTypeObj, relationshipPropertiesObj, relationship.getStartNodeId(), relationship.getEndNodeId());

			allRelationshipElements.getRelationshipElementSet().add(relationshipElement);
		}

		writeObjectAsJson(allRelationshipElements, jsonPath);
		dataTx.close();

	}


	private static void writeObjectAsJson(Object objectSet, String jsonPath) {


		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(new File(jsonPath.toString()), objectSet);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param filepath
	 * @return
	 * Connect to existing database
	 */
	public static GraphDatabaseService connectDB(String filepath) {

		return new GraphDatabaseFactory().newEmbeddedDatabase(new File(filepath));

	}




}
