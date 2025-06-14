import java.time.LocalDate;

public class Birthday {
    private int id;
    private String name;
    private LocalDate birthdate;

    public Birthday(int id, String name, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
    }

    public Birthday(String name, LocalDate birthdate) {
        this(-1, name, birthdate);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getBirthdate() { return birthdate; }
}
