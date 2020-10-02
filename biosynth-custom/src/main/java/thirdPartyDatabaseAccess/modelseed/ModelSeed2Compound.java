package thirdPartyDatabaseAccess.modelseed;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/* This class is used to map the ModelSeed compounds.json attributes to a Java object */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelSeed2Compound {

    @JsonProperty("abstract_compound")
	public String abstract_compound;
    
    @JsonProperty("source")
	public String source;
    
    @JsonProperty("smiles")
	public String smiles;

    @JsonProperty("id")
	public String id;
    
    @JsonProperty("comprised_of")
	public String comprised_of;
    
    @JsonProperty("inchikey")
	public String inchikey;
	
    @JsonProperty("name")
	public String name;

    @JsonProperty("formula")
	public String formula;
    
    @JsonProperty("linked_compound")
	public String linked_compound;
    
    @JsonProperty("charge")
	public Integer charge;

    @JsonProperty("deltag")
	public Double deltag;
	
    @JsonProperty("deltagerr")
	public Double deltagerr;

    @JsonProperty("mass")
	public Double mass;

    @JsonProperty("abbreviation")
	public String abbreviation;

    @JsonProperty("is_cofactor")
	public Integer is_cofactor;
	
    @JsonProperty("is_core")
	public Integer is_core;
	
    @JsonProperty("is_obsolete")
	public Integer is_obsolete;

    @JsonProperty("notes")
	public List<String> notes = new ArrayList<> ();
	
    @JsonProperty("aliases")
	public List<String> aliases = new ArrayList<> ();
    
    @JsonProperty("pka")
	public String pka = new String();
	
    @JsonProperty("pkb")
	public String pkb = new String();
	
	@Override
	public String toString() {
		return String.format("[%s]%s %s %s:%s", source, id, formula, abbreviation, name);
	}



}
