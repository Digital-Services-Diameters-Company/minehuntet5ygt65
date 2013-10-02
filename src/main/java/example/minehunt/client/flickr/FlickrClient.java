package example.minehunt.client.flickr;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.Size;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;


/**
 *
 */
final class FlickrClient {

    private static final String API_KEY = System.getProperty("minehunt.flickr.key");

    private Flickr flickr;

    FlickrClient() {
        try {
            this.flickr = new Flickr(API_KEY, "", new REST());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Image nextImage() {

        if (flickr == null) {
            return null;
        }

        try {
            System.out.println("Start loading photo");
            final long t = System.currentTimeMillis();
            BufferedImage image;
            int tries = 0;
            do {
                image = findPhoto("mer", ThreadLocalRandom.current().nextInt(1000));
            } while (image == null && tries++ < 10);

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 1024);
            ImageIO.write(image, "jpeg", outputStream);
            final Image i = new Image(new ByteArrayInputStream(outputStream.toByteArray()));
            System.out.println("Photo loaded from flickr in " + (System.currentTimeMillis() - t) + "ms");
            return i;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private BufferedImage findPhoto(final String text, final int random) {
        try {
            final PhotosInterface photosInterface = flickr.getPhotosInterface();
            final SearchParameters searchParameters = new SearchParameters();
            searchParameters.setTags(new String[]{text});
            //searchParameters.setText(text);

            final PhotoList<Photo> results = photosInterface.search(searchParameters, 1, random);
            if (results.isEmpty()) {
                System.out.println("photo not found");
                return null;
            }
            final Photo photo = results.get(0);
            return photosInterface.getImage(photo, Size.MEDIUM_800);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}