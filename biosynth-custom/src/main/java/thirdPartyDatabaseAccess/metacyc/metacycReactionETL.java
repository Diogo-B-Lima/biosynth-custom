package thirdPartyDatabaseAccess.metacyc;

import pt.uminho.sysbio.biosynth.integration.etl.biodb.biocyc.BiocycReactionTransform;
import pt.uminho.sysbio.biosynth.integration.io.dao.neo4j.ReactionMajorLabel;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.ptools.biocyc.RestBiocycReactionDaoImpl;

public class metacycReactionETL {


	/**
	 * @return
	 */
	public static RestBiocycReactionDaoImpl getRestBiocycReactionDaoImpl() {

		return new RestBiocycReactionDaoImpl();
	}

	/**
	 * @return
	 */
	public static BiocycReactionTransform getBiocycReactionTransform() {

		return new BiocycReactionTransform(ReactionMajorLabel.MetaCyc.toString());
	}
}
