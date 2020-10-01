package org.softwire.training.bookish.services;
import org.jdbi.v3.core.Handle;
import org.softwire.training.bookish.models.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService extends DatabaseService {
    public static List<BookAuthor> outPutAllBooks(Handle handle) {
        List<BookAuthor> bookAuthorList = new ArrayList<>();
        List<Book> books = handle.createQuery("SELECT ID, ISBN, noOfCopies, Title, availableCopies FROM Books ORDER BY Title")
                .map((rs, ctx) -> new Book(rs.getInt("ID"), rs.getLong("ISBN"), rs.getInt("noOfCopies"), rs.getString("Title"), rs.getInt("availableCopies")))
                .list();

        for(int i = 0; i < books.size(); i++){
            Book book = books.get(i);

            String SELECT_ALL = "SELECT Authors.authorName  "
                    + "FROM Authors JOIN bookAuthor on Authors.id = bookAuthor.authorId "
                    + "WHERE bookAuthor.bookId = "+ book.getID();
            List<String> authors = handle.createQuery(SELECT_ALL)
                    .mapTo(String.class)
                    .list();

            BookAuthor bookAuthor = new BookAuthor(books.get(i).getID(), books.get(i).getISBN(), books.get(i).getNoOfCopies(), books.get(i).getTitle(), books.get(i).getAvailableCopies(), authors);
            
            bookAuthorList.add(bookAuthor);
        }
        return bookAuthorList;
    }

  /*  public static List<BookAuthor> searchForBook(Handle handle) {
        List<BookAuthor> bookAuthorList = new ArrayList<>();
        List<Book> books = handle.createQuery("SELECT ID, ISBN, noOfCopies, Title, availableCopies FROM Books ORDER BY Title")
                .map((rs, ctx) -> new Book(rs.getInt("ID"), rs.getLong("ISBN"), rs.getInt("noOfCopies"), rs.getString("Title"), rs.getInt("availableCopies")))
                .list();

        for(int i = 0; i < books.size(); i++){
            Book book = books.get(i);
            if(book.getTitle().equals())

            String SELECT_ALL = "SELECT Authors.authorName  "
                    + "FROM Authors JOIN bookAuthor on Authors.id = bookAuthor.authorId "
                    + "WHERE bookAuthor.bookId = "+ book.getID();
            List<String> authors = handle.createQuery(SELECT_ALL)
                    .mapTo(String.class)
                    .list();

            BookAuthor bookAuthor = new BookAuthor(books.get(i).getID(), books.get(i).getISBN(), books.get(i).getNoOfCopies(), books.get(i).getTitle(), books.get(i).getAvailableCopies(), authors);

            bookAuthorList.add(bookAuthor);
        }
        return bookAuthorList;
    }*/

    public static void addNewBook(Handle handle){

        List<Book> books = handle.createQuery("SELECT ID, ISBN, noOfCopies, Title, availableCopies FROM Books")
                .map((rs, ctx) -> new Book(rs.getInt("ID"), rs.getLong("ISBN"), rs.getInt("noOfCopies"), rs.getString("Title"), rs.getInt("availableCopies")))
                .list();

        List<Author> authors = handle.createQuery("SELECT ID, authorName FROM Authors")
                .map((rs, ctx) -> new Author(rs.getInt("ID"), rs.getString("authorName")))
                .list();


        boolean bookExists = false;
        boolean authorExists = false;

        Scanner sc = new Scanner(System.in);
        System.out.println("Adding a new book (Please enter):");
        System.out.println("Title: ");
        String titleInput = sc.nextLine();
        System.out.println("ISBN: ");
        long isbnInput = Long.parseLong(sc.nextLine());
        System.out.println("How many authors?: ");
        int noOfAuthorsInput = Integer.parseInt(sc.nextLine());
        System.out.println("How many copies?: ");
        int copiesInput = Integer.parseInt(sc.nextLine());

        for(int i = 0; i < books.size(); i++){
            Book book = books.get(i);
            if(book.getTitle().equals(titleInput)){
                Optional<Integer> noOfCopies = handle.createQuery("select noOfCopies from Books where Title like (?)")
                        .bind(0, titleInput)
                        .mapTo(Integer.class)
                        .findFirst();
                Optional<Integer> availableCopies = handle.createQuery("select availableCopies from Books where Title like (?)")
                        .bind(0, titleInput)
                        .mapTo(Integer.class)
                        .findFirst();
                handle.createUpdate("UPDATE Books SET noOfCopies = (?) WHERE Title = (?)")
                        .bind(0,noOfCopies.get()+ copiesInput)
                        .bind(1,titleInput)
                        .execute();
                handle.createUpdate("UPDATE Books SET availableCopies = (?) WHERE Title = (?)")
                        .bind(0,availableCopies.get()+ copiesInput)
                        .bind(1,titleInput)
                        .execute();

                bookExists = true;
                break;
            }
        }
        if(!bookExists){
            handle.createUpdate("insert into Books (Title, ISBN, noOfCopies, availableCopies) values (?, ?, ?, ?)")
                    .bind(0, titleInput)
                    .bind(1, isbnInput)
                    .bind(2, copiesInput)
                    .bind(3, copiesInput)
                    .execute();
        }
        for (int i = 0; i <noOfAuthorsInput; i++){
            System.out.println("Author: ");
            String authorInput = sc.nextLine();
            for(int x = 0; x < authors.size(); x++){
                Author author = authors.get(x);
                if(author.getName().equals(authorInput)){
                    authorExists = true;
                    break;
                }
            }
            if(!authorExists){
                handle.createUpdate("insert into Authors (authorName) values (?)")
                        .bind(0, authorInput)
                        .execute();
            }

            if(!bookExists){
                Optional<Integer> bookid = handle.createQuery("select ID from Books where Title like (?)")
                        .bind(0, titleInput)
                        .mapTo(Integer.class)
                        .findFirst();

                Optional<Integer> authorid = handle.createQuery("select ID from Authors where authorName like (?)")
                        .bind(0, authorInput)
                        .mapTo(Integer.class)
                        .findFirst();

                if(bookid.isPresent() && authorid.isPresent()){
                    handle.createUpdate("insert into bookAuthor (bookId, authorId) values (?,?)")
                            .bind(0, bookid.get())
                            .bind(1,authorid.get())
                            .execute();
                }
            }
        }
    }

    public static void searchByTitle(Handle handle){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter title of book: ");
        String titleOfBook = sc.nextLine();
        System.out.println("\nSearch Results: ");
        Optional<Book> book = handle.createQuery("SELECT ID, ISBN, noOfCopies, Title, availableCopies FROM Books WHERE Title like (?)")
                .bind(0, titleOfBook)
                .map((rs, ctx) -> new Book(rs.getInt("ID"), rs.getLong("ISBN"), rs.getInt("noOfCopies"), rs.getString("Title"), rs.getInt("availableCopies")))
                .findFirst();

        if (book.isPresent()){
            System.out.println("\nID: " + book.get().getID() + "\nTitle: " + book.get().getTitle() + "\nISBN: " + book.get().getISBN() + "\nNumber of copies: " +  book.get().getNoOfCopies() + "\nAvailable copies: " + book.get().getAvailableCopies());

            String SELECT_ALL = "SELECT Authors.authorName "
                    + "FROM Authors JOIN bookAuthor on Authors.id = bookAuthor.authorId "
                    + "WHERE bookAuthor.bookId = "+ book.get().getID();
            List<String> bookAuthor = handle.createQuery(SELECT_ALL)
                    .mapTo(String.class)
                    .list();

            System.out.println("Author(s): ");
            for(int n = 0; n < bookAuthor.size(); n++) {
                System.out.println("- " + bookAuthor.get(n));
            }
        }
        else{
            System.out.println("Book not found");
        }
    }

    public static void editBookDetailsByID(Handle handle){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter ID of book: ");
        int bookId = Integer.parseInt(sc.nextLine());
        Optional<Book> book = handle.createQuery("SELECT ID, ISBN, noOfCopies, Title, availableCopies FROM Books WHERE ID like (?)")
                .bind(0, bookId)
                .map((rs, ctx) -> new Book(rs.getInt("ID"), rs.getLong("ISBN"), rs.getInt("noOfCopies"), rs.getString("Title"), rs.getInt("availableCopies")))
                .findFirst();

        if (book.isPresent()){
            System.out.println("\nID: " + book.get().getID() + "\nTitle: " + book.get().getTitle() + "\nISBN: " + book.get().getISBN() + "\nNumber of copies: " +  book.get().getNoOfCopies() + "\nAvailable copies: " + book.get().getAvailableCopies());

            String SELECT_ALL = "SELECT Authors.authorName "
                    + "FROM Authors JOIN bookAuthor on Authors.id = bookAuthor.authorId "
                    + "WHERE bookAuthor.bookId = "+ book.get().getID();
            List<String> bookAuthor = handle.createQuery(SELECT_ALL)
                    .mapTo(String.class)
                    .list();

            System.out.println("Author(s): ");
            for(int n = 0; n < bookAuthor.size(); n++) {
                System.out.println("- " + bookAuthor.get(n));
            }
            System.out.println("Change:\n1)Title\n2)ISBN\n3)Author");
            String userChoice = sc.nextLine();
            switch (userChoice){
                case "1":
                    System.out.println("What would you ike the new title to be?: ");
                    String newTitle = sc.nextLine();
                    handle.createUpdate("UPDATE Books SET Title = (?) WHERE ID = (?)")
                            .bind(0, newTitle)
                            .bind(1,bookId)
                            .execute();
                    break;
                case "2":
                    System.out.println("What would you ike the new ISBN to be?: ");
                    String newISBN = sc.nextLine();
                    handle.createUpdate("UPDATE Books SET ISBN = (?) WHERE ID = (?)")
                            .bind(0, newISBN)
                            .bind(1,bookId)
                            .execute();
                    break;
                case "3":
                    String getAuthorID = "SELECT Authors.ID "
                            + "FROM Authors JOIN bookAuthor on Authors.id = bookAuthor.authorId "
                            + "WHERE bookAuthor.bookId = "+ book.get().getID();
                    List<Integer> AuthorID = handle.createQuery(getAuthorID)
                            .mapTo(Integer.class)
                            .list();


                    System.out.println("Which author would you like to change: ");
                    System.out.println("Author(s): ");
                    for(int n = 0; n < bookAuthor.size(); n++) {
                        System.out.println((n+1) + ") " + bookAuthor.get(n));
                    }
                    userChoice = sc.nextLine();
                    System.out.println("What would you ike the new Author to be?: ");
                    String newAuthor = sc.nextLine();

                    handle.createUpdate("UPDATE Authors SET authorName = (?) WHERE ID = (?)")
                            .bind(0, newAuthor)
                            .bind(1,AuthorID.get(Integer.parseInt(userChoice)-1))
                            .execute();
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }

        else{
            System.out.println("Book not found");
        }

    }

    public static void deleteCopyOfBookByID(Handle handle){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter ID of book: ");
        int bookId = Integer.parseInt(sc.nextLine());
        Optional<Book> book = handle.createQuery("SELECT ID, ISBN, noOfCopies, Title, availableCopies FROM Books WHERE ID like (?)")
                .bind(0, bookId)
                .map((rs, ctx) -> new Book(rs.getInt("ID"), rs.getLong("ISBN"), rs.getInt("noOfCopies"), rs.getString("Title"), rs.getInt("availableCopies")))
                .findFirst();

        if (book.isPresent()) {
            if(book.get().getNoOfCopies() == 1){
                handle.createUpdate("DELETE FROM Books WHERE ID = (?)")
                        .bind(0, book.get().getID())
                        .execute();
            }
            else {
                if(book.get().getAvailableCopies() <= book.get().getNoOfCopies()){
                    if(book.get().getAvailableCopies() >= 0){
                        handle.createUpdate("UPDATE Books SET availableCopies = (?) WHERE ID = (?)")
                                .bind(0, book.get().getAvailableCopies() - 1)
                                .bind(1, book.get().getID())
                                .execute();
                    }
                    handle.createUpdate("UPDATE Books SET noOfCopies = (?) WHERE ID = (?)")
                            .bind(0, book.get().getNoOfCopies() - 1)
                            .bind(1, book.get().getID())
                            .execute();
                }

            }
        }
    }

    public static void addMember(Handle handle){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please Enter member name: ");
        String memberName = sc.nextLine();

        handle.createUpdate("insert into Members (name) values (?)")
                .bind(0,memberName)
                .execute();
    }

    public static void outPutAllMembers(Handle handle){
        List<Member> members = handle.createQuery("SELECT ID, name FROM Members ORDER BY name")
                .map((rs, ctx) -> new Member(rs.getInt("ID"), rs.getString("name")))
                .list();

        System.out.println("Library Member List:\n");
        for(int i = 0; i < members.size(); i++){
            Member member = members.get(i);
            System.out.println("\nID: " + member.getID() + "\nName: " + member.getName());
            }
    }

    public static void deleteMemberById(Handle handle){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter member ID: ");
        int memberID = Integer.parseInt(sc.nextLine());
        handle.createUpdate("DELETE FROM Members WHERE ID = (?)")
                .bind(0, memberID)
                .execute();
    }

    public static void editMemberDetailsByID(Handle handle){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter member ID: ");
        int memberID = Integer.parseInt(sc.nextLine());
        System.out.println("Enter new Member name: ");
        String newMemberName = sc.nextLine();
        handle.createUpdate("UPDATE Members SET name = (?) WHERE ID = (?)")
                .bind(0, newMemberName)
                .bind(1,memberID)
                .execute();
    }
}
