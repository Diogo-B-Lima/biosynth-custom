package thirdPartyDatabaseAccess.bigg;

import java.io.File;

import pt.uminho.sysbio.biosynth.integration.GraphMetaboliteEntity;
import pt.uminho.sysbio.biosynth.integration.etl.EtlTransform;
import pt.uminho.sysbio.biosynth.integration.etl.biodb.bigg.Bigg2MetaboliteTransform;
import pt.uminho.sysbio.biosynth.integration.etl.biodb.bigg.BiggMetaboliteTransform;
import pt.uminho.sysbio.biosynthframework.biodb.bigg.Bigg2MetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.biodb.bigg.BiggMetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.bigg.CsvBiggMetaboliteDaoImpl;
import pt.uminho.sysbio.biosynthframework.core.data.io.factory.BiggDaoFactory;
import pt.uminho.sysbio.biosynthframework.io.biodb.RestBigg2MetaboliteDaoImpl;


public class biggCompoundETL {

	/* BIGG 1 we can only get data from local files
	   BIGG 2 we cant get data from their REST API */

	/**
	 * @param biggMetabolitesPath
	 * @return
	 * Get Bigg data from local files (deprecated as Bigg "1" does not exist anymore)
	 */
	@Deprecated
	public static CsvBiggMetaboliteDaoImpl getBigg1CompoundMetaboliteDao(String biggMetabolitesPath) {

		CsvBiggMetaboliteDaoImpl dao = new BiggDaoFactory().withFile(new File(biggMetabolitesPath)).buildCsvBiggMetaboliteDao();
		return dao;
	}


	/**
	 * @return
	 */
	public static RestBigg2MetaboliteDaoImpl getRestBigg2CompoundMetaboliteDao() {

		RestBigg2MetaboliteDaoImpl dao = new RestBigg2MetaboliteDaoImpl();
		return dao;

	}


	/**
	 * @return
	 */
	public static EtlTransform<Bigg2MetaboliteEntity, GraphMetaboliteEntity> getBigg2MetaboliteTransform() {

		return new Bigg2MetaboliteTransform();
	}

	/**
	 * @return
	 */
	@Deprecated
	public static EtlTransform<BiggMetaboliteEntity, GraphMetaboliteEntity> getBiggMetaboliteTransform() {

		return new BiggMetaboliteTransform();
	}

}