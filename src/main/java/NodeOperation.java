
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.bogo.models.Node;
/**
 * This library was designed to operate on a json structure that wants to simulate a filesystem.
 * It is part of a larger project, but with small adaptations it can be used by anyone who wants to operate on json nodes.
 * The library uses the Jackson json library; So if you want NodeOperation in the project you have to add the Jackson 2.12 library as well
 * @author bogojob@gmail.com
 * @version 0.1.0
 *
 */

public class NodeOperation {

	private static String JSONSTRUCTURE = "jsonstructure";
	private static String PROCESSEDNODE = "processednode";
	private Map<String, String> opResult = new HashMap<String, String>();
	private String jsonSchema;
	private String nodeToSearch;
	private ObjectNode addElement;
	private String newname = null;
	private ObjectNode nodetofind = null;
	private JsonNode json;
	private JSON_OP operation;
	private boolean nodeFound = false;
	private List<String> arrayParent = new ArrayList<String>();
	private ObjectMapper mapper = new ObjectMapper();
	int incrementSize = 0;

	public NodeOperation() {
		super();
	}

	/**
	 * Create a node 
	 * @return ObjectNode
	 */
	public ObjectNode createNode(String name, String type, String mime, int size) {

		Node node = new Node();
		node.setId(getRandomHex());
		node.setName(name);
		node.setSize(size);
		node.setType(type);
		node.setMime(type.contains(Costants.NODE_TYPE_DIR) ? "unavailable" : mime);
		return (ObjectNode) mapper.convertValue(node, JsonNode.class);

	}
/**
 * Create a node with default parameters
 * @return ObjectNode
 */
	public ObjectNode createNode() {

		Node node = new Node();
		node.setId(getRandomHex());
		node.setName(getRandomHex());
		node.setSize(0);
		node.setType(Costants.NODE_TYPE_DIR);
		node.setMime("unavailable");
		return (ObjectNode) mapper.convertValue(node, JsonNode.class);

	}

	/**
	 * Create a Hex String from a random number. It's used like id for each node
	 * @return Hex String
	 */
	private String getRandomHex() {
		SecureRandom random = new SecureRandom();
		String s = String.format("%06x", random.nextInt(0x1000000));
		return s;
	}

	/**
	 * Locate the node within the json structure: if found, apply the request
	 * operation on it.
	 * 
	 * @param search:   node to Locate
	 * @param jsonNode: Json Structure data
	 * @return Json data structure modified from operation
	 */

	private JsonNode nodeLocate(String search, JsonNode jsonNode) throws JsonMappingException, JsonProcessingException {
		ObjectNode objectNode = null;

		if (jsonNode.isObject()) {
			objectNode = (ObjectNode) jsonNode;

			Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();

			while (iter.hasNext()) {
				Map.Entry<String, JsonNode> entry = iter.next();
				// System.out.println(" entry evalutation -> " + entry.getKey() + " val: " +
				// entry.getValue().asText());
				if (entry.getKey().contains("id") && (entry.getValue().asText()).contains(search)) {
					// System.out.println("oggetto trovato");
					setNodeFound(true);
					ObjectNode on = null;
					switch (operation) {
					case ADD:
						on = getAddElement();
						setIncrementSize(Integer.parseInt(on.get("size").asText()));

						if (objectNode.get("type").asText()
								.contains("directory")) { /* check id destination is directory type! */
							// increment il size della directory
							int k = Integer.parseInt(objectNode.get("size").asText()) + getIncrementSize();
							objectNode.put("size", k);
							ArrayNode an = (ArrayNode) objectNode.get("childs");
							on.put("id", on.get("id").asText().concat("-")
									.concat(objectNode.get("id").asText()));/* add parent id to new node id */
							an.add(on);
							this.opResult.put(PROCESSEDNODE, mapper.writeValueAsString(on));
						} else {
							System.out.println("Error: destination node not isn't a directory");
						}

						break;

					case UPDATESIZE:
						int k = Integer.parseInt(objectNode.get("size").asText()) + getIncrementSize();
						objectNode.put("size", k);
						break;

					case RENAME:
						objectNode.put("name", getNewname());
						break;

					case REMOVE:
						ArrayNode childs = (ArrayNode) objectNode.get("childs");
						for (int i = 0; i < childs.size(); i++) {
							on = (ObjectNode) childs.get(i);
							if (on.get("id").asText().contains(arrayParent.get(0))) {
								on.put("id", arrayParent.get(0));
								this.opResult.put(PROCESSEDNODE, mapper.writeValueAsString(on));
								childs.remove(i);
								this.opResult.put(JSONSTRUCTURE, mapper.writeValueAsString(jsonNode));
							}
						}
						break;
					default:
						setNodetofind(objectNode);
						this.opResult.put(PROCESSEDNODE, mapper.writeValueAsString(on));
						this.opResult.put(JSONSTRUCTURE, mapper.writeValueAsString(jsonNode));
						break;
					}

					if (isNodeFound()) {
						break;
					}
				}

				nodeLocate(search, entry.getValue());
			}

		} else if (jsonNode.isArray()) {
			ArrayNode arrayNode = (ArrayNode) jsonNode;
			for (int i = 0; i < arrayNode.size(); i++) {
				nodeLocate(search, arrayNode.get(i));
				if (isNodeFound()) {
					break;
				}
			}
		}

		return jsonNode;

	}

	/**
	 * Add a new node to root tree
	 * @param node to add
	 * @param jsondata
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	private void addToRoot(ObjectNode node, String jsondata) throws JsonMappingException, JsonProcessingException {
		JsonNode jsonstructure = null;
		String nodename = node.get("id").asText();
		if (nodename.contains("-")) {
			String[] w = nodename.split("-");
			node.put("id", w[0]);
		}
		jsonstructure = mapper.readTree(jsondata);
		ArrayNode childs = (ArrayNode) jsonstructure.path("userfilesystem").get("nodes");
		childs.add(node);
		this.opResult.put(PROCESSEDNODE, mapper.writeValueAsString(node));
		this.opResult.put(JSONSTRUCTURE, mapper.writeValueAsString(jsonstructure));
		// return opResult;

	}

	/**
	 * Remove a node from root tree
	 * @param nodename : node to remove
	 * @param jsondata
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	private void removeFromRoot(String nodename, String jsondata) throws JsonMappingException, JsonProcessingException {
		JsonNode jsonstructure = null;

		jsonstructure = mapper.readTree(jsondata);
		ArrayNode childs = (ArrayNode) jsonstructure.path("userfilesystem").get("nodes");
		for (int i = 0; i < childs.size(); i++) {
			ObjectNode on = (ObjectNode) childs.get(i);
			if (on.get("id").asText().contains(nodename)) {
				childs.remove(i);
				this.opResult.put(PROCESSEDNODE, mapper.writeValueAsString(on));
				this.opResult.put(JSONSTRUCTURE, mapper.writeValueAsString(jsonstructure));
				break;
			}
		}

	}

	/**
	 * Add a Node into
	 *  @param nodename:   Node that will receive the new node. the new knot will be his son: if NULL the
	 *                    JsonNode is added to root. If != NULL JsonNode is added
	 *                    into child field of the parent if and only if parent type
	 *                    is directory
	 * @param parent:     String identified parent where add objectnode
	 * @return: opResult
	 * */
	public Map<String, String> add(String nodename, String jsondata, ObjectNode newobject)
			throws JsonMappingException, JsonProcessingException {

		JsonNode jsonstructure = null;
		JsonNode jsonadded = null;
		setOperation(JSON_OP.ADD);
		setNodeFound(false);
		setAddElement(newobject);
		jsonstructure = mapper.readTree(jsondata);
		if (nodename.contains("root")) { /* if nodename is null adde is make to root */
			ArrayNode an = (ArrayNode) jsonstructure.path("userfilesystem").get("nodes");
			int tot = ((jsonstructure.get("userfilesystem")).get("size").asInt())
					+ (getAddElement().get("size").asInt());
			((ObjectNode) jsonstructure.get("userfilesystem")).put("size", tot);
			an.add(getAddElement());
			this.opResult.put(JSONSTRUCTURE, mapper.writeValueAsString(jsonstructure));
			this.opResult.put(PROCESSEDNODE, mapper.writeValueAsString(getAddElement()));
		} else {

			jsonadded = nodeLocate(nodename, jsonstructure);
			this.opResult.put(JSONSTRUCTURE, mapper.writeValueAsString(jsonadded));
		}
		return opResult;
	}

	/**
	 * 
	 * @param nodename node to update the quantity of 
	 * @param jsondata
	 * @param size
	 * @return opresult
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	
	public Map<String, String> updatesize(String nodename, String jsondata, int size)
			throws JsonMappingException, JsonProcessingException {

		JsonNode jsonstructure = null;
		JsonNode jsonadded = null;

		setOperation(JSON_OP.UPDATESIZE);
		setNodeFound(false);
		setIncrementSize(size);
		jsonstructure = mapper.readTree(jsondata);
		int tot = ((jsonstructure.get("userfilesystem")).get("size").asInt()) + size;
		((ObjectNode) jsonstructure.get("userfilesystem")).put("size", tot);

		jsonadded = nodeLocate(nodename, jsonstructure);

		this.opResult.put(JSONSTRUCTURE, mapper.writeValueAsString(jsonadded));

		return opResult;
	}

	/**
	 * Remove a node from json structure
	 * 
	 * @param nodename
	 * @param jsondata
	 * @return opResult
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	public Map<String, String> remove(String nodename, String jsondata)
			throws JsonMappingException, JsonProcessingException {

		JsonNode jsonstructure = null;
		JsonNode jsonremoved = null;
		String nodetoLocate = null;
		setOperation(JSON_OP.REMOVE);
		setNodeFound(false);
		if (nodename.contains("-")) {
			arrayParent = Arrays.asList(nodename.split("-"));
			nodetoLocate = arrayParent.get(1);
			jsonstructure = mapper.readTree(jsondata);
			jsonremoved = nodeLocate(nodetoLocate, jsonstructure);
			this.opResult.put(JSONSTRUCTURE, mapper.writeValueAsString(jsonremoved));

		} else {

			removeFromRoot(nodename, jsondata);

		}
		return opResult;
	}

	/**
	 * Moves a node inside a JSON structure
	 * 
	 * @param nodename: node that must moved
	 * @param jsondata 
	 * @param destnode: destination node. null is destination node is root
	 * @return opResult
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	public Map<String, String> move(String srcnodename, String jsondata, String destnodename)
			throws JsonMappingException, JsonProcessingException {

		JsonNode jsonstructure = null;
		String nodetoLocate = null;
		ObjectNode noderemoved = null;
		setOperation(JSON_OP.REMOVE);
		setNodeFound(false);
		jsonstructure = mapper.readTree(jsondata);
		if (srcnodename.contains("-")) {
			arrayParent = Arrays.asList(srcnodename.split("-"));
			nodetoLocate = arrayParent.get(1);

			jsonstructure = nodeLocate(nodetoLocate, jsonstructure);
			setNodeFound(false);
			noderemoved = (ObjectNode) mapper.readTree(opResult.get(PROCESSEDNODE));
			add(destnodename, mapper.writeValueAsString(jsonstructure), noderemoved);
		} else {
			removeFromRoot(srcnodename, jsondata);
			noderemoved = (ObjectNode) mapper.readTree(this.opResult.get(PROCESSEDNODE));

			jsonstructure = mapper.readTree(opResult.get(JSONSTRUCTURE));
			if (destnodename.contains("root")) {
				addToRoot(noderemoved, opResult.get(JSONSTRUCTURE));
			} else {
				setOperation(JSON_OP.ADD);
				add(destnodename, mapper.writeValueAsString(jsonstructure), noderemoved);
			}
		}
		return opResult;
	}

	/**
	 * Rename a Node to Json Structure
	 * 
	 * @param nodename to rename
	 * @param jsondata: 
	 * @Param newname: new name of node
	 * @return opResult
	 * 
	 * 
	 */
	public Map<String, String> rename(String nodename, String jsondata, String newname)
			throws JsonMappingException, JsonProcessingException {

		JsonNode jsonstructure = null;

		setNewname(newname);
		setOperation(JSON_OP.RENAME);
		setNodeFound(false);
		jsonstructure = mapper.readTree(jsondata);
		nodeLocate(nodename, jsonstructure);
		this.opResult.put(JSONSTRUCTURE, mapper.writeValueAsString(jsonstructure));
		return opResult;
	}

	/**
	 * Locate a node into json structure
	 * 
	 * @param nodename name of node to locate
	 * @param jsondata
	 * @return jsonnode found else null
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */

	public JsonNode nodeExist(String nodename, String jsondata) throws JsonMappingException, JsonProcessingException {
		JsonNode jsonstructure = mapper.readTree(jsondata);
		setOperation(JSON_OP.NONE);
		nodeLocate(nodename, jsonstructure);
		return getNodetofind();
	}

	/** getter e setter **/

	public void setJsonSchema(String jsonSchema) {
		this.jsonSchema = jsonSchema;
	}

	public String getJsonSchema() {
		return jsonSchema;
	}

	public String getNodeToSearch() {
		return nodeToSearch;
	}

	public void setNodeToSearch(String nodeToSearch) {
		this.nodeToSearch = nodeToSearch;
	}

	public ObjectNode getAddElement() {
		return addElement;
	}

	public void setAddElement(ObjectNode addElement) {
		this.addElement = addElement;
	}

	public JSON_OP getOperation() {
		return operation;
	}

	public void setOperation(JSON_OP operation) {
		this.operation = operation;
	}

	public boolean isNodeFound() {
		return nodeFound;
	}

	public void setNodeFound(boolean nodeFound) {
		this.nodeFound = nodeFound;
	}

	public JsonNode getJson() {
		return json;
	}

	public void setJson(JsonNode json) {
		this.json = json;
	}

	public ObjectNode getNodetofind() {
		return nodetofind;
	}

	public void setNodetofind(ObjectNode nodetofind) {
		this.nodetofind = nodetofind;
	}

	public String getNewname() {
		return newname;
	}

	public void setNewname(String newname) {
		this.newname = newname;
	}

	public int getIncrementSize() {
		return incrementSize;
	}

	public void setIncrementSize(int incrementSize) {
		this.incrementSize = incrementSize;
	}

}
