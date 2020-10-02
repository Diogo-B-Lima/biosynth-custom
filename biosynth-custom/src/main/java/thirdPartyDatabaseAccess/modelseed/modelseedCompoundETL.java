package thirdPartyDatabaseAccess.modelseed;


import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;

import pt.uminho.sysbio.biosynth.integration.etl.biodb.ModelSeedMetaboliteTransform;


public class modelseedCompoundETL {


	/**
	 * @param jsonFilePath
	 * @return
	 * @throws IOException
	 */
	public static  Json2ModelSeedMetaboliteDaoImpl getModelSeedCompoundDatabase(String jsonFilePath) throws IOException {

		return new Json2ModelSeedMetaboliteDaoImpl(new FileSystemResource(new File(jsonFilePath)));

	}


	/**
	 * @return
	 */
	public static ModelSeedMetaboliteTransform getModelseedCompoundTransform() {

		return new ModelSeedMetaboliteTransform();


	}

}