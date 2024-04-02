package ru.antelit.fiskabinet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;
import ru.antelit.fiskabinet.api.bitrix.model.CompanyDto;
import ru.antelit.fiskabinet.api.bitrix.model.CompanyInfo;
import ru.antelit.fiskabinet.api.bitrix.model.RequisiteDto;
import ru.antelit.fiskabinet.api.bitrix.model.TaskDto;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class BitrixService {

    private final Bitrix24 bitrix24;


    @Value("${bitrix24.responsible-id}")
    private Integer responsibleId;

    @Autowired
    public BitrixService(Bitrix24 bitrix24, OrgService orgService) {
        this.bitrix24 = bitrix24;
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

    public List<CompanyInfo> findCompaniesByName(String query) {
        var companies = bitrix24.findCompanyByName(query);
        var companiesIds = companies.stream().map(CompanyDto::getId).collect(toList());
        Map<String, Object> filter = new HashMap<>();
        filter.put("ENTITY_ID", companiesIds);
        var requisites = bitrix24.getRequisites(filter).stream()
                .filter(req -> req.getInn() != null && !req.getInn().isBlank())
                .collect(groupingBy(RequisiteDto::getEntityId));
        List<CompanyInfo> result = new ArrayList<>();
        for (var company : companies) {
            var companyInfo = new CompanyInfo();
            companyInfo.setName(company.getTitle());
            companyInfo.setSourceId(company.getId());
            if (requisites.get(company.getId()) == null) {
                companyInfo.addMessage("У организации отсутствуют реквизиты", true);
            } else {
                if (requisites.get(company.getId()).size() > 1) {
                    companyInfo.addMessage("У организации указано несколько реквизитов", true);
                } else {
                    companyInfo.setInn(requisites.get(company.getId()).get(0).getInn());
                }
            }
                result.add(companyInfo);
            }
            return result;
        }

        public RequisiteDto getRequisites (CompanyDto company){
            Map<String, Object> filter = new HashMap<>();
            filter.put("ENTITY_ID", company.getId());
            var response = bitrix24.getRequisites(filter, null);
            return !response.isEmpty() ? response.get(0) : new RequisiteDto();
        }

        public String getCompanyUrl (String id){
            return bitrix24.getCompanyUrl(id);
        }

        public List<CompanyInfo> findCompaniesByRequisiteName(String name) {
            Map<String, Object> filter = new HashMap<>();
            filter.put("%RQ_COMPANY_NAME", name);
            List<RequisiteDto> requisites = bitrix24.getRequisites(filter).stream()
                    .filter(req -> (!req.getCompanyName().isBlank() || !req.getCompanyFullName().isBlank()))
                    .filter(req -> Objects.nonNull(req.getInn()))
                    .collect(toList());
            return convert(requisites);
    }

        public List<CompanyInfo> findCompaniesByRequisiteInn (String query) {
            Map<String, Object> filter = new HashMap<>();
            filter.put("%RQ_INN", query);
            List<RequisiteDto> requisites = bitrix24.getRequisites(filter).stream()
                    .filter(req -> Objects.nonNull(req.getCompanyName()))
                    .filter(req -> (!req.getCompanyName().isBlank() || !req.getCompanyFullName().isBlank()))
                    .collect(toList());
            return convert(requisites);
        }

        private List<CompanyInfo> convert(List<RequisiteDto> requisites) {
            List<CompanyInfo> companies = new ArrayList<>();
            for (RequisiteDto req : requisites) {
                CompanyInfo info = new CompanyInfo();
                info.setInn(req.getInn());
                info.setName(!req.getCompanyName().isBlank() ? req.getCompanyName() : req.getCompanyFullName());
                info.setSourceId(req.getEntityId());
                companies.add(info);
            }
            return companies;
        }
    }
