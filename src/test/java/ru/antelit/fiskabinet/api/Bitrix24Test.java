package ru.antelit.fiskabinet.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.antelit.fiskabinet.FiskabinetTestConfiguration;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;
import ru.antelit.fiskabinet.api.bitrix.model.RequisiteDto;
import ru.antelit.fiskabinet.api.bitrix.model.TaskDto;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
        TaskDto actual = bitrix24Post.createTask(task);
        Assertions.assertEquals(task.getTitle(), actual.getTitle());
    }

    @Test
    void testRequisiteRequest() {
        List<String> ids = List.of("10", "20", "30");

        Map<String, Object> filter = new HashMap<>();
        filter.put("ENTITY_ID", ids);
        List<RequisiteDto> reqs = bitrix24Prod.getRequisites(filter, null);
    }
}