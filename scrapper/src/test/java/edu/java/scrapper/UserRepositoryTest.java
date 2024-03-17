package edu.java.scrapper;

import edu.java.scrapper.domain.BaseUsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
public class UserRepositoryTest extends AbstractIntegrationDBTest {
    @Autowired
    private BaseUsersRepository usersRepository;

//    @BeforeEach
//    public void fillTable() {
//        usersRepository.findAll(
//            List.of()
//        )
//    }

//    @Test
//    public void addUserTest() {
//        usersRepository.add(new User(1L));
//        assertEquals(new User(1L), usersRepository.find(1L).get());
//    }

    @AfterEach
    public void dropTable() {
        usersRepository.dropTable();
    }
}
