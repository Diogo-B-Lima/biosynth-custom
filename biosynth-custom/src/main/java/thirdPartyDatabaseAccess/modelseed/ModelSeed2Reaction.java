package thirdPartyDatabaseAccess.modelseed;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/* This class is used to map the ModelSeed reactions.json attributes to a Java object */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelSeed2Reaction {

    @JsonProperty("abstract_reaction")
	public String abstract_reaction;
    
    @JsonProperty("source")
	public String source;

    @JsonProperty("id")
	public String id;
	
    @JsonProperty("name")
	public String name;

    @JsonProperty("definition")
	public String definition;
    
    @JsonProperty("code")
	public String code;
    
    @JsonProperty("compound_ids")
	public String compound_ids;
    
    @JsonProperty("direction")
	public String direction;

    @JsonProperty("reversibility")
	public String reversibility;
    
    @JsonProperty("linked_reaction")
	public String linked_reaction;
    
    @JsonProperty("equation")
	public String equation;
    
    @JsonProperty("status")
	public String status;
    
    @JsonProperty("stoichiometry")
	public String stoichiometry;
   
    @JsonProperty("deltag")
	public Double deltag;
	
    @JsonProperty("deltagerr")
	public Double deltagerr;

    @JsonProperty("abbreviation")
	public String abbreviation;
	
    @JsonProperty("is_obsolete")
	public Integer is_obsolete;
    
    @JsonProperty("is_transport")
	public Integer is_transport;

    @JsonProperty("notes")
	public List<String> notes = new ArrayList<> ();
	
    @JsonProperty("aliases")
	public List<String> aliases = new ArrayList<> ();
    
    @JsonProperty("ec_numbers")
	public List<String> ec_numbers = new ArrayList<> ();
    
    @JsonProperty("pathways")
	public List<String> pathways = new ArrayList<> ();

    
	
	@Override
	public String toString() {
		return String.format("[%s]%s %s %s:%s", source, id, abbreviation, name);
	}



}
