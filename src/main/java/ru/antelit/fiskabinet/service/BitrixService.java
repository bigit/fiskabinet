package ru.antelit.fiskabinet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.api.Bitrix24;
import ru.antelit.fiskabinet.api.BitrixTask;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.UserInfo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class BitrixService {

    @Autowired
    private Bitrix24 bitrix24;

    @Value("${bitrix24.responsible-id}")
    private Integer responsibleId;

    public void createTask(UserInfo user, Kkm kkm) throws ExecutionException, InterruptedException, TimeoutException {
        BitrixTask task = new BitrixTask();
        task.setResponsibleId(responsibleId);
        task.setTitle("Заявка на обслуживание");
        task.setDescription(
                String.format("Заявка на обслуживание от %s. %s зав. номер %d",
                        user.getShortName(), kkm.getKkmModel().getFullName(), kkm.getFnNumber()));
        bitrix24.createTask(task);
    }

    public void importOrganizationsData() {

    }
}
