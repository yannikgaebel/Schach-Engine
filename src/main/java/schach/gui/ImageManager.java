package schach.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * The image manager is used to access images using caching
 */
public class ImageManager {
	private static ImageManager instance;

	private Map<String, Image> imgs = new HashMap<String, Image>();

	// Private constructor to prevent outside initialization
	private ImageManager() {
	}

	/**
	 * Returns the instance of the image manager
	 * 
	 * @return the instance of the image manager
	 */
	public static ImageManager getInstance() {
		if (instance == null) { instance = new ImageManager(); }
		return instance;
	}

	private void loadImage(String name, String... extensions) {
		URL url = null;
		for (String ext : extensions) {
			url = ImageManager.class.getResource("/images/" + name + "." + ext);
			if (url != null) { break; }
		}

		if (url == null) { System.out.println("Could not load resource: " + name); }

		Image image = new Image(url.toExternalForm());
		imgs.put(name, image);
	}

	/**
	 * Get a image by filename without extension
	 * 
	 * @param key the filename without extension
	 * @return the image
	 */
	public Image getImage(final String key) {
		if (imgs.get(key) == null) { loadImage(key, "jpg", "png", "bmp", "gif"); }
		return imgs.get(key);
	}
}