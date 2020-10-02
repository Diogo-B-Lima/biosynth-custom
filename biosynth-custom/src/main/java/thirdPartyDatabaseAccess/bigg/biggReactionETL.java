package thirdPartyDatabaseAccess.bigg;

import java.io.File;

import pt.uminho.sysbio.biosynth.integration.etl.biodb.bigg.Bigg2ReactionTransform;
import pt.uminho.sysbio.biosynth.integration.etl.biodb.bigg.BiggReactionTransform;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.bigg.CsvBiggReactionDaoImpl;
import pt.uminho.sysbio.biosynthframework.core.data.io.factory.BiggDaoFactory;
import pt.uminho.sysbio.biosynthframework.io.biodb.RestBigg2ReactionDaoImpl;

public class biggReactionETL {


	/**
	 * @return
	 */
	public static RestBigg2ReactionDaoImpl getBigg2ReactionDao() {

		return new RestBigg2ReactionDaoImpl();
	}

	/**
	 * @param biggReactionsPath
	 * @return
	 */
	@Deprecated
	public static CsvBiggReactionDaoImpl getBigg1ReactionDao(String biggReactionsPath) {

		CsvBiggReactionDaoImpl dao = new BiggDaoFactory().withFile(new File(biggReactionsPath)).buildCsvBiggReactionDao();
		return dao;
	}

	/**
	 * @return
	 */
	public static Bigg2ReactionTransform getBigg2ReactionTransform() {

		return new Bigg2ReactionTransform();
	}

	/**
	 * @return
	 */
	@Deprecated
	public static BiggReactionTransform getBigg1ReactionTransform() {

		return new BiggReactionTransform();
	}
}
