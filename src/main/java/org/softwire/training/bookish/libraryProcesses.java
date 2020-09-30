package org.softwire.training.bookish;
import org.jdbi.v3.core.Handle;
import org.softwire.training.bookish.models.database.Book;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class libraryProcesses {
    public static void outPutAllBooks(Handle handle) {
        List<Book> books = handle.createQuery("SELECT ID, ISBN, noOfCopies, Title, availableCopies FROM Books")
                .map((rs, ctx) -> new Book(rs.getInt("ID"), rs.getLong("ISBN"), rs.getInt("noOfCopies"), rs.getString("Title"), rs.getInt("availableCopies")))
                .list();

        System.out.println("Library Books List:\n");
        for(int i = 0; i < books.size(); i++){
            Book book = books.get(i);
            System.out.println("\nID: " + book.getID() + "\nTitle: " + book.getTitle() + "\nISBN: " + book.getISBN() + "\nNumber of copies: " +  book.getNoOfCopies() + "\nAvailable copies: " + book.getAvailableCopies());

            String SELECT_ALL = "SELECT Authors.authorName "
                    + "FROM Authors JOIN bookAuthor on Authors.id = bookAuthor.authorId "
                    + "WHERE bookAuthor.bookId = "+ book.getID();
            List<String> bookAuthor = handle.createQuery(SELECT_ALL)
                    .mapTo(String.class)
                    .list();

            System.out.println("Author(s): ");
            for(int n = 0; n < bookAuthor.size(); n++){
                System.out.println("- " + bookAuthor.get(n));
            }
        }
    }

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
        String titleInput = sc.next();
        System.out.println("ISBN: ");
        long isbnInput = Long.parseLong(sc.next());
        System.out.println("How many authors?: ");
        int noOfAuthorsInput = Integer.parseInt(sc.next());
        System.out.println("How many copies?: ");
        int copiesInput = Integer.parseInt(sc.next());

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
            String authorInput = sc.next();
            for(int x = 0; x < authors.size(); x++){
                Author author = authors.get(i);
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
}
