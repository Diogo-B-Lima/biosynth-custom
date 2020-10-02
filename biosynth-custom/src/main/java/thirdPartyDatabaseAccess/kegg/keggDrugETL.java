package thirdPartyDatabaseAccess.kegg;

import pt.uminho.sysbio.biosynth.integration.etl.biodb.kegg.KeggDrugTransform;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggDrugMetaboliteDaoImpl;

public class keggDrugETL {

	
	/**
	 * @return
	 */
	public static RestKeggDrugMetaboliteDaoImpl getKeggDrugMetaboliteDao() {

		return new RestKeggDrugMetaboliteDaoImpl();
	}
	
	/**
	 * @return
	 */
	public static KeggDrugTransform getKeggDrugTransform() {

		return new KeggDrugTransform();
	}
}
