package thirdPartyDatabaseAccess.modelseed;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.uminho.sysbio.biosynthframework.ReferenceType;
import pt.uminho.sysbio.biosynthframework.biodb.modelseed.ModelSeedReactionCrossreferenceEntity;
import pt.uminho.sysbio.biosynthframework.biodb.modelseed.ModelSeedReactionEntity;
import pt.uminho.sysbio.biosynthframework.io.ReactionDao;

public class Json2ModelSeedReactionDaoImpl implements ReactionDao<ModelSeedReactionEntity> {

	private static final Logger logger = LoggerFactory.getLogger(Json2ModelSeedReactionDaoImpl.class);

	private Map<String, ModelSeed2Reaction> data = new HashMap<> ();

	public static Boolean integerToBoolean(Integer i) {
		if ( i == null) {
			return null;
		}
		return i == 0 ? false : true;
	}

	public Function<ModelSeed2Reaction, ModelSeedReactionEntity> function =
			new Function<ModelSeed2Reaction, ModelSeedReactionEntity>() {

		@Override
		public ModelSeedReactionEntity apply(ModelSeed2Reaction t) {
			ModelSeedReactionEntity cpd = new ModelSeedReactionEntity();

			cpd.setEntry(t.id);
			cpd.setName(t.name);
			cpd.setAbbreviation(t.abbreviation);
			cpd.setObsolete(integerToBoolean(t.is_obsolete));
			cpd.setCode(t.code);
			cpd.setDefinition(t.definition);
			cpd.setDeltag(t.deltag);
			cpd.setDeltagerr(t.deltagerr);
			cpd.setDirection(t.direction);
			cpd.setEc(t.ec_numbers);
			cpd.setEquation(t.equation);
			cpd.setReversibility(t.reversibility);
			cpd.setSource(t.source);
			cpd.setStatus(t.status);

			//cpd.setReactantStoichiometry(reactantStoichiometry);
			//cpd.setProductStoichiometry(productStoichiometry);

			if(t.aliases != null && !t.aliases.isEmpty()) {

				for(String alias: t.aliases) {

					List<ModelSeedReactionCrossreferenceEntity> refs = new ArrayList<> ();

					if(alias.startsWith("KEGG")) {

						String[] keggAliasArray = alias.replace("KEGG: ", "").split("; ");

						for(String keggAlias:keggAliasArray) {

							if (keggAlias != null) {
								refs.add(new ModelSeedReactionCrossreferenceEntity(ReferenceType.DATABASE, "KEGG", keggAlias));
							}
						}
					}

					if(alias.startsWith("BiGG")) {
						String[] biggAliasArray = alias.replace("BiGG: ", "").split("; ");

						for (String biggAlias : biggAliasArray) {
							refs.add(new ModelSeedReactionCrossreferenceEntity(
									ReferenceType.DATABASE, "BiGG", biggAlias));
						}

					}

					if(alias.startsWith("MetaCyc")) {
						String[] metacycAliasArray = alias.replace("MetaCyc: ", "").split("; ");

						for (String metacycAlias : metacycAliasArray) {
							refs.add(new ModelSeedReactionCrossreferenceEntity(
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

	public Json2ModelSeedReactionDaoImpl(Resource reactionsJson) throws IOException {

		ObjectMapper mapper = new ObjectMapper();

		/* Read JSON file as a String  */
		String jsonAsString = IOUtils.toString(reactionsJson.getInputStream(), StandardCharsets.UTF_8);

		/* Read String and build Tree of nodes where each node represents an entry of the JSON file */
		JsonNode tree = mapper.readTree(jsonAsString);
		int treeNodeIndex;

		try {

			for (treeNodeIndex = 0; treeNodeIndex < tree.size(); treeNodeIndex++) {

				/* Convert the node to a ModelSeed2Reaction object containing all relevant data from a ModelSeed reaction */
				/* Any missing/extra attribute present in the json file must be declared in the ModelSeed2Reaction class*/

				ModelSeed2Reaction reaction = mapper.treeToValue(tree.get(treeNodeIndex), ModelSeed2Reaction.class);
				if (reaction != null && reaction.id != null && !reaction.id.trim().isEmpty()) {
					if (data.put(reaction.id, reaction) != null) {
						logger.warn("duplicate ID - {}", reaction.id);
					}
				} else {
					logger.warn("invalid record - {}", reaction);
				}
			}


		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public Set<String> getAllReactionEntries() {

		return new HashSet<> (this.data.keySet());
	}

	@Override
	public Set<Long> getAllReactionIds() {
		throw new RuntimeException("Unsupported operation");
	}

	@Override
	public ModelSeedReactionEntity getReactionByEntry(String entry) {
		return function.apply(data.get(entry));
	}

	@Override
	public ModelSeedReactionEntity getReactionById(Long arg0) {
		throw new RuntimeException("Unsupported operation");
	}

	@Override
	public ModelSeedReactionEntity saveReaction(ModelSeedReactionEntity arg0) {
		throw new RuntimeException("Unsupported operation");
	}

}
