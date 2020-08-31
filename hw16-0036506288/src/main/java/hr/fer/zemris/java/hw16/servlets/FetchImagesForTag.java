package hr.fer.zemris.java.hw16.servlets;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet provides a thumbnail of given name to the client.
 * <p>
 * If the thumbnail does not yet exist in a corresponding directory,
 * a big version of the picture is then fetched and scaled to a
 * thumbnail and written to the thumbnail directory, after which the
 * newly created thumbnail is returned.
 * <p>
 * In other words, the thumbnail directory is used as a cache and
 * new thumbnails are created "lazily", on demand.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="fetchImages", urlPatterns={"/servlets/fetchImage"})
public class FetchImagesForTag extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = req.getServletContext();
		String imageName = req.getParameter("name");
		
		String thumbnailsPath = context.getRealPath("WEB-INF/thumbnails");
		Path thumbnails = Paths.get(thumbnailsPath);
		
		if(!Files.exists(thumbnails)) {
			Files.createDirectory(thumbnails);
		}
		
		resp.setContentType("image/jpg");
		String thumbnailPath = context.getRealPath("WEB-INF/thumbnails/" + imageName);
		Path thumbnail = Paths.get(thumbnailPath);
		
		BufferedImage thumbnailBim = null;
		if(!Files.exists(thumbnail)) {
			thumbnailBim = createThumbnail(context, thumbnailPath, imageName);
		} else {
			InputStream thumbnailInputStream = Files.newInputStream(Paths.get(thumbnailPath));
			thumbnailBim = ImageIO.read(thumbnailInputStream);
			thumbnailInputStream.close();
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(thumbnailBim, "jpg", baos);
		
		resp.getOutputStream().write(baos.toByteArray());
		baos.close();
	}
	
	/**
	 * This method creates a thumbnail of given <code>imageName</code>,
	 * writes it to <code>thumbnailPath</code> and returns the instance
	 * of {@link BufferedImage} which represents the newly created
	 * thumbnail.
	 * 
	 * @param context - servlet context used to get real path of image
	 * @param thumbnailPath - path to which to write new thumbnail
	 * @param imageName - name of thumbnail/image
	 * @throws IOException if there is an error while reading or writing images
	 */
	private BufferedImage createThumbnail(ServletContext context, String thumbnailPath, String imageName) throws IOException {
		String imagePath = context.getRealPath("/WEB-INF/slike/" + imageName);
		Path image = Paths.get(imagePath);
		
		InputStream imageStream = Files.newInputStream(image);
		BufferedImage bim = ImageIO.read(imageStream);
		Image scaledInstance = bim.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
		
		BufferedImage thumbnail = new BufferedImage(150, 150, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = thumbnail.createGraphics();
		g2d.drawImage(scaledInstance, 0, 0, null);
		
		OutputStream thumbnailStream = Files.newOutputStream(Paths.get(thumbnailPath));
		ImageIO.write(thumbnail, "jpg", thumbnailStream);
		
		imageStream.close();
		thumbnailStream.close();
		g2d.dispose();
		
		return thumbnail;
	}
	
}
