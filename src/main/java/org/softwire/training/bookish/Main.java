package org.softwire.training.bookish;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.softwire.training.bookish.services.LibraryService;

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
            System.out.println("\nWelcome to the library! What would you like to do? \n1) View all books \n2) Add a new book \n3) Search for a book by title\n4) Edit book by ID\n5) Delete book by ID\n6) Add new member\n7) View all members\n8) Delete member by ID\n9) Edit member by ID");
            String choice = sc.next();
            switch (choice){
                case "1":
                    LibraryService.outPutAllBooks(handle);
                    break;
                case "2":
                    LibraryService.addNewBook(handle);
                    break;
                case "3":
                    LibraryService.searchByTitle(handle);
                    break;
                case "4":
                    LibraryService.editBookDetailsByID(handle);
                    break;
                case "5":
                    LibraryService.deleteCopyOfBookByID(handle);
                    break;
                case "6":
                    LibraryService.addMember(handle);
                    break;
                case "7":
                    LibraryService.outPutAllMembers(handle);
                    break;
                case "8":
                    LibraryService.deleteMemberById(handle);
                    break;
                case "9":
                    LibraryService.editMemberDetailsByID(handle);
                default:
                    System.out.println("Invalid input");
            }
        }
    }
}
