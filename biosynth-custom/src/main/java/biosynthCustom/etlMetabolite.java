package biosynthCustom;

import org.neo4j.graphdb.GraphDatabaseService;

import pt.uminho.sysbio.biosynth.integration.GraphMetaboliteEntity;
import pt.uminho.sysbio.biosynth.integration.etl.CentralMetaboliteEtlDataCleansing;
import pt.uminho.sysbio.biosynth.integration.etl.DefaultMetaboliteEtlExtract;
import pt.uminho.sysbio.biosynth.integration.etl.EtlTransform;
import pt.uminho.sysbio.biosynth.integration.etl.HbmNeo4jHybridMetaboliteEtlPipeline;
import pt.uminho.sysbio.biosynth.integration.etl.HeterogenousMetaboliteEtlLoad;
import pt.uminho.sysbio.biosynth.integration.io.dao.neo4j.Neo4jGraphMetaboliteDaoImpl;
import pt.uminho.sysbio.biosynthframework.Metabolite;
import pt.uminho.sysbio.biosynthframework.chemanalysis.cdk.CdkWrapper;
import pt.uminho.sysbio.biosynthframework.io.MetaboliteDao;

public class etlMetabolite {


	/**
	 * @param <M>
	 * @param src
	 * @param t
	 * @param graphDatabaseService
	 * Generic ETL process for any MetaboliteDao, enabling the Extraction, Transformation and Loading of metabolite data from any of the project's third party resources
	 */
	public static<M extends Metabolite> void etl(MetaboliteDao<M> src, EtlTransform<M, GraphMetaboliteEntity> t, GraphDatabaseService graphDatabaseService) {

		Neo4jGraphMetaboliteDaoImpl dst = new Neo4jGraphMetaboliteDaoImpl(graphDatabaseService);

		HbmNeo4jHybridMetaboliteEtlPipeline<M, GraphMetaboliteEntity> etlPipeline = new HbmNeo4jHybridMetaboliteEtlPipeline<M, GraphMetaboliteEntity>();

		etlPipeline.setSkipLoad(false);
		etlPipeline.setGraphDatabaseService(graphDatabaseService);
		etlPipeline.setSessionFactory(null);
		etlPipeline.setEtlDataCleasingSubsystem(new CentralMetaboliteEtlDataCleansing(new CdkWrapper()));
		etlPipeline.setExtractSubsystem(new DefaultMetaboliteEtlExtract<M>(src));
		etlPipeline.setLoadSubsystem(new HeterogenousMetaboliteEtlLoad<GraphMetaboliteEntity>(dst));
		etlPipeline.setTransformSubsystem(t);
		etlPipeline.etl();
	}
	
}

