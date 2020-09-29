package org.softwire.training.bookish;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.softwire.training.bookish.models.database.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class Main {
    private static final String hostname = "127.0.0.1";
    private static final String database = "mydb";
    private static final String user = "root";
    private static final String password = "BookishPassword";
    private static final String connectionString = "jdbc:mysql://" + hostname + "/" + database + "?user=" + user + "&password=" + password + "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT&useSSL=false&allowPublicKeyRetrieval=true";

    public static void main(String[] args){
        Jdbi jdbi = Jdbi.create(connectionString);
        Handle handle = jdbi.open();

        libraryProcesses.outPutAllBooks(handle);
        libraryProcesses.addNewBook(handle);
    }
}
