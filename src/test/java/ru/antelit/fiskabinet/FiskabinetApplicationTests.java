package ru.antelit.fiskabinet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.antelit.fiskabinet.service.ActivationCodeService;

@SpringBootTest(classes = FiskabinetTestConfiguration.class)
@ActiveProfiles("test")
public class FiskabinetApplicationTests {

}