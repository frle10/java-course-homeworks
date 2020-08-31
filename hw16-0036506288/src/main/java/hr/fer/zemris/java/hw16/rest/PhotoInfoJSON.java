package hr.fer.zemris.java.hw16.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

/**
 * This class uses RESTful API in order to fetch information
 * about a photo given by its name.
 * <p>
 * The information fetched is the photo's description and
 * tags associated with it.
 * 
 * @author Ivan Skorupan
 */
@Path("/photoInfo")
public class PhotoInfoJSON {
	
	/**
	 * Servlet context that we can use to get relevant information from.
	 */
	@Context
	private ServletContext context;
	
	/**
	 * This method takes a query parameter which represents a photo name
	 * and then based on that parameter fetches information (description
	 * and tags) about the given photo.
	 * <p>
	 * Afterwards, the collected information is put in a JSON object and
	 * returned inside a {@link Response} object.
	 * 
	 * @param photoName - name of the photo whose information to fetch
	 * @return a {@link Response} object that contains a JSON object
	 * containing our result (photo information)
	 */
	@GET
	@Produces("application/json")
	public Response getPhotoInfo(@QueryParam("photoName") String photoName) {
		String descriptorPath = context.getRealPath("/WEB-INF/opisnik.txt");

		List<String> descriptorLines = null;
		try {
			descriptorLines = Files.readAllLines(Paths.get(descriptorPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String description = null;
		String tags = null;
		for(int i = 0; i < descriptorLines.size(); i += 4) {
			if(photoName.equals(descriptorLines.get(i))) {
				description = descriptorLines.get(i + 1);
				tags = descriptorLines.get(i + 2);
				break;
			}
		}
		
		JSONObject result = new JSONObject();
		result.put("desc", description);
		result.put("tags", tags);
		
		return Response.status(Status.OK).entity(result.toString()).build();
	}
	
}
