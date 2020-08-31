package hr.fer.zemris.java.hw16.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class uses RESTful API in order to fetch
 * all distinct tag names situated in a photo
 * descriptor file.
 * 
 * @author Ivan Skorupan
 */
@Path("/taglist")
public class TagJSON {

	/**
	 * Servlet context that we can use to get relevant information from.
	 */
	@Context
	private ServletContext context;

	/**
	 * This method finds all distinct tag names in a photo descriptor
	 * file and puts them in a set.
	 * <p>
	 * The set is then converted into an array which is being put in
	 * a JSON object which is then returned inside a {@link Response}
	 * object.
	 * 
	 * @return a {@link Response} object that contains a JSON object
	 * containing our result (list of distinct tags)
	 */
	@GET
	@Produces("application/json")
	public Response getTagList() {
		String realPath = context.getRealPath("/WEB-INF/opisnik.txt");

		List<String> descriptorLines = null;
		try {
			descriptorLines = Files.readAllLines(Paths.get(realPath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Set<String> tags = new HashSet<>();
		for(int i = 2; i < descriptorLines.size(); i += 4) {
			String picTags = descriptorLines.get(i).trim();
			String[] distinctTags = picTags.split(",");
			for(String tag : distinctTags) {
				tags.add(tag.trim());
			}
		}
		
		List<String> tagsList = tags.stream()
				.sorted((t1, t2) -> Collator.getInstance(Locale.forLanguageTag("hr")).compare(t1, t2))
				.collect(Collectors.toList());
		
		JSONObject result = new JSONObject();

		JSONArray tagsArray = new JSONArray();
		for(String tag : tagsList) {
			tagsArray.put(tag);
		}

		result.put("tags", tagsArray);
		return Response.status(Status.OK).entity(result.toString()).build();
	}

}
