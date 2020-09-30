package org.softwire.training.bookish.models.database;

public class Author {
    int ID;
    String Name;

    public Author(int id, String name){
        ID = id;
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public int getID() {
        return ID;
    }
}
