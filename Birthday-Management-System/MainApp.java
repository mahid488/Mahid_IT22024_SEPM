import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        BirthdayController controller = new BirthdayController();
        Scene scene = new Scene(controller.getView(), 600, 500);
        primaryStage.setTitle("জন্মদিন ম্যানেজমেন্ট সিস্টেম");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
