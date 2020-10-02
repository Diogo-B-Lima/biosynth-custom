package biosynthCustom;

import org.neo4j.graphdb.GraphDatabaseService;

import pt.uminho.sysbio.biosynth.integration.GraphReactionEntity;
import pt.uminho.sysbio.biosynth.integration.HeterogenousReactionEtlLoad;
import pt.uminho.sysbio.biosynth.integration.etl.DefaultReactionEtlExtract;
import pt.uminho.sysbio.biosynth.integration.etl.EtlTransform;
import pt.uminho.sysbio.biosynth.integration.etl.HbmNeo4jHybridReactionEtlPipeline;
import pt.uminho.sysbio.biosynth.integration.io.dao.neo4j.Neo4jGraphReactionDaoImpl;
import pt.uminho.sysbio.biosynthframework.Reaction;
import pt.uminho.sysbio.biosynthframework.io.ReactionDao;

public class etlReaction {


	/**
	 * @param <M>
	 * @param src
	 * @param t
	 * @param graphDatabaseService
	 * Generic ETL process for any ReactionDao, enabling the Extraction, Transformation and Loading of reaction data from any of the project's third party resources
	 */
	public static<M extends Reaction> void etl(ReactionDao<M> src, EtlTransform<M, GraphReactionEntity> t, GraphDatabaseService graphDatabaseService) {

		Neo4jGraphReactionDaoImpl dst = new Neo4jGraphReactionDaoImpl(graphDatabaseService);

		HbmNeo4jHybridReactionEtlPipeline<M, GraphReactionEntity> etlPipeline = new HbmNeo4jHybridReactionEtlPipeline<M, GraphReactionEntity>(null, graphDatabaseService);

		etlPipeline.setSkipLoad(false);
		etlPipeline.setExtractSubsystem(new DefaultReactionEtlExtract<M>(src));
		etlPipeline.setLoadSubsystem(new HeterogenousReactionEtlLoad<GraphReactionEntity>(dst));
		etlPipeline.setTransformSubsystem(t);
		etlPipeline.etl();
	}

}
