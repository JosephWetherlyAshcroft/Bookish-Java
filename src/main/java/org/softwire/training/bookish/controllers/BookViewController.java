package org.softwire.training.bookish.controllers;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.softwire.training.bookish.models.database.Book;
import org.softwire.training.bookish.models.database.Technology;
import org.softwire.training.bookish.models.page.AboutPageModel;
import org.softwire.training.bookish.models.page.BookViewModel;
import org.softwire.training.bookish.services.LibraryService;
import org.softwire.training.bookish.services.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/browse")
public class BookViewController {
    private static final String hostname = "127.0.0.1";
    private static final String database = "mydb";
    private static final String user = "root";
    private static final String password = "BookishPassword";
    private static final String connectionString = "jdbc:mysql://" + hostname + "/" + database + "?user=" + user + "&password=" + password + "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT&useSSL=false&allowPublicKeyRetrieval=true";

    Jdbi jdbi = Jdbi.create(connectionString);
    Handle handle = jdbi.open();
    private final LibraryService libraryService = new LibraryService();
    private final org.softwire.training.bookish.services.LibraryService LibraryService;

    @Autowired
    public BookViewController(LibraryService libraryService) {
        this.LibraryService = libraryService;
    }

    @RequestMapping("")
    ModelAndView aboutUs() {

        List<Book> allBooks = libraryService.outPutAllBooks(handle);

        BookViewModel bookViewModel = new BookViewModel();
        bookViewModel.setBooks(allBooks);

        return new ModelAndView("browse", "model", bookViewModel);
    }

}
