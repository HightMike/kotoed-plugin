package gui.Items;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Course extends HBox implements Initializable {
    @FXML
    private ImageView courseIcon;
    @FXML
    private Label courseName;

    private String _name;
    private String _icon;
    private String _oldStyle;

    public Course(String icon, String name) {
        _name = name;
        _icon = icon;
        FXMLLoader l = new FXMLLoader(getClass().getResource("/Views/Course.fxml"));
        l.setController(this);
        l.setRoot(this);
        try {
            l.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        courseName.setText(_name);
        courseIcon.setImage(new Image(getClass().getResourceAsStream(_icon)));
        this.setOnMouseEntered((event -> {
            _oldStyle = this.getStyle();
            this.setStyle("-fx-background-color: lightblue;");
        }));
        this.setOnMouseExited((event -> this.setStyle(_oldStyle)));
    }
}
