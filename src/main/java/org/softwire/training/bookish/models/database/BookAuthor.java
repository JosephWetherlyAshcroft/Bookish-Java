package org.softwire.training.bookish.models.database;
import java.math.BigInteger;
import java.util.List;

public class BookAuthor {
    int BookID;
    long ISBN;
    int noOfCopies;
    String title;
    int availableCopies;
    List<String> Name;

    public BookAuthor(int id, long isbn, int noofcopies, String booktitle, int availablecopies, List<String> name){
        BookID = id;
        ISBN = isbn;
        noOfCopies = noofcopies;
        title = booktitle;
        availableCopies = availablecopies;
        Name = name;
    }

    public long getISBN() {
        return ISBN;
    }
    public int getAvailableCopies() {
        return availableCopies;
    }
    public int getNoOfCopies() {
        return noOfCopies;
    }
    public String getTitle() {
        return title;
    }
    public int getBookID() {
        return BookID;
    }
    public List<String> getName() {
        return Name;
    }
}