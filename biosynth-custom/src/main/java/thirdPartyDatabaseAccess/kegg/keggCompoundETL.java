package thirdPartyDatabaseAccess.kegg;

import pt.uminho.sysbio.biosynth.integration.etl.biodb.kegg.KeggCompoundTransform;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggCompoundMetaboliteDaoImpl;

public class keggCompoundETL {


	/**
	 * @return
	 */
	public static RestKeggCompoundMetaboliteDaoImpl getKeggCompoundMetaboliteDao() {

		return  new RestKeggCompoundMetaboliteDaoImpl();
	}


	/**
	 * @return
	 */
	public static KeggCompoundTransform getKeggCompoundTransform() {

		return new KeggCompoundTransform();
	}


}