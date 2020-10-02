package thirdPartyDatabaseAccess.kegg;

import pt.uminho.sysbio.biosynth.integration.etl.biodb.kegg.KeggGlycanTransform;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggGlycanMetaboliteDaoImpl;

public class keggGlycanETL {


		/**
		 * @return
		 */
		public static RestKeggGlycanMetaboliteDaoImpl getKeggGlycanMetaboliteDao() {
	
			return new RestKeggGlycanMetaboliteDaoImpl();
		}
		
		/**
		 * @return
		 */
		public static KeggGlycanTransform getKeggGlycanTransform() {
	
			return new KeggGlycanTransform();
		}
}
