package ru.dubrovka.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dubrovka.project.models.Book;
import ru.dubrovka.project.models.Person;
import ru.dubrovka.project.repositories.BooksRepository;
import ru.dubrovka.project.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);

        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void updateBookedPerson(int id, int personId) {
        Book foundBook = findOne(id);
        Optional<Person> foundedPerson = peopleRepository.findById(personId);
        Person person = foundedPerson.get();

        foundBook.setOwner(person);

        booksRepository.save(foundBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Optional<Person> getBookOwner(int id) {
        return peopleRepository.findByBooksId(id);
    }

    public List<Book> findByOwner(Person person) {
        return booksRepository.findByOwner(person);
    }

    @Transactional
    public void release(int id) {
        Book book = findOne(id);
        book.release();

        booksRepository.save(book);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        Book book = findOne(id);
        book.assign(selectedPerson);

        booksRepository.save(book);
    }

}
