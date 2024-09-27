package uk.co.caprica.vlcj.javafx.demo;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.util.List;

/**
 *
 */
public class VlcjJavaFxApplication extends Application {

    private final MediaPlayerFactory mediaPlayerFactory;

    private final EmbeddedMediaPlayer embeddedMediaPlayer;

    private ImageView videoImageView;

    public VlcjJavaFxApplication() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        this.embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
            }
        });
    }

    @Override
    public void init() {
        this.videoImageView = new ImageView();
        this.videoImageView.setPreserveRatio(true);

        embeddedMediaPlayer.videoSurface().set(new ImageViewVideoSurface(this.videoImageView));
    }

    @Override
    public final void start(Stage primaryStage) throws Exception {
        List<String> params = getParameters().getRaw();
       /*  if (params.size() != 1) {
            System.out.println("Specify a single MRL");
            System.exit(-1);
        }
 */
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        videoImageView.fitWidthProperty().bind(root.widthProperty());
        videoImageView.fitHeightProperty().bind(root.heightProperty());

        root.widthProperty().addListener((observableValue, oldValue, newValue) -> {
            // If you need to know about resizes
        });

        root.heightProperty().addListener((observableValue, oldValue, newValue) -> {
            // If you need to know about resizes
        });

        root.setCenter(videoImageView);

        // Create the menu bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem openMenuItem = new MenuItem("Open Media File");
        openMenuItem.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mkv"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                embeddedMediaPlayer.media().play(selectedFile.getAbsolutePath());
            }
        });
        fileMenu.getItems().add(openMenuItem);
        menuBar.getMenus().add(fileMenu);

        // Set the menu bar at the top of the BorderPane
        root.setTop(menuBar);

        // Load icons from resources
        Image playIcon = new Image(getClass().getResourceAsStream("/resources/play.png"));
        Image pauseIcon = new Image(getClass().getResourceAsStream("/resources/pause.png"));
        Image stopIcon = new Image(getClass().getResourceAsStream("/resources/stop.png"));

        // Create playback controls
        Button playButton = new Button();
        playButton.setGraphic(new ImageView(playIcon));
        playButton.setOnAction(event -> embeddedMediaPlayer.controls().play());

        Button pauseButton = new Button();
        pauseButton.setGraphic(new ImageView(pauseIcon));
        pauseButton.setOnAction(event -> embeddedMediaPlayer.controls().pause());

        Button stopButton = new Button();
        stopButton.setGraphic(new ImageView(stopIcon));
        stopButton.setOnAction(event -> embeddedMediaPlayer.controls().stop());

        HBox controlBox = new HBox(10, playButton, pauseButton, stopButton);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setPadding(new Insets(10));

        // Set the control box at the bottom of the BorderPane
        root.setBottom(controlBox);

        Scene scene = new Scene(root, 1200, 675, Color.BLACK);
        primaryStage.setTitle("vlcj JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        //embeddedMediaPlayer.media().play(params.get(0));
        embeddedMediaPlayer.media().play( "/Users/matt/dev/vid/my-app/Peggy-O 70s JG Lead Lesson.mp4");

        //embeddedMediaPlayer.controls().setPosition(0.4f);
    }

    @Override
    public final void stop() {
        embeddedMediaPlayer.controls().stop();
        embeddedMediaPlayer.release();
        mediaPlayerFactory.release();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
