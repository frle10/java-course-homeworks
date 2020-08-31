package hr.fer.zemris.java.hw16.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class uses RESTful API in order to enable fetching
 * a list of image names for a specific tag given as a query
 * parameter.
 * 
 * @author Ivan Skorupan
 */
@Path("/imageList")
public class ImagesJSON {

	/**
	 * Servlet context that we can use to get relevant information from.
	 */
	@Context
	private ServletContext context;

	/**
	 * This method walks through an images descriptor file in order
	 * to find all image names that correspond to a given tag given
	 * as a query parameter.
	 * <p>
	 * All found image names are put into a list and afterwards the
	 * list is converted into an array and put into a JSON object
	 * which is being returned upon finish.
	 * 
	 * @param tag - image tag to search images by
	 * @return a {@link Response} object that contains a JSON object
	 * containing our result (array of image names)
	 */
	@GET
	@Produces("application/json")
	public Response getImagesList(@QueryParam("tag") String tag) {
		String descriptorPath = context.getRealPath("/WEB-INF/opisnik.txt");

		List<String> descriptorLines = null;
		try {
			descriptorLines = Files.readAllLines(Paths.get(descriptorPath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> images = new ArrayList<>();
		for(int i = 0; i < descriptorLines.size(); i += 4) {
			if(descriptorLines.get(i + 2).contains(tag)) {
				images.add(descriptorLines.get(i));
			}
		}
		
		JSONObject result = new JSONObject();

		JSONArray imagesArray = new JSONArray();
		for(String image : images) {
			imagesArray.put(image);
		}

		result.put("images", imagesArray);
		return Response.status(Status.OK).entity(result.toString()).build();
	}

}
