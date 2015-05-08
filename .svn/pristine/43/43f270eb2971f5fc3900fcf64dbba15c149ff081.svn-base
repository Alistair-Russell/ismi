package de.mpiwg.itgroup.ismi.json.utils;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.bo.Relation;
import org.mpi.openmind.repository.utils.RomanizationLoC;

public class JSONUtils {

	private static Logger logger = Logger.getLogger(JSONUtils.class);
	
	//data structure
	public static String ENT = "ent";
	public static String SRC_RELS = "src_rels";
	public static String TAR_RELS = "tar_rels";
	public static String ATTS = "atts";
	public static String OV = "ov";
	public static String NOV = "nov";
	public static String LW = "lw";
	public static String ID = "id";
	public static String OC = "oc";
	public static String NODE_TYPE = "node_type";
	public static String NAME = "name";
	public static String CONTENT_TYPE = "content_type";
	public static String SRC_ID = "src_id";
	public static String SRC_OC = "src_oc";
	public static String TAR_ID = "tar_id";
	public static String TAR_OC = "tar_oc";
	public static String SRC = "src";
	public static String TAR = "tar";
	public static String POSSIBLE_VALUES = "possible_values";
	public static String ROMANIZATION = "romanization";
	
	//methods
	public static String METHOD = "method";
	public static String M_GET_ENT = "get_ent";
	public static String M_GET_ENTS= "get_ents";
	public static String M_GET_ENTS_SIZE  = "get_ents_size";
	public static String M_GET_DEF = "get_def";
	public static String M_GET_DEFS = "get_defs";
	public static String M_GET_SRC_RELS = "get_src_rels";
	public static String M_GET_TAR_RELS = "get_tar_rels";
	public static String M_GET_ATTS = "get_atts";
	public static String M_GET_TARS_4_SRC_REL = "get_tars_4_src_rel";
	public static String M_GET_SRCS_4_TAR_REL = "get_srcs_4_tar_rel";
	public static String M_EXIST_RELATION = "exist_rel";
	public static String M_GET_RELS = "get_rels";
	public static String M_SEARCH = "search";
	public static String M_GET_PUBLIC_CODICES = "get_public_codices";
	public static String M_WITNESSES_4_CODEX = "get_witnisses_4_codex";
	
	//parameters
	public static String P_METHOD = "method";
	public static String P_MR = "max_results";
	public static String P_INCLUDE = "include_content";
	public static String P_DEPTH = "depth";
	public static String P_ID = "id";
	public static String P_MPIWGID = "mpiwg_id";
	public static String P_IDS = "ids";
	public static String P_OC = "oc";
	public static String P_REL_NAME = "rel_name";
	public static String P_SRC_OC = "src_oc";
	public static String P_TAR_OC = "tar_oc";
	public static String P_SRC_ID = "src_id";
	public static String P_TAR_ID = "tar_id";
	public static String P_INCL_ENTS = "include_ents";
	public static String P_INCL_ROMANIZATION = "include_romanization";
	
	//returns
	public static String R_ENTS = "ents";
	public static String R_DEF = "def";
	public static String R_DEFS = "defs";
	public static String R_ENTS_SIZE = "ents_size";
	public static String R_ATTS = "att_array";
	public static String R_RELS = "rels";
	public static String R_RELS_SIZE = "rels_size";
	public static String R_SRC_RELS = "src_rels";
	public static String R_TAR_RELS = "tar_rels";
	public static String R_EXIST_REL = "exist_rel";
	
	public static JSONObject entityToJSON(
			Entity entity, 
			List<Attribute> attList, 
			List<Relation> srcList, 
			List<Relation> tarList,
			boolean includeRomanization) throws JSONException{
		
		JSONObject json = entityToJSON(entity, includeRomanization);
		
		JSONArray attArray = new JSONArray();
		for(Attribute att : attList){
			if(StringUtils.isNotEmpty(att.getOwnValue())){
				attArray.put(attributeToJSON(att, includeRomanization));
			}
		}
		json.put(ATTS, attArray);
		
		JSONArray srcRel = new JSONArray();
		for(Relation rel : srcList){
			srcRel.put(relationToJSON(rel, includeRomanization));
		}
		json.put(SRC_RELS, srcRel);
		
		JSONArray tarRel = new JSONArray();
		for(Relation rel : tarList){
			tarRel.put(relationToJSON(rel, includeRomanization));
		}
		json.put(TAR_RELS, tarRel);
		
		return json;
	}
	
	public static JSONObject entityToJSONWithAtts(Entity entity, List<Attribute> atts, boolean includeRomanization) throws JSONException{
		JSONObject json = new JSONObject();
		
		json.put(OV, entity.getOwnValue());
		json.put(NOV, entity.getNormalizedOwnValue());
		json.put(LW, true);
		json.put(ID, entity.getId());
		json.put(OC, entity.getObjectClass());
		json.put(NODE_TYPE, entity.getType());
		
		JSONArray attArray = new JSONArray();
		for(Attribute att : atts){
			if(StringUtils.isNotEmpty(att.getOwnValue())){
				attArray.put(attributeToJSON(att, includeRomanization));
			}
		}
		json.put(ATTS, attArray);
		return json;
	}
	
	public static JSONObject entityToJSON(Entity entity, int depth, WrapperService ws, boolean includeRomanization) throws JSONException{
		JSONObject json = new JSONObject();
		
		json.put(OV, entity.getOwnValue());
		json.put(NOV, entity.getNormalizedOwnValue());
		json.put(LW, entity.isLightweight());
		json.put(ID, entity.getId());
		json.put(OC, entity.getObjectClass());
		json.put(NODE_TYPE, entity.getType());
		
		
		if(!entity.isLightweight()){
			JSONArray attArray = new JSONArray();
			for(Attribute att : entity.getAttributes()){
				if(StringUtils.isNotEmpty(att.getOwnValue())){
					attArray.put(attributeToJSON(att, includeRomanization));
				}
			}
			json.put(ATTS, attArray);
			
			JSONArray srcRel = new JSONArray();
			for(Relation rel : entity.getSourceRelations()){
				srcRel.put(relationToJSON(rel, depth, ws, includeRomanization));
			}
			json.put(SRC_RELS, srcRel);
			
			JSONArray tarRel = new JSONArray();
			for(Relation rel : entity.getTargetRelations()){
				tarRel.put(relationToJSON(rel, depth, ws, includeRomanization));
			}
			json.put(TAR_RELS, tarRel);
		}
		
		return json;
	}
	
	public static Entity jsonToEntity(JSONObject json) throws JSONException{
		Entity ent = new Entity();
		
		ent.setOwnValue(json.getString(OV));
		ent.setNormalizedOwnValue(json.getString(NOV));
		ent.setLightweight(json.getBoolean(LW));
		ent.setId(json.getLong(ID));
		ent.setObjectClass(json.getString(OC));
		ent.setType(json.getString(NODE_TYPE));
		
		if(json.has(ATTS)){
			JSONArray jsonAtts = json.getJSONArray(ATTS);
			for(int i=0; i<jsonAtts.length(); i++){
				ent.getAttributes().add(jsonToAttribute(jsonAtts.getJSONObject(i)));
			}	
		}
		
		if(json.has(SRC_RELS)){
			JSONArray srcRels = json.getJSONArray(SRC_RELS);
			for(int i=0; i<srcRels.length(); i++){
				//TODO ent.getSourceRelations().add(e);
			}	
		}
		
		if(json.has(TAR_RELS)){
			JSONArray tarRels = json.getJSONArray(TAR_RELS);
			for(int i=0; i<tarRels.length(); i++){
				//TODO ent.getTargetRelations().add(e);
			}	
		}
		
		return ent;
	}
	
	public static JSONObject entityToJSON(Entity entity, boolean includeRomanization) throws JSONException{
		JSONObject json = new JSONObject();
		
		json.put(OV, entity.getOwnValue());
		json.put(NOV, entity.getNormalizedOwnValue());
		json.put(LW, entity.isLightweight());
		json.put(ID, entity.getId());
		json.put(OC, entity.getObjectClass());
		json.put(NODE_TYPE, entity.getType());
		if(includeRomanization){
			json.put(ROMANIZATION, RomanizationLoC.convert(entity.getOwnValue()));
		}
		
		
		if(!entity.isLightweight()){
			JSONArray attArray = new JSONArray();
			for(Attribute att : entity.getAttributes()){
				if(StringUtils.isNotEmpty(att.getOwnValue())){
					attArray.put(attributeToJSON(att, includeRomanization));
				}
			}
			json.put(ATTS, attArray);
			
			JSONArray srcRel = new JSONArray();
			for(Relation rel : entity.getSourceRelations()){
				srcRel.put(relationToJSON(rel, includeRomanization));
			}
			json.put(SRC_RELS, srcRel);
			
			JSONArray tarRel = new JSONArray();
			for(Relation rel : entity.getTargetRelations()){
				tarRel.put(relationToJSON(rel, includeRomanization));
			}
			json.put(TAR_RELS, tarRel);
		}
		
		return json;
	}
	
	public static JSONObject attributeToJSON(Attribute att, boolean includeRomanization) throws JSONException{
		JSONObject json = new JSONObject();
		
		json.put(NAME, att.getName());
		json.put(OV, att.getOwnValue());
		json.put(NOV, att.getNormalizedOwnValue());
		json.put(SRC_ID, att.getSourceId());
		json.put(ID, att.getId());
		json.put(CONTENT_TYPE, att.getContentType());
		json.put(NODE_TYPE, att.getType());
		json.put(POSSIBLE_VALUES, att.getPossibleValues());
		if(includeRomanization){
			json.put(ROMANIZATION, RomanizationLoC.convert(att.getOwnValue()));
		}
		
		return json;
	}
	
	public static Attribute jsonToAttribute(JSONObject json) throws JSONException{
		
		Attribute att = new Attribute();
		
		att.setName(json.getString(NAME));
		
		if(json.has(OV))
			att.setOwnValue(json.getString(OV));
		
		if(json.has(NOV))
			att.setNormalizedOwnValue(json.getString(NOV));
		
		att.setSourceId(json.getLong(SRC_ID));
		att.setId(json.getLong(ID));
		att.setContentType(json.getString(CONTENT_TYPE));
		att.setType(json.getString(NODE_TYPE));
		if(json.has(POSSIBLE_VALUES))
			att.setPossibleValues(json.getString(POSSIBLE_VALUES));
		
		return att;
	}
	
	public static JSONObject relationToJSON(Relation rel, 
			Entity src, 
			Entity tar, 
			List<Attribute> srcAtts, 
			List<Attribute> tarAtts,
			boolean includeRomanization) throws JSONException{
		
		JSONObject json = new JSONObject();
		
		json.put(NAME, rel.getOwnValue());
		json.put(ID, rel.getId());
		
		json.put(SRC_ID, rel.getSourceId());
		json.put(SRC_OC, rel.getSourceObjectClass());
		
		json.put(TAR_ID, rel.getTargetId());
		json.put(TAR_OC, rel.getTargetObjectClass());
		
		json.put(SRC, entityToJSONWithAtts(src, srcAtts, includeRomanization));
		json.put(TAR, entityToJSONWithAtts(tar, tarAtts, includeRomanization));
		
		JSONArray attArray = new JSONArray();
		for(Attribute att : rel.getAttributes()){
			attArray.put(attributeToJSON(att, includeRomanization));
		}
		json.put(ATTS, attArray);
		
		return json;
	}
	
	
	public static JSONObject relationToJSON(Relation rel, boolean includeRomanization) throws JSONException{
		JSONObject json = new JSONObject();
		
		json.put(NAME, rel.getOwnValue());
		json.put(ID, rel.getId());
		
		json.put(SRC_ID, rel.getSourceId());
		json.put(SRC_OC, rel.getSourceObjectClass());
		
		json.put(TAR_ID, rel.getTargetId());
		json.put(TAR_OC, rel.getTargetObjectClass());
		
		JSONArray attArray = new JSONArray();
		for(Attribute att : rel.getAttributes()){
			if(StringUtils.isNotEmpty(att.getOwnValue())){
				attArray.put(attributeToJSON(att, includeRomanization));
			}
		}
		json.put(ATTS, attArray);
		
		return json;
	}
	
	public static JSONObject relationToJSON(Relation rel, int depth, WrapperService ws, boolean includeRomanization) throws JSONException{
		JSONObject json = new JSONObject();
		
		json.put(NAME, rel.getOwnValue());
		json.put(ID, rel.getId());
		
		json.put(SRC_ID, rel.getSourceId());
		json.put(SRC_OC, rel.getSourceObjectClass());
		
		json.put(TAR_ID, rel.getTargetId());
		json.put(TAR_OC, rel.getTargetObjectClass());
		
		if(depth > 0){
			Entity src = ws.getEntityByIdWithContent(rel.getSourceId());
			Entity tar = ws.getEntityByIdWithContent(rel.getTargetId());
			if(src != null && tar != null){
				json.put(SRC, entityToJSON(src, depth - 1, ws, includeRomanization));
				json.put(TAR, entityToJSON(tar, depth - 1, ws, includeRomanization));
			}else{
				try {
					throw new Exception("Inconsistency. Relation has either no source or target. " + rel.toString());
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		
		JSONArray attArray = new JSONArray();
		for(Attribute att : rel.getAttributes()){
			if(StringUtils.isNotEmpty(att.getOwnValue())){
				attArray.put(attributeToJSON(att, includeRomanization));
			}
		}
		json.put(ATTS, attArray);
		
		return json;
	}
	
	public static boolean getBoolean(JSONObject json, String name){
		try {
			if(json.has(name)){
				return json.getBoolean(name);
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public static String getString(JSONObject json, String name){
		try {
			if(json.has(name)){
				return json.getString(name);
			}
		} catch (Exception e) {
		}
		return "";
	}
}
