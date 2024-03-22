package ru.antelit.fiskabinet.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.antelit.fiskabinet.FiskabinetTestConfiguration;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;
import ru.antelit.fiskabinet.api.bitrix.TaskDto;
import ru.antelit.fiskabinet.api.bitrix.dto.CompanyDto;
import ru.antelit.fiskabinet.api.bitrix.dto.RequisiteDto;
import ru.antelit.fiskabinet.domain.Organization;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.ASSIGNED_BY_ID;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.ID;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.KABINETS;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.KKT;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.LOGIN_PASSWORD;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.MAINTAIN_ADDRESS;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.MAINTAIN_ADDRESS_2;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.OFD;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.OFD2;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.OFD3;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.SERIAL_NUMBERS;
import static ru.antelit.fiskabinet.api.bitrix.Bitrix24.TITLE;

@SpringBootTest(classes = FiskabinetTestConfiguration.class)
@ActiveProfiles("test")
class Bitrix24Test {

    @Autowired
    @Qualifier("bitrix.test")
    private Bitrix24 bitrix24Prod;

    @Autowired
    @Qualifier("bitrix.post")
    private Bitrix24 bitrix24Post;

    @Test
    void createTask() throws ExecutionException, InterruptedException, TimeoutException {
        TaskDto task = new TaskDto();
        task.setTitle("Тестовая задача");
        task.setResponsibleId(21);
        task.setDescription("Заявка");
        Integer res = 0;
        TaskDto actual = bitrix24Post.createTask(task);
        Assertions.assertEquals(task.getTitle(), actual.getTitle());
    }

    @Test
    void testImportFromBitrix() {
        Map<String, String> filter = new HashMap<>();
        filter.put(">UF_CRM_1524819776", "");

        List<String> select = Arrays.asList(ID, TITLE, ASSIGNED_BY_ID, LOGIN_PASSWORD, KKT, SERIAL_NUMBERS, KABINETS,
                MAINTAIN_ADDRESS, MAINTAIN_ADDRESS_2, OFD, OFD2, OFD3);
       List<CompanyDto> org =  bitrix24Prod.getOrganizations(filter, select);
       Assertions.assertEquals(158, org.size());
    }

    @Test
    void testRequisiteRequest() {
        List<String> ids = List.of("10", "20", "30");

        Map<String, Object> filter = new HashMap<>();
        filter.put("ENTITY_ID", ids);
        List<RequisiteDto> reqs = bitrix24Prod.getRequisites(filter, null);
    }
}