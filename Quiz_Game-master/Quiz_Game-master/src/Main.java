import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class Main extends Application {
    private Stage primaryStage;
    private QuizDAO quizDAO = new QuizDAO();
    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private String playerId;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showStartScreen();
    }

    private void showStartScreen() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        Label label = new Label("আপনার আইডি লিখুন:");
        TextField idField = new TextField();
        Button startBtn = new Button("কুইজ শুরু করুন");

        startBtn.setOnAction(e -> {
            playerId = idField.getText().trim();
            if (!playerId.isEmpty()) {
                if (quizDAO.isIdAllowed(playerId)) {
                    startQuiz();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "আইডি পাওয়া যায়নি!", ButtonType.OK);
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "দয়া করে আইডি লিখুন!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(label, idField, startBtn);
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.setTitle("বাংলা কুইজ গেম");
        primaryStage.show();
    }

    private void startQuiz() {
        questions = quizDAO.getRandomQuestions(2); // only 2 questions
        currentIndex = 0;
        score = 0;
        showQuestion();
    }

    private void showQuestion() {
        if (currentIndex >= questions.size()) {
            quizDAO.savePlayer(playerId, score);
            showResult();
            return;
        }

        Question q = questions.get(currentIndex);
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        Label questionLabel = new Label((currentIndex + 1) + ". " + q.getQuestion());

        ToggleGroup optionsGroup = new ToggleGroup();
        RadioButton a = new RadioButton("A. " + q.getOptionA());
        a.setToggleGroup(optionsGroup);
        RadioButton b = new RadioButton("B. " + q.getOptionB());
        b.setToggleGroup(optionsGroup);
        RadioButton c = new RadioButton("C. " + q.getOptionC());
        c.setToggleGroup(optionsGroup);
        RadioButton d = new RadioButton("D. " + q.getOptionD());
        d.setToggleGroup(optionsGroup);

        Button nextBtn = new Button("পরবর্তী");
        nextBtn.setOnAction(e -> {
            if (optionsGroup.getSelectedToggle() != null) {
                String selected = ((RadioButton) optionsGroup.getSelectedToggle()).getText().substring(0, 1);
                if (selected.equalsIgnoreCase(q.getCorrectOption())) {
                    score += 20;
                }
                currentIndex++;
                showQuestion();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "দয়া করে একটি অপশন নির্বাচন করুন!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(questionLabel, a, b, c, d, nextBtn);
        primaryStage.setScene(new Scene(root, 450, 350));
    }

    private void showResult() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        Label resultLabel = new Label("ধন্যবাদ " + playerId + "! আপনার স্কোর: " + score + "/40");

        Button restartBtn = new Button("আবার খেলুন");
        Button exitBtn = new Button("বন্ধ করুন");

        restartBtn.setOnAction(e -> showStartScreen());
        exitBtn.setOnAction(e -> primaryStage.close());

        root.getChildren().addAll(resultLabel, restartBtn, exitBtn);
        primaryStage.setScene(new Scene(root, 400, 300));
    }
}
