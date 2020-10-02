package thirdPartyDatabaseAccess.modelseed;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.uminho.sysbio.biosynthframework.ReferenceType;
import pt.uminho.sysbio.biosynthframework.biodb.modelseed.ModelSeedMetaboliteCrossreferenceEntity;
import pt.uminho.sysbio.biosynthframework.biodb.modelseed.ModelSeedMetaboliteEntity;
import pt.uminho.sysbio.biosynthframework.io.MetaboliteDao;

public class Json2ModelSeedMetaboliteDaoImpl implements MetaboliteDao<ModelSeedMetaboliteEntity> {

	private static final Logger logger = LoggerFactory.getLogger(Json2ModelSeedMetaboliteDaoImpl.class);

	private Map<String, ModelSeed2Compound> data = new HashMap<> ();

	public static Boolean integerToBoolean(Integer i) {
		if ( i == null) {
			return null;
		}
		return i == 0 ? false : true;
	}

	public Function<ModelSeed2Compound, ModelSeedMetaboliteEntity> function =
			new Function<ModelSeed2Compound, ModelSeedMetaboliteEntity>() {

		@Override
		public ModelSeedMetaboliteEntity apply(ModelSeed2Compound t) {
			ModelSeedMetaboliteEntity cpd = new ModelSeedMetaboliteEntity();
			cpd.setEntry(t.id);
			cpd.setName(t.name);
			cpd.setFormula(t.formula);
			cpd.setAbbreviation(t.abbreviation);
			cpd.setSeedSource(t.source);
			cpd.setSearchInchi(t.inchikey);
			cpd.setCore(integerToBoolean(t.is_core));
			cpd.setCofactor(integerToBoolean(t.is_cofactor));
			cpd.setObsolete(integerToBoolean(t.is_obsolete));
			cpd.setDefaultCharge(t.charge);
			cpd.setDeltaG(t.deltag);
			cpd.setDeltaGErr(t.deltagerr);
			cpd.setMass(t.mass);

			if(t.aliases != null && !t.aliases.isEmpty()) {

				for(String alias: t.aliases) {

					List<ModelSeedMetaboliteCrossreferenceEntity> refs = new ArrayList<> ();

					if(alias.startsWith("KEGG")) {

						String[] keggAliasArray = alias.replace("KEGG: ", "").split("; ");

						for(String keggAlias:keggAliasArray) {

							String database = null;
							switch (keggAlias.substring(0, 1)) {
							case "C": database = "LigandCompound"; break;
							case "G": database = "LigandGlycan"; break;
							default:
								logger.warn("unknown KEGG database - {}", keggAlias);
								break;
							}

							if (database != null) {
								refs.add(new ModelSeedMetaboliteCrossreferenceEntity(
										ReferenceType.DATABASE, database, keggAlias));
							}
						}
					}

					if(alias.startsWith("BiGG")) {
						String[] biggAliasArray = alias.replace("BiGG: ", "").split("; ");

						for (String biggAlias : biggAliasArray) {
							refs.add(new ModelSeedMetaboliteCrossreferenceEntity(
									ReferenceType.DATABASE, "BiGG", biggAlias));
						}

					}

					if(alias.startsWith("MetaCyc")) {
						String[] metacycAliasArray = alias.replace("MetaCyc: ", "").split("; ");

						for (String metacycAlias : metacycAliasArray) {
							refs.add(new ModelSeedMetaboliteCrossreferenceEntity(
									ReferenceType.DATABASE, "MetaCyc", "META:" + metacycAlias));
						}
					}

					cpd.setCrossreferences(refs);

					if(alias.startsWith("Name")) {

						String[] AliasArray = alias.replace("Name: ", "").split("; ");

						for (String name : AliasArray) {
							cpd.getNames().add(name);
						}
					}
				}
			}

			return cpd;
		}
	};

	public Json2ModelSeedMetaboliteDaoImpl(Resource compoundsJson) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		/* Read JSON file as a String  */
		String jsonAsString = IOUtils.toString(compoundsJson.getInputStream(), StandardCharsets.UTF_8);

		/* Read String and build Tree of nodes where each node represents an entry of the JSON file */
		JsonNode tree = mapper.readTree(jsonAsString);
		int treeNodeIndex;

		try {

			for (treeNodeIndex = 0; treeNodeIndex < tree.size(); treeNodeIndex++) { 		

				/* Convert the node to a ModelSeed2Compound object containing all relevant data from a ModelSeed metabolite */
				/* Any missing/extra attribute present in the json file must be declared in the ModelSeed2Compound class*/

				ModelSeed2Compound compound = mapper.treeToValue(tree.get(treeNodeIndex), ModelSeed2Compound.class);
				if (compound != null && compound.id != null && !compound.id.trim().isEmpty()) {
					if (data.put(compound.id, compound) != null) {
						logger.warn("duplicate ID - {}", compound.id);
					}
				} else {
					logger.warn("invalid record - {}", compound);
				}
			}


		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public ModelSeedMetaboliteEntity getMetaboliteById(Serializable id) {
		throw new RuntimeException("Unsupported operation");
	}

	@Override
	public ModelSeedMetaboliteEntity getMetaboliteByEntry(String entry) {
		return function.apply(data.get(entry));
	}

	@Override
	public ModelSeedMetaboliteEntity saveMetabolite(ModelSeedMetaboliteEntity metabolite) {
		throw new RuntimeException("Unsupported operation");
	}

	@Override
	public Serializable saveMetabolite(Object metabolite) {
		throw new RuntimeException("Unsupported operation");
	}

	@Override
	public List<Serializable> getAllMetaboliteIds() {
		throw new RuntimeException("Unsupported operation");
	}

	@Override
	public List<String> getAllMetaboliteEntries() {
		return new ArrayList<> (this.data.keySet());
	}

	@Override
	public Serializable save(ModelSeedMetaboliteEntity entity) {
		throw new RuntimeException("Unsupported operation");
	}

}
