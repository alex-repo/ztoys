package com.ap.gdeocr;

import com.ap.common.core.ComparableId;
import com.ap.common.core.ContainerOnList;
import com.ap.common.image.AbstractImageWrapper;
import com.ap.common.image.CoresImageJob;
import com.ap.common.image.FlatImageJob;
import com.ap.common.image.ImageJob;
import com.ap.common.model.VectorInjectiveSpace;
import com.ap.common.util.ImageUtils;
import com.ap.ztoys.samples.convolution.ModelMetric;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class Controller {

    @FXML
    private VBox root_vbox;
    @FXML
    private FlowPane flow_stack;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private ScrollPane scroll_cnrl;
    @FXML
    private ImageView image_view;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private ToggleButton contrast_togglebutton;
    @FXML
    private ToggleButton size_togglebutton;
    @FXML
    ProgressBar progress_bar;

    @FXML
    private Button openMenuItem;
    private final int SIDE = 32;

    private Group zoomGroup;
    private double lastX;
    private double lastY;
    private ImageView currentView = null;
    private double scale;
    private double scaleStep;
    private TextField currentTextField;

    @FXML
    void initialize() {
        System.out.println("ocr.Controller.initialize");

        assert flow_stack != null : "fx:id=\"map_listview\" was not injected: check your FXML file 'ocr.fxml'.";
        assert root_vbox != null : "fx:id=\"root_vbox\" was not injected: check your FXML file 'ocr.fxml'.";
        assert map_scrollpane != null : "fx:id=\"map_scrollpane\" was not injected: check your FXML file 'ocr.fxml'.";
        assert scroll_cnrl != null : "fx:id=\"map_scrollpane\" was not injected: check your FXML file 'ocr.fxml'.";
        assert map_pin != null : "fx:id=\"map_pin\" was not injected: check your FXML file 'ocr.fxml'.";
        assert pin_info != null : "fx:id=\"pin_info\" was not injected: check your FXML file 'ocr.fxml'.";
        assert image_view != null : "fx:id=\"image_view\" was not injected: check your FXML file 'ocr.fxml'.";
        assert openMenuItem != null : "fx:id=\"openMenuItem\" was not injected: check your FXML file 'menubar.fxml'.";

        map_pin.setVisible(false);
        map_pin.setStyle("-fx-shape: \"M0 0 L" + SIDE + " 0 L" + SIDE + " " + SIDE + " L0 " + SIDE + " Z\"; -fx-pref-width: " + SIDE + "; -fx-pref-height: "
                + SIDE + ";\n");

        scale = 1.0;
        scaleStep = 0.1;

        // Wrap scroll content in a Group so ScrollPane re-computes scroll bars
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);

        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);

        // Add large UI styling and make full screen if we are on device
        if (Platform.isSupported(ConditionalFeature.INPUT_TOUCH)) {
            System.out.println("ocr.Controller.initialize, device detected");
            size_togglebutton.setSelected(true);
            root_vbox.getStyleClass().add("touch-sizes");
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            root_vbox.setPrefSize(bounds.getWidth(), bounds.getHeight());
        }

    }

    @FXML
    void zoomIn(ActionEvent event) {
        zoom(scale += scaleStep);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        zoom(scale -= scaleStep);
    }

    private void zoom(double scaleValue) {
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    void onMouseMove(MouseEvent event) {
        if (!(currentView != null)) {
            currentView = new ImageView();
            currentTextField = addtostack(currentView);
        }
        lastX = event.getX();
        lastY = event.getY();
        pinshow(SIDE);
    }

    @FXML
    void onPinMouseMove(MouseEvent event) {
        lastX += event.getX();
        lastY += event.getY();
        map_pin.setLayoutX(lastX - map_pin.getLayoutBounds().getMinX());
        map_pin.setLayoutY(lastY - map_pin.getLayoutBounds().getMinY());
        pinshow(SIDE);
    }

    @FXML
    void onMouseClickedButton(MouseEvent event) {
        currentView = null;
        currentView = new ImageView();
        currentTextField = addtostack(currentView);
    }

    private TextField addtostack(ImageView simageView) {
        TextField textField = new TextField("unknown");
        textField.setStyle("-fx-background-color: rgba(50, 50, 50, 0.4); -fx-text-fill:white; ");
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-alignment:baseline-left;");
        stackPane.setPadding(new Insets(2, 0, 0, 0));
        stackPane.getChildren().add(simageView);
        stackPane.getChildren().add(textField);

        ContextMenu cm = new ContextMenu();
        MenuItem cmItem1 = new MenuItem("Save Image");
        StackContextMenuHandler contextMenuHandler = new StackContextMenuHandler(textField, simageView);
        cmItem1.setOnAction(contextMenuHandler);
        cm.getItems().add(cmItem1);
        simageView.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                cm.show(simageView, e.getScreenX(), e.getScreenY());
            }
        });
        simageView.setPreserveRatio(true);
        simageView.fitWidthProperty().bind(flow_stack.widthProperty());
        flow_stack.getChildren().add(0, stackPane);
        return textField;
    }

    private void pinshow(int side) {
        map_pin.setLayoutX(lastX - map_pin.getLayoutBounds().getMinX());
        map_pin.setLayoutY(lastY - map_pin.getLayoutBounds().getMinY());
        map_pin.setVisible(true);
        WritablePixelFormat<ByteBuffer> wpixelFormat = (WritablePixelFormat<ByteBuffer>) mainImage.getPixelFormat();
        byte[] buffer = mainImage.getBuffer(lastX, lastY, side, side);
        AbstractImageWrapper currentImage = new AbstractImageWrapper(wpixelFormat, buffer, side, side);
        double[] dbuffer;
        dbuffer = currentImage.getHistogramEqualization(GRAYLUT, true);
        buffer = ImageUtils.dataVisualayxer(dbuffer, false);
        currentImage = new AbstractImageWrapper(wpixelFormat, buffer, side, side);
        currentView.setImage(currentImage.getWritableImage());
        VectorInjectiveSpace signs = new VectorInjectiveSpace(ContainerOnList.class, currentImage);

        PriorityQueue<ComparableId> actualOutput = ModelMetric.calculateResult(
                signs.get(0)
                , StartOcr.convolutionalNet
        );
        Integer s = StartOcr.trainSet.get(actualOutput.peek().getId()).getId();
        if (currentTextField != null) {
            currentTextField.setText(s.toString());
        }
    }

    class StackContextMenuHandler implements EventHandler<ActionEvent> {
        private final TextField textField;
        private final ImageView imageView;

        StackContextMenuHandler(TextField textField, ImageView imageView) {
            this.textField = textField;
            this.imageView = imageView;
        }

        @Override
        public void handle(ActionEvent e) {
            System.out.println(imageView);
            if (imageView != null) {
                File file = new File(textField.getText() + ".png");
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(imageView.getImage(), null), "png", file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @FXML
    void onOpenMenu(ActionEvent event) {
        Stage stage = (Stage) OCR.getScene().getWindow();

        OCR.getScene().setCursor(Cursor.CROSSHAIR);// todo

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"), new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        try {
            imageOrig = new AbstractImageWrapper(selectedFile.toURI().toURL().toString());
            setImage(imageOrig.asFX());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void imageActionCores(ActionEvent event) {

        String className = ((MenuItem) event.getSource()).getId().replaceAll("_", ".");
        IJTask iJTask;
        try {
            iJTask = new IJTask(new CoresImageJob(className));
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        imgAction(iJTask);
    }

    @FXML
    void imageAction(ActionEvent event) {

        String className = ((Button) event.getSource()).getId().replaceAll("_", ".");
        IJTask iJTask;
        try {
            iJTask = new IJTask(new FlatImageJob(className));
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        imgAction(iJTask);
    }

    @FXML
    void resetImage(ActionEvent event) {
        setImage(imageOrig.asFX());
    }

    private AbstractImageWrapper imageOrig;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    private void imgAction(IJTask iJTask) {
        progress_bar.progressProperty().bind(iJTask.progressProperty());
        executorService.submit(iJTask);
    }

    private AbstractImageWrapper mainImage;
    private double[] GRAYLUT;

    private void setImage(Image image) {
        image_view.setImage(image);
        mainImage = new AbstractImageWrapper(image);
        GRAYLUT = mainImage.getHistogramLUT();
        image_view.setImage(mainImage.getWritableImage());
    }

    class IJTask extends Task {

        final ImageJob imageJob;

        public IJTask(ImageJob imageJob) {
            this.imageJob = imageJob;
        }

        final Function<Long[], Void> progressCallBack = (Long[] state) -> {
            if (state.length == 2) {
                updateProgress(state[0], state[1]);
            }
            return null;
        };

        @Override
        public Void call() {
            if (imageOrig != null) {
                long start;
                try {
                    start = System.nanoTime();
                    AbstractImageWrapper wimage = imageJob.run(imageOrig, progressCallBack);
                    System.out.println(imageJob.getClass().getSimpleName()
                            + ":" + imageJob.getName()
                            + " run in "
                            + (System.nanoTime() - start) / 1000000.
                            + "ms");
                    setImage(wimage.asFX());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
