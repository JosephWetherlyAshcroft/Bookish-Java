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

    public static void main(String[] args) throws SQLException {
        String hostname = "127.0.0.1";
        String database = "mydb";
        String user = "root";
        String password = "BookishPassword";
        String connectionString = "jdbc:mysql://" + hostname + "/" + database + "?user=" + user + "&password=" + password + "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT&useSSL=false&allowPublicKeyRetrieval=true";

        jdbcMethod(connectionString);
        jdbiMethod(connectionString);
    }

    private static void jdbcMethod(String connectionString) throws SQLException {
        System.out.println("JDBC method...");

        // TODO: print out the details of all the books (using JDBC)
        // See this page for details: https://docs.oracle.com/javase/tutorial/jdbc/basics/processingsqlstatements.html

        Connection connection = DriverManager.getConnection(connectionString);
    }

    private static void jdbiMethod(String connectionString) {
        System.out.println("\nJDBI method...");

        // TODO: print out the details of all the books (using JDBI)
        // See this page for details: http://jdbi.org
        // Use the "Book" class that we've created for you (in the models.database folder)
        Jdbi jdbi = Jdbi.create(connectionString);
        Handle handle = jdbi.open();
        handle.registerRowMapper(ConstructorMapper.factory(Book.class));
//        List<Book> books = handle.createQuery("SELECT * FROM Books ORDER BY ID")
//                    .mapTo(Book.class)
//                    .list();
        List<Book> books = handle.createQuery("SELECT id, ISBN, noOfCopies, name, availableCopies FROM user")
                .map((rs, ctx) -> new Book(rs.getInt("ID"), rs.getLong("ISBN"), rs.getInt("noOfCopies"), rs.getString("name"), rs.getInt("availableCopies")))
                .list();



        for(int i = 0; i < books.size(); i++){
            Book book = books.get(i);
            System.out.println("ID, Title, ISBN, Number of copies, Available copies");
            System.out.println(book.getID() + ", " + book.getTitle() + ", " + book.getISBN()  + ", " + book.getNoOfCopies()  + ", " + book.getAvailableCopies());
        }
    }
}
