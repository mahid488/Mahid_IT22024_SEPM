import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;

public class BirthdayController {
    private VBox view;
    private ListView<String> listView;
    private TextField nameField, searchField;
    private DatePicker datePicker;
    private ObservableList<String> listItems;
    private BirthdayDAO dao = new BirthdayDAO();

    public BirthdayController() {
        view = new VBox(10);
        view.setPadding(new Insets(10));

        Label title = new Label("জন্মদিন তালিকা");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        listItems = FXCollections.observableArrayList();
        listView = new ListView<>(listItems);
        refreshList();

        HBox form = new HBox(10);
        nameField = new TextField();
        nameField.setPromptText("নাম");
        datePicker = new DatePicker();
        Button addBtn = new Button("যোগ করুন");
        addBtn.setOnAction(e -> {
            dao.addBirthday(new Birthday(nameField.getText(), datePicker.getValue()));
            refreshList();
            nameField.clear();
            datePicker.setValue(null);
        });
        form.getChildren().addAll(nameField, datePicker, addBtn);

        Button deleteBtn = new Button("মুছে ফেলুন");
        deleteBtn.setOnAction(e -> {
            int selected = listView.getSelectionModel().getSelectedIndex();
            if (selected >= 0) {
                Birthday b = dao.getAllBirthdays().get(selected);
                dao.deleteBirthday(b.getId());
                refreshList();
            }
        });

        searchField = new TextField();
        searchField.setPromptText("নাম বা মাস দিয়ে খুঁজুন");
        Button searchBtn = new Button("খুঁজুন");
        searchBtn.setOnAction(e -> {
            listItems.clear();
            for (Birthday b : dao.searchByNameOrMonth(searchField.getText())) {
                listItems.add(b.getName() + " - " + b.getBirthdate());
            }
        });

        Label todayLabel = new Label("আজকের জন্মদিন:");
        ListView<String> todayList = new ListView<>();
        ObservableList<String> todayItems = FXCollections.observableArrayList();
        for (Birthday b : dao.getTodaysBirthdays()) {
            todayItems.add(b.getName() + " - শুভ জন্মদিন!");
        }
        todayList.setItems(todayItems);

        view.getChildren().addAll(title, form, listView, deleteBtn, searchField, searchBtn, todayLabel, todayList);
    }

    public VBox getView() {
        return view;
    }

    private void refreshList() {
        listItems.clear();
        for (Birthday b : dao.getAllBirthdays()) {
            listItems.add(b.getName() + " - " + b.getBirthdate());
        }
    }
}
