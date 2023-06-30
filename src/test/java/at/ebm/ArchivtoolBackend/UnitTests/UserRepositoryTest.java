package at.ebm.ArchivtoolBackend.UnitTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import at.ebm.ArchivtoolBackend.entity.User;
import at.ebm.ArchivtoolBackend.repository.UserRepository;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@Log4j2
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    
    @BeforeAll
    public void setUp() throws Exception {
        User user1= new User("johndeer", "John Deer" , "password123" , "admin");
        User user2= new User("hansbauer", "Hans Bauer" , "password123" , "user");
        this.userRepository.save(user1);
        this.userRepository.save(user2);
        Assertions.assertNotNull(user1.getUsername());
        Assertions.assertNotNull(user2.getUsername());
    }
    @Test
    public void testFetchData(){
        /*Test data retrieval*/
        User userA = userRepository.findByUsername("johndeer");
        Assertions.assertNotNull(userA);
        Assertions.assertEquals("password123", userA.getPassword());
        log.info("UserRepositoryTest finished");
    }
}
