
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import it.bogo.models.Node;
import it.bogo.models.User;

public class NodeOperationTest {
	ObjectMapper mapper = new ObjectMapper();
	private static Map<String, String> opResult = new HashMap<>();

	@Test
	public void addToNode() throws JsonParseException, JsonMappingException, IOException {
		String target = "root";
		String newnodename = "999999";
		String[] nodes = target.split("-");

		File file = new File("datarif.json");
		User user = mapper.readValue(file, User.class);
		String jsondata = mapper.writeValueAsString(user);
		// List<Node> nodeList = user.getUserfilesystem().getNodes();

		List<String> parts = new ArrayList<String>();
		List<Node> childs = new ArrayList<Node>();
		Node node = new Node(newnodename, "file", "image/png", 3000, childs, parts);
		JsonNode jsonNewNode = mapper.readTree(mapper.writeValueAsString(node));
		opResult = new NodeOperation().add(target, jsondata, (ObjectNode) jsonNewNode);

		ArrayList<String> nodetoupdatesize = new ArrayList(Arrays.asList(target.split("-")));

		if (nodetoupdatesize.size() >= 2) {

			for (int i = 0; i < nodetoupdatesize.size() - 1; i++) {
				nodetoupdatesize.remove(i);
			}

			for (String nodename : nodetoupdatesize) {
				opResult = new NodeOperation().updatesize(nodename, opResult.get("jsonstructure"), node.getSize());

			}
		}

		JsonNode ob = mapper.readTree(opResult.get("jsonstructure"));
		assertTrue(opResult.get("jsonstructure").contains(newnodename));
		
		
	}

	@Test
	public void nodeExist() throws JsonParseException, JsonMappingException, IOException {
		File file = new File("datarif.json");
		User user = mapper.readValue(file, User.class);
		String jsondata = mapper.writeValueAsString(user);
		
		String nodetosearch = "facedd";
		JsonNode jsn = new NodeOperation().nodeExist(nodetosearch,jsondata);
		assertTrue(jsn.get("id").asText().contains(nodetosearch));
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsn));
	}
	
	@Test
	public void nodeMove() throws JsonParseException, JsonMappingException, IOException {
		
		File file = new File("datarif.json");
		User user = mapper.readValue(file, User.class);
		String jsondata = mapper.writeValueAsString(user);
		String nodesource = "1793cd";
		String nodedest = "facedd";
		
		opResult = new NodeOperation().move(nodesource, jsondata, nodedest);
		
		JsonNode jsn = new NodeOperation().nodeExist(nodedest,opResult.get("jsonstructure"));
				
		assertTrue(mapper.writeValueAsString((ObjectNode) jsn).contains(nodesource));
		System.out.println("now "+nodesource+" is a child of "+nodedest+"! see below");
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsn));

	}
	
}
