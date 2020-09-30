package org.softwire.training.bookish.services;

import org.jdbi.v3.core.Jdbi;

public abstract class DatabaseService {

    private static final String hostname = "127.0.0.1";
    private static final String database = "mydb";
    private static final String user = "root";
    private static final String password = "BookishPassword";
    private static final String connectionString = "jdbc:mysql://" + hostname + "/" + database + "?user=" + user + "&password=" + password + "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT&useSSL=false&allowPublicKeyRetrieval=true";

    protected final Jdbi jdbi = Jdbi.create(connectionString);
}
