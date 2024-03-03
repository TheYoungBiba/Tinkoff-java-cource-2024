package edu.java.scrapper.model;

import edu.java.scrapper.database.InMemoryDatabase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DatabaseConnector {
    private final InMemoryDatabase databaseConnection;


}
