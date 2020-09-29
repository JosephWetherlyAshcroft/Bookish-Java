package org.softwire.training.bookish.models.database;

import java.math.BigInteger;

public class Book {
    int ID;
    long ISBN;
    int noOfCopies;
    String title;
    int availableCopies;

    public Book(int id, long isbn, int noofcopies, String name, int availablecopies){
        ID = id;
        ISBN = isbn;
        noOfCopies = noofcopies;
        title = name;
        availableCopies = availablecopies;
    }

    public long getISBN() {
        return ISBN;
    }
    public int getAvailableCopies() {
        return availableCopies;
    }
    public int getID() {
        return ID;
    }
    public int getNoOfCopies() {
        return noOfCopies;
    }
    public String getTitle() {
        return title;
    }
}
