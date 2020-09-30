package org.softwire.training.bookish.models.database;

public class Member {
    int ID;
    String Name;

    public Member (int id, String name){
        ID = id;
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }
}
