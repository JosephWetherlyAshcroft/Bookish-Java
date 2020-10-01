package org.softwire.training.bookish.models.page;

import org.softwire.training.bookish.models.database.Author;
import org.softwire.training.bookish.models.database.Book;
import org.softwire.training.bookish.models.database.BookAuthor;
import org.softwire.training.bookish.models.database.Technology;

import java.util.List;

public class BookViewModel {
    private List<BookAuthor> books;

    public List<BookAuthor> getBooks() {
        return books;
    }
    public void setBooks(List<BookAuthor> books) {
        this.books = books;
    }

}


