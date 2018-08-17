package fr.insee.bidbo.ws.interne.test;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.insee.bidbo.dao.BaseRDF;
import fr.insee.bidbo.service.ColonneService;

@ContextConfiguration(locations = { "classpath:spring-ws-interne-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class ChargementDonneesAgregeesServiceTest {

    @Autowired
    private ColonneService colonneService;

    @Test
    public void test() throws IOException {
	System.out.println(colonneService.findAll(BaseRDF.INTERNE));
    }

}
