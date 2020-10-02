package thirdPartyDatabaseAccess.modelseed;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;

import pt.uminho.sysbio.biosynth.integration.etl.biodb.ModelSeedReactionTransform;

public class modelseedReactionETL {


	/**
	 * @param jsonFilePath
	 * @return
	 * @throws IOException
	 */
	public static  Json2ModelSeedReactionDaoImpl getModelSeedReactionDatabase(String jsonFilePath) throws IOException {

		return new Json2ModelSeedReactionDaoImpl(new FileSystemResource(new File(jsonFilePath)));

	}


	/**
	 * @return
	 */
	public static ModelSeedReactionTransform getModelseedReactionTransform() {

		return new ModelSeedReactionTransform();

	}
}
