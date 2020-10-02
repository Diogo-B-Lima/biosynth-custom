package biosynthCustom;


import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import edu.uminho.biosynth.core.data.integration.neo4j.HelperNeo4jConfigInitializer;
import pt.uminho.sysbio.biosynth.integration.GraphMetaboliteEntity;
import pt.uminho.sysbio.biosynth.integration.GraphReactionEntity;
import pt.uminho.sysbio.biosynth.integration.etl.EtlTransform;
import pt.uminho.sysbio.biosynth.integration.etl.biodb.kegg.KeggCompoundTransform;
import pt.uminho.sysbio.biosynth.integration.etl.biodb.kegg.KeggDrugTransform;
import pt.uminho.sysbio.biosynth.integration.etl.biodb.kegg.KeggGlycanTransform;
import pt.uminho.sysbio.biosynth.integration.etl.biodb.kegg.KeggReactionTransform;
import pt.uminho.sysbio.biosynth.integration.neo4j.BiodbMetaboliteNode;
import pt.uminho.sysbio.biosynth.integration.neo4j.BiodbReactionNode;
import pt.uminho.sysbio.biosynthframework.BiodbGraphDatabaseService;
import pt.uminho.sysbio.biosynthframework.biodb.bigg.Bigg2MetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.biodb.bigg.Bigg2ReactionEntity;
import pt.uminho.sysbio.biosynthframework.biodb.biocyc.BioCycMetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.biodb.biocyc.BioCycReactionEntity;
import pt.uminho.sysbio.biosynthframework.biodb.modelseed.ModelSeedMetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.biodb.modelseed.ModelSeedReactionEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggCompoundMetaboliteDaoImpl;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggDrugMetaboliteDaoImpl;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggGlycanMetaboliteDaoImpl;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggReactionDaoImpl;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.ptools.biocyc.RestBiocycMetaboliteDaoImpl;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.ptools.biocyc.RestBiocycReactionDaoImpl;
import pt.uminho.sysbio.biosynthframework.io.MetaboliteDao;
import pt.uminho.sysbio.biosynthframework.io.ReactionDao;
import pt.uminho.sysbio.biosynthframework.io.biodb.RestBigg2ReactionDaoImpl;
import thirdPartyDatabaseAccess.bigg.biggCompoundETL;
import thirdPartyDatabaseAccess.bigg.biggReactionETL;
import thirdPartyDatabaseAccess.kegg.keggCompoundETL;
import thirdPartyDatabaseAccess.kegg.keggDrugETL;
import thirdPartyDatabaseAccess.kegg.keggGlycanETL;
import thirdPartyDatabaseAccess.kegg.keggReactionETL;
import thirdPartyDatabaseAccess.metacyc.metacycCompoundETL;
import thirdPartyDatabaseAccess.metacyc.metacycReactionETL;
import thirdPartyDatabaseAccess.modelseed.modelseedCompoundETL;
import thirdPartyDatabaseAccess.modelseed.modelseedReactionETL;


/*

Please make sure you have the following JARs from the biosynth-framework project in this project's build path (available at src/main/resources):

biosynth-biodb-0.9.0-SNAPSHOT.jar  (NOT THE 0.9.1-SNAPSHOT.jar version available in Filipe Liu's GitHub, that one misses important classes for using Bigg's REST API)
biosynth-biodb-chebi-0.9.1-SNAPSHOT.jar
biosynth-biodb-intenz-0.9.1-SNAPSHOT.jar
biosynth-biodb-lipidmaps-0.9.1-SNAPSHOT.jar
biosynth-biodb-modelseed-0.9.1-SNAPSHOT.jar
biosynth-biodb-reactome-0.9.1-SNAPSHOT.jar
biosynth-chemanalysis-0.9.1-SNAPSHOT.jar
biosynth-core-0.9.1-SNAPSHOT.jar
biosynth-genome-0.9.1-SNAPSHOT.jar
biosynth-integration-0.9.1-SNAPSHOT.jar
biosynth-optimization-0.9.1-SNAPSHOT.jar
biosynth-visualization-0.9.1-SNAPSHOT.jar */

public class initDB {


	/**
	 * @param String filepath
	 * @param String modelSeedCompoundsJsonPath
	 * @param String modelSeedReactionsJsonPath
	 * @throws IOException
	 * Create a database (or connect to existing one) and perform ETL process for KEGG, Bigg, MetaCyc and ModelSeed third party resources
	 */
	public static void main(String[] args) throws IOException {

		/* absolute path for the directory where the Neo4j will be created or already exists at */
		String filepath = args[0];

		/* compounds.json file retrieved from ModelSeed (available here at the moment of writing -> https://github.com/ModelSEED/ModelSEEDDatabase/blob/master/Biochemistry/compounds.json) */
		String modelSeedCompoundsJsonPath = args[1];

		/* reactions.json file retrieved from ModelSeed (available here at the moment of writing -> https://github.com/ModelSEED/ModelSEEDDatabase/blob/master/Biochemistry/reactions.json) */
		String modelSeedReactionsJsonPath = args[2];

		/* Create an embedded Neo4j database and initialize constraints (to avoid duplicates and other structural issues as well as increase performance) */
		GraphDatabaseService graphDatabaseService = createDB(filepath);

		/* Uncomment this if you want to connect to a previously existing database (this method does not init constraints in the database) */
		//GraphDatabaseService graphDatabaseService = connectDB(filepath);

		/* The following methods extract data from KEGG, Bigg (most recent version, commonly called Bigg 2) and MetaCyc using their REST API. ModelSeed data is parsed from the two files mentioned above */
		/* After extracting the data, these methods standardize everything, convert the data to graph structures and load it to the Neo4j database */
		System.out.println("Starting KEGG ETL");
		etlKegg(graphDatabaseService);

		System.out.println("Starting BIGG ETL");
		etlBigg(graphDatabaseService);

		System.out.println("Starting MetaCyc ETL");
		etlMetacyc(graphDatabaseService);

		System.out.println("FIXING MODELSEED");
		System.out.println("Starting ModelSeed ETL");
		etlModelSeed(modelSeedCompoundsJsonPath, modelSeedReactionsJsonPath, graphDatabaseService);

		/* Uncomment this to test the ETL process and list loaded entities */
		//listMetab(graphDatabaseService);
		//listReactions(graphDatabaseService);

		System.out.println("Operation finished successfully");


	}


	/**
	 * @param graphDatabaseService
	 * Connect to database and print all metabolite properties
	 */
	private static void listMetab(GraphDatabaseService graphDatabaseService) {

		/* It is mandary to start a transaction to access the database */
		Transaction dataTx = graphDatabaseService.beginTx();
		BiodbGraphDatabaseService service = new BiodbGraphDatabaseService(graphDatabaseService);

		queryMetab(service);

		/* Transaction.failure() makes sure that nothing is saved to the database even if write queries were executed */
		/*  It is mandatory to close the transaction and close the database service after querying the database */
		dataTx.failure(); 
		dataTx.close();
		service.shutdown();
	}

	/**
	 * @param graphDatabaseService
	 * Connect to database and print all reaction properties
	 */
	private static void listReactions(GraphDatabaseService graphDatabaseService) {

		/* It is mandary to start a transaction to access the database */
		Transaction dataTx = graphDatabaseService.beginTx();
		BiodbGraphDatabaseService service = new BiodbGraphDatabaseService(graphDatabaseService);

		queryReact(service);

		/* Transaction.failure() makes sure that nothing is saved to the database even if write queries were executed */
		/*  It is mandatory to close the transaction and close the database service after querying the database */
		dataTx.failure(); 
		dataTx.close();
		service.shutdown();
	}



	/**
	 * @param filepath
	 * @return
	 * Create database and initialize constraints
	 */
	private static GraphDatabaseService createDB(String filepath) {

		return HelperNeo4jConfigInitializer.initializeNeo4jDataDatabaseConstraints(filepath);

	}

	/**
	 * @param filepath
	 * @return
	 * Connect to existing database
	 */
	public static GraphDatabaseService connectDB(String filepath) {

		return new GraphDatabaseFactory().newEmbeddedDatabase(new File(filepath));

	}

	/**
	 * @param service
	 * Access all nodes with the metabolite label in the database and print their properties
	 */
	private static void queryMetab(BiodbGraphDatabaseService service) {

		Set<BiodbMetaboliteNode> metabolites = service.listMetabolites();

		for(BiodbMetaboliteNode met : metabolites) {
			System.out.println(met.getAllProperties());
		}
	}



	/**
	 * @param service
	 * Access all nodes with the reaction label in the database and print their properties

	 */ 
	private static void queryReact(BiodbGraphDatabaseService service) {

		Set<BiodbReactionNode> reactions = service.listReactions();

		for(BiodbReactionNode reac : reactions)
			System.out.println(reac.getAllProperties());

	}



	/**
	 * @param graphDatabaseService
	 * Perform ETL process for KEGG, fetching data in their Compound, Drug, Glycan and Reaction knowledgebase
	 */
	private static void etlKegg(GraphDatabaseService graphDatabaseService) {

		// Extract, transform and load KEGG compound data

		System.out.println("Starting KEGG Compound ETL");

		RestKeggCompoundMetaboliteDaoImpl keggCompoundDao = keggCompoundETL.getKeggCompoundMetaboliteDao();
		KeggCompoundTransform keggCompoundTransform = keggCompoundETL.getKeggCompoundTransform();
		etlMetabolite.etl(keggCompoundDao, keggCompoundTransform, graphDatabaseService);

		//		// Extract, transform and load KEGG drug data

		System.out.println("Starting KEGG Drug ETL");

		RestKeggDrugMetaboliteDaoImpl keggDrugdDao = keggDrugETL.getKeggDrugMetaboliteDao();
		KeggDrugTransform keggDrugTransform = keggDrugETL.getKeggDrugTransform();
		etlMetabolite.etl(keggDrugdDao, keggDrugTransform, graphDatabaseService);

		// Extract, transform and load KEGG glycan data

		System.out.println("Starting KEGG Glycan ETL");

		RestKeggGlycanMetaboliteDaoImpl keggGlycanDao = keggGlycanETL.getKeggGlycanMetaboliteDao();
		KeggGlycanTransform keggGlycanTransform = keggGlycanETL.getKeggGlycanTransform();
		etlMetabolite.etl(keggGlycanDao, keggGlycanTransform, graphDatabaseService);

		// Extract, transform and load KEGG reaction data

		System.out.println("Starting KEGG Reaction ETL");

		RestKeggReactionDaoImpl keggReactionDao = keggReactionETL.getKeggReactionDao();
		KeggReactionTransform keggReactionTransform = keggReactionETL.getKeggReactionTransform();
		etlReaction.etl(keggReactionDao, keggReactionTransform, graphDatabaseService);


	}


	/**
	 * @param graphDatabaseService
	 * Perform ETL process for Bigg
	 */
	private static void etlBigg(GraphDatabaseService graphDatabaseService) {

		//		// Extract, transform and load Bigg 2 compound data using Bigg Rest API

		System.out.println("Starting Bigg 2 Compound ETL");

		MetaboliteDao<Bigg2MetaboliteEntity> bigg2CompoundDao = biggCompoundETL.getRestBigg2CompoundMetaboliteDao();
		EtlTransform<Bigg2MetaboliteEntity, GraphMetaboliteEntity> bigg2CompoundTransform = biggCompoundETL.getBigg2MetaboliteTransform();
		etlMetabolite.etl(bigg2CompoundDao, bigg2CompoundTransform, graphDatabaseService);
		//
		//		// Extract, transform and load Bigg 1 compound data from a local CSV file

		//		MetaboliteDao<BiggMetaboliteEntity> bigg1CompoundDao = biggCompoundETL.getBigg1CompoundMetaboliteDao(biggMetabolitesPath);
		//		EtlTransform<BiggMetaboliteEntity, GraphMetaboliteEntity> bigg1CompoundTransform = biggCompoundETL.getBiggMetaboliteTransform();
		//		etlMetabolite.etl(bigg1CompoundDao, bigg1CompoundTransform, graphDatabaseService);


		// Extract, transform and load Bigg 2 reaction data using Bigg Rest API

		System.out.println("Starting Bigg 2 Reaction ETL");
		//
		RestBigg2ReactionDaoImpl bigg2ReactionDao = biggReactionETL.getBigg2ReactionDao();
		EtlTransform<Bigg2ReactionEntity, GraphReactionEntity> bigg2ReactionTransform = biggReactionETL.getBigg2ReactionTransform();
		etlReaction.etl(bigg2ReactionDao, bigg2ReactionTransform, graphDatabaseService);

		////		// Extract, transform and load Bigg 1 reaction data from a local CSV file

		//		CsvBiggReactionDaoImpl bigg1ReactionDao = biggReactionETL.getBigg1ReactionDao(biggReactionsPath);
		//		EtlTransform<BiggReactionEntity, GraphReactionEntity> bigg1ReactionTransform = biggReactionETL.getBigg1ReactionTransform();
		//		etlReaction.etl(bigg1ReactionDao, bigg1ReactionTransform, graphDatabaseService);



	}


	/**
	 * @param graphDatabaseService
	 * Perform ETL process for MetaCyc
	 */
	private static void etlMetacyc(GraphDatabaseService graphDatabaseService) {

		// Extract, transform and load BioCyc (Metacyc) metabolites

		System.out.println("Starting BioCyc (MetaCyc) Compound ETL");

		RestBiocycMetaboliteDaoImpl bioCompoundDao = metacycCompoundETL.getBiocycMetaboliteDaoImpl();
		EtlTransform<BioCycMetaboliteEntity, GraphMetaboliteEntity> biocycCompoundTransform = metacycCompoundETL.getBioTransformFromBiggRest();
		etlMetabolite.etl(bioCompoundDao, biocycCompoundTransform, graphDatabaseService);

		// Extract, transform and load BioCyc (Metacyc) reactions

		System.out.println("Starting BioCyc (MetaCyc) Reaction ETL");

		RestBiocycReactionDaoImpl bioReactionDao = metacycReactionETL.getRestBiocycReactionDaoImpl();
		EtlTransform<BioCycReactionEntity, GraphReactionEntity> biocycReactionTransform = metacycReactionETL.getBiocycReactionTransform();
		etlReaction.etl(bioReactionDao, biocycReactionTransform, graphDatabaseService);
	}


	/**
	 * @param modelSeedCompoundsJsonPath
	 * @param modelSeedReactionsJsonPath
	 * @param graphDatabaseService
	 * @throws IOException
	 * Perform ETL process for ModelSedd using local JSON files for both the compounds and reactions

	 */
	private static void etlModelSeed(String modelSeedCompoundsJsonPath, String modelSeedReactionsJsonPath, GraphDatabaseService graphDatabaseService) throws IOException {


		// Extract, transform and load ModelSeed metabolites

		System.out.println("Starting ModelSeed Compound ETL");

		MetaboliteDao<ModelSeedMetaboliteEntity> modelseedCompoundDao = modelseedCompoundETL.getModelSeedCompoundDatabase(modelSeedCompoundsJsonPath);
		EtlTransform<ModelSeedMetaboliteEntity, GraphMetaboliteEntity> modelseedCompoundTransform = modelseedCompoundETL.getModelseedCompoundTransform();
		etlMetabolite.etl(modelseedCompoundDao, modelseedCompoundTransform, graphDatabaseService);

		// Extract, transform and load ModelSeed reactions

		System.out.println("Starting ModelSeed Reaction ETL");

		ReactionDao<ModelSeedReactionEntity> modelseedReactionDao = modelseedReactionETL.getModelSeedReactionDatabase(modelSeedReactionsJsonPath);
		EtlTransform<ModelSeedReactionEntity, GraphReactionEntity> modelseedReactionTransform = modelseedReactionETL.getModelseedReactionTransform();
		etlReaction.etl(modelseedReactionDao, modelseedReactionTransform, graphDatabaseService);



	}












}
