package uk.co.caprica.vlcj.javafx.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.io.File;

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
          Image playIcon = new Image(getClass().getResourceAsStream("/icons/buttons/play.png"));
          Image pauseIcon = new Image(getClass().getResourceAsStream("/icons/buttons/pause.png"));
          Image stopIcon = new Image(getClass().getResourceAsStream("/icons/buttons/stop.png"));
  // Debugging: Check if images are loaded
  System.out.println("Play Icon: " + (playIcon != null));
  System.out.println("Pause Icon: " + (pauseIcon != null));
  System.out.println("Stop Icon: " + (stopIcon != null));

  // Create playback controls with ImageView
  ImageView playImageView = new ImageView(playIcon);
  playImageView.setFitWidth(30);
  playImageView.setFitHeight(30);
  playImageView.setOpacity(0.7); // Set opacity for transparency
  playImageView.setOnMouseClicked(event -> embeddedMediaPlayer.controls().play());

  ImageView pauseImageView = new ImageView(pauseIcon);
  pauseImageView.setFitWidth(30);
  pauseImageView.setFitHeight(30);
  pauseImageView.setOpacity(0.7); // Set opacity for transparency
  pauseImageView.setOnMouseClicked(event -> embeddedMediaPlayer.controls().pause());

  ImageView stopImageView = new ImageView(stopIcon);
  stopImageView.setFitWidth(30);
  stopImageView.setFitHeight(30);
  stopImageView.setOpacity(0.7); // Set opacity for transparency
  stopImageView.setOnMouseClicked(event -> embeddedMediaPlayer.controls().stop());

  HBox controlBox = new HBox(10, playImageView, pauseImageView, stopImageView);
  controlBox.setAlignment(Pos.CENTER);
  controlBox.setPadding(new Insets(10));
  controlBox.setStyle("-fx-background-color: transparent;"); // Set background color to transparent
  controlBox.setVisible(false); // Initially hide the control box

  // Debugging: Check if controlBox is added
  System.out.println("ControlBox added: " + (controlBox != null));

  // Create a VBox to stack the video and controls vertically
    VBox vbox = new VBox();
    vbox.getChildren().addAll(videoImageView, controlBox);
    VBox.setVgrow(videoImageView, Priority.ALWAYS); // Allow video to grow and take available space

    // Set the VBox as the center of the BorderPane
    root.setCenter(vbox);

    // Show controls on mouse hover
    vbox.setOnMouseEntered(event -> controlBox.setVisible(true));
    vbox.setOnMouseExited(event -> controlBox.setVisible(false));

    // Debugging: Check if root has children
    System.out.println("Root children: " + root.getChildren().size());

    // Debugging: Check controlBox bounds
    controlBox.boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
        System.out.println("ControlBox bounds: " + newValue);
    });


  Scene scene = new Scene(root, 1200, 675, Color.BLACK);
  primaryStage.setTitle("vlcj JavaFX");
  primaryStage.setScene(scene);
  primaryStage.show();
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