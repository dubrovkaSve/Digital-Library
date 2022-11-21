package ru.dubrovka.project.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Название книги не может быть пустым")
    @Size(min = 1, max = 50, message = "Название книги может содержать от 1 до 50 символов")
    private String name;

    @Column(name = "author")
    @NotEmpty(message = "Поле не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя автора может содержать от 2 до 30 символов")
    private String author;

    @Column(name = "year")
    @NotNull(message = "Поле не может быть пустым")
    @Min(value = 0, message = "Дата издания книги должно быть больше 0")
    private int year;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "time_of_getting_book")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeOfGettingBook;

    @Transient
    private boolean exceedingPeriod;

    public Book() {}

    public Book(String name, String author, int year, Date timeOfGettingBook) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.timeOfGettingBook = timeOfGettingBook;
    }

    public void release() {
        this.setOwner(null);
        this.setTimeOfGettingBook(null);
    }

    public void assign(Person selectedPerson) {
        this.setOwner(selectedPerson);
        this.setTimeOfGettingBook(new Date());
    }

    public boolean IsPeriodExceeded() {
        LocalDateTime localDateTimeOfStartReading = (this.getTimeOfGettingBook()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime allowedPeriodOfReading = localDateTimeOfStartReading.plusDays(10);
        LocalDateTime currentDateAsLocalDateTime = (new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (currentDateAsLocalDateTime.isAfter(allowedPeriodOfReading)){
            exceedingPeriod = true;
        } else {
            exceedingPeriod = false;
        }

        return exceedingPeriod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getTimeOfGettingBook() {

        return timeOfGettingBook;
    }

    public void setTimeOfGettingBook(Date timeOfGettingBook) {

        this.timeOfGettingBook = timeOfGettingBook;
    }

    public boolean isExceedingPeriod() {
        return exceedingPeriod;
    }

    public void setExceedingPeriod(boolean exceedingPeriod) {
        this.exceedingPeriod = exceedingPeriod;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", owner=" + owner +
                ", timeOfGettingBook=" + timeOfGettingBook +
                ", exceedingPeriod=" + exceedingPeriod +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && year == book.year && name.equals(book.name) && Objects.equals(author, book.author) && Objects.equals(owner, book.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, year, owner);
    }
}
