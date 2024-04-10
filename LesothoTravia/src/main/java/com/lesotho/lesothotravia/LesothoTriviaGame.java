package com.lesotho.lesothotravia;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LesothoTriviaGame extends Application {

    private final List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private final int totalQuestions;

    private final Label questionLabel = new Label();
    private final ImageView mediaView = new ImageView();
    private final ChoiceBox<String> answerChoiceBox = new ChoiceBox<>();
    private final Label scoreLabel = new Label();
    private final Label progressLabel = new Label();
    private final Button submitButton = new Button("Submit");

    private final VBox root = new VBox(10);

    public LesothoTriviaGame() {
        createQuestions();
        totalQuestions = questions.size();
    }

    @Override
    public void start(Stage primaryStage) {
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());

        primaryStage.setTitle("Lesotho Trivia Game");
        primaryStage.setScene(scene);

        setupUI();

        showQuestion();

        primaryStage.show();
    }

    private void createQuestions() {
        questions.add(new Question(
                "What is the capital of Lesotho?",
                "ui.jpg",
                "stun.mp4", "Leribe", "Mohale's Hoek",
                "Maseru"
        ));
        questions.add(new Question(
                "Which mountain range covers much of Lesotho?",
                "drakensberg.jpg",
                "Drakensberg", "Atlas Mountains", "Himalayas",
                "Drakensberg"
        ));
        questions.add(new Question(
                "What is the official language of Lesotho?",
                "sesotho.jpg",
                "English", "Sesotho", "French",
                "Sesotho"
        ));
        questions.add(new Question(
                "What is the currency of Lesotho?",
                "lesotho_loti.jpg",
                "Rand", "Loti", "Euro",
                "Loti"
        ));
        questions.add(new Question(
                "When did Lesotho gain independence?",
                "lesotho_independence.jpg",
                "1966", "1970", "1980",
                "1966"
        ));
    }

    private void setupUI() {
        root.getChildren().addAll(questionLabel, mediaView, answerChoiceBox, submitButton, scoreLabel, progressLabel);

        answerChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> submitButton.setDisable(newValue == null));

        submitButton.setOnAction(event -> {
            String selectedAnswer = answerChoiceBox.getValue();
            checkAnswer(selectedAnswer);
            submitButton.setDisable(true);
        });
    }

    private void showQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            questionLabel.setText(question.getQuestion());

            loadImage(question.getImageFileName());
            loadVideo(question.getVideoFileName());

            answerChoiceBox.getItems().setAll(question.getAnswers());
            answerChoiceBox.setValue(null);

            progressLabel.setText(String.format("Question %d of %d", currentQuestionIndex + 1, totalQuestions));
            scoreLabel.setText(String.format("Score: %d / %d", score, totalQuestions));
        } else {
            questionLabel.setText("Game Over");
            mediaView.setImage(null);
            answerChoiceBox.setVisible(false);
            submitButton.setVisible(false);
            scoreLabel.setText(String.format("Final Score: %d / %d", score, totalQuestions));
        }
    }

    private void loadImage(String fileName) {
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lesothotravia/images/" + fileName)));
            mediaView.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    private void loadVideo(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        try {
            Media media = new Media(Objects.requireNonNull(getClass().getResource("/lesothotravia/video/" + fileName)).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            MediaView mediaView = new MediaView(mediaPlayer);
            root.getChildren().add(mediaView);
        } catch (Exception e) {
            System.err.println("Error loading video: " + e.getMessage());
        }
    }

    private void checkAnswer(String selectedAnswer) {
        Question question = questions.get(currentQuestionIndex);
        String correctAnswer = question.getCorrectAnswer();
        if (selectedAnswer != null && selectedAnswer.equals(correctAnswer)) {
            score++;
        }
        currentQuestionIndex++;
        showQuestion();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class Question {
    private final String question;
    private final List<String> answers;
    private final String correctAnswer;
    private final String imageFileName;
    private final String videoFileName;

    public Question(String question, String imageFileName, String videoFileName, String... answers) {
        this.question = question;
        this.imageFileName = imageFileName;
        this.videoFileName = videoFileName;
        this.answers = List.of(answers);
        this.correctAnswer = answers[0]; // Assuming the first answer is always the correct one
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String getVideoFileName() {
        return videoFileName;
    }
}
