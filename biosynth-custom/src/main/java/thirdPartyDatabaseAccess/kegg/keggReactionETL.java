package thirdPartyDatabaseAccess.kegg;

import pt.uminho.sysbio.biosynth.integration.etl.biodb.kegg.KeggReactionTransform;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.kegg.RestKeggReactionDaoImpl;

public class keggReactionETL {


		/**
		 * @return
		 */
		public static RestKeggReactionDaoImpl getKeggReactionDao() {
	
			return new RestKeggReactionDaoImpl();
		}
		
		/**
		 * @return
		 */
		public static KeggReactionTransform getKeggReactionTransform() {
	
			return new KeggReactionTransform();
		}
}
