package thirdPartyDatabaseAccess.metacyc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uminho.sysbio.biosynth.integration.GraphMetaboliteEntity;
import pt.uminho.sysbio.biosynth.integration.etl.EtlTransform;
import pt.uminho.sysbio.biosynth.integration.etl.biodb.biocyc.BiocycMetaboliteTransform;
import pt.uminho.sysbio.biosynth.integration.io.dao.neo4j.MetaboliteMajorLabel;
import pt.uminho.sysbio.biosynthframework.biodb.bigg.Bigg2MetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.biodb.bigg.BiggMetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.biodb.biocyc.BioCycMetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.bigg.CsvBiggMetaboliteDaoImpl;
import pt.uminho.sysbio.biosynthframework.core.data.io.dao.biodb.ptools.biocyc.RestBiocycMetaboliteDaoImpl;
import pt.uminho.sysbio.biosynthframework.io.biodb.RestBigg2MetaboliteDaoImpl;
import thirdPartyDatabaseAccess.bigg.biggCompoundETL;


public class metacycCompoundETL {


	/**
	 * @return
	 */
	public static RestBiocycMetaboliteDaoImpl getBiocycMetaboliteDaoImpl() {

		return new RestBiocycMetaboliteDaoImpl();
	}


	/**
	 * @return
	 */
	@Deprecated
	public static EtlTransform<BioCycMetaboliteEntity, GraphMetaboliteEntity> getBioTransform() {

		Map<String, String> biggInternalIdToEntryMap = new HashMap<String, String>();

		CsvBiggMetaboliteDaoImpl bigg1MetabDatabase = biggCompoundETL.getBigg1CompoundMetaboliteDao("");

		for (String cpdEntry : bigg1MetabDatabase.getAllMetaboliteEntries()) {
			BiggMetaboliteEntity cpd = bigg1MetabDatabase.getMetaboliteByEntry(cpdEntry);
			biggInternalIdToEntryMap.put(cpd.getInternalId().toString(), cpdEntry);
		}

		return new BiocycMetaboliteTransform(MetaboliteMajorLabel.MetaCyc.toString(), biggInternalIdToEntryMap);

	}
	
	
	/**
	 * @return
	 */
	public static EtlTransform<BioCycMetaboliteEntity, GraphMetaboliteEntity> getBioTransformFromBiggRest() {
		
		Map<String, String> biggInternalIdToEntryMap = new HashMap<String, String>();

		RestBigg2MetaboliteDaoImpl bigg1MetabDatabase = biggCompoundETL.getRestBigg2CompoundMetaboliteDao();
		
		List<String> metab = bigg1MetabDatabase.getAllMetaboliteEntries();
		
		for (String cpdEntry : metab) {
			Bigg2MetaboliteEntity cpd = bigg1MetabDatabase.getMetaboliteByEntry(cpdEntry);
			biggInternalIdToEntryMap.put(cpd.getEntry(), cpdEntry);
		}

		return new BiocycMetaboliteTransform(MetaboliteMajorLabel.MetaCyc.toString(), biggInternalIdToEntryMap);
		
	}

}