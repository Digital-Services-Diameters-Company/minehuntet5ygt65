package example.minehunt.client;

import example.minehunt.MinehuntService;
import example.minehunt.SimpleMinehuntService;
import example.minehunt.client.flickr.FlickProvider;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 */
public final class Minehunt extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Stage stage = new Stage(StageStyle.UTILITY);

        final MinehuntService minehuntService = new SimpleMinehuntService();
        final Image backgroundImage = FlickProvider.getInstance().nextImage();

        Scene scene = new Scene(new GameNode(minehuntService, backgroundImage));
        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.setTitle("Minehunt");
        stage.show();

        //TODO this is a bug on databinding ("bomb") try to remove this later
        Platform.runLater(() -> scene.setRoot(new GameNode(minehuntService, backgroundImage)));
    }
}
