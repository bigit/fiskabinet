package ru.antelit.fiskabinet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;
import ru.antelit.fiskabinet.api.bitrix.model.CompanyDto;
import ru.antelit.fiskabinet.api.bitrix.model.RequisiteDto;
import ru.antelit.fiskabinet.api.bitrix.model.TaskDto;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.UserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class BitrixService {

    private final Bitrix24 bitrix24;
    private final OrgService orgService;

    @Value("${bitrix24.responsible-id}")
    private Integer responsibleId;

    @Autowired
    public BitrixService(Bitrix24 bitrix24, OrgService orgService) {
        this.bitrix24 = bitrix24;

        this.orgService = orgService;
    }

    public TaskDto createTask(UserInfo user, Kkm kkm) throws ExecutionException, InterruptedException {
        TaskDto task = new TaskDto();
        task.setResponsibleId(responsibleId);
        task.setTitle("Заявка на обслуживание");
        task.setDescription(
                String.format("Заявка на обслуживание от %s. %s зав. номер %s",
                        user.getShortName(), kkm.getKkmModel().getFullName(), kkm.getFnNumber()));
        return bitrix24.createTask(task);
    }

    public List<CompanyDto> findCompaniesByName(String query) {
        return bitrix24.findCompanyByName(query);
    }

    public RequisiteDto getRequisites(CompanyDto companyDto) {
        Map<String, String> filter = new HashMap<>();
        filter.put("ENTITY_ID", companyDto.getId());
        var response = bitrix24.getRequisites(filter, null);
        return !response.isEmpty() ? response.get(0) : new RequisiteDto();
    }

    public void setImportStatus(List<CompanyDto> companies) {
        var importedIds = orgService.findImportedFromBitrixId(companies.stream()
                .map(CompanyDto::getId)
                .map(String::valueOf)
                .collect(toList()));
        companies.forEach(c -> c.setImported(importedIds.contains(String.valueOf(c.getId()))));
    }

    public String getCompanyUrl(String id) {
        return bitrix24.getCompanyUrl(id);
    }

    public List<CompanyDto> findCompaniesByInn(String query) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("%REQ_INN", query);
        bitrix24.findCompanyByName(query);
        return null;
    }
}
