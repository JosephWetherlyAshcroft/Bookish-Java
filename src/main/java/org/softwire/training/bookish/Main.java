package org.softwire.training.bookish;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.softwire.training.bookish.models.database.Book;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String hostname = "127.0.0.1";
    private static final String database = "mydb";
    private static final String user = "root";
    private static final String password = "BookishPassword";
    private static final String connectionString = "jdbc:mysql://" + hostname + "/" + database + "?user=" + user + "&password=" + password + "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT&useSSL=false&allowPublicKeyRetrieval=true";

    public static void main(String[] args){
        Jdbi jdbi = Jdbi.create(connectionString);
        Handle handle = jdbi.open();
        Scanner sc = new Scanner(System.in);
        while(1==1){
            System.out.println("Welcome to the library! What would you like to do? \n1) View all books \n2) Add a new book \n3) Search for a book by title\n4) Edit book by ID\n5) Delete book by ID");
            String choice = sc.next();
            switch (choice){
                case "1":
                    libraryProcesses.outPutAllBooks(handle);
                    break;
                case "2":
                    libraryProcesses.addNewBook(handle);
                    break;
                case "3":
                    libraryProcesses.searchByTitle(handle);
                    break;
                case "4":
                    libraryProcesses.editBookDetailsByID(handle);
                    break;
                case "5":
                    libraryProcesses.deleteCopyOfBookByID(handle);
                    break;
                default:
                    System.out.println("Invalid input!!!!!!!");
            }
        }
    }
}
