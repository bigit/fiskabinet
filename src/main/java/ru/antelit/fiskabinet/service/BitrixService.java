package ru.antelit.fiskabinet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;
import ru.antelit.fiskabinet.api.bitrix.dto.CompanyDto;
import ru.antelit.fiskabinet.api.bitrix.TaskDto;
import ru.antelit.fiskabinet.api.bitrix.dto.RequisiteDto;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.UserInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

@Slf4j
@Service
public class BitrixService {

    private static final String PATTERN_FN_NUMBER = "\\d{16}";
    private static final String PATTERN_KKM_NUMBER = "(\\d)\\)";
    private static final String PATTERN_DATE = "\\d{2}\\.\\d{2}\\.d{2,4}";

    @Autowired
    private Bitrix24 bitrix24;

    @Autowired
    private KkmService kkmService;

    @Autowired
    private OrgService orgService;

    @Value("${bitrix24.responsible-id}")
    private Integer responsibleId;
    private int current;

    public void createTask(UserInfo user, Kkm kkm) throws ExecutionException, InterruptedException, TimeoutException {
        TaskDto task = new TaskDto();
        task.setResponsibleId(responsibleId);
        task.setTitle("Заявка на обслуживание");
        task.setDescription(
                String.format("Заявка на обслуживание от %s. %s зав. номер %d",
                        user.getShortName(), kkm.getKkmModel().getFullName(), kkm.getFnNumber()));
        bitrix24.createTask(task);
    }

    @Transactional
    public void importOrganizationsData() {
        Map<String, String> filter = new HashMap<>();
        filter.put(">UF_CRM_1524819776", "");
        List<String> select = Arrays.asList(ID, TITLE, ASSIGNED_BY_ID, LOGIN_PASSWORD, KKT, SERIAL_NUMBERS, KABINETS,
                MAINTAIN_ADDRESS, MAINTAIN_ADDRESS_2, OFD, OFD2, OFD3);
        List<CompanyDto> companies = bitrix24.getOrganizations(filter, select);
        List<Organization> organizations = new ArrayList<>();
        for (CompanyDto dto : companies) {

            var org = orgService.findOrganizationBySourceId(String.valueOf(dto.getId()));
            if (org == null) {
                org = new Organization();
            }

            Pattern patternFnNumber = Pattern.compile(PATTERN_FN_NUMBER);
            Pattern patternKkmNumber = Pattern.compile(PATTERN_KKM_NUMBER);
            Pattern patternDate = Pattern.compile(PATTERN_DATE);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy[yy]");

            Matcher matcher;

            Map<Integer, Map<Integer, Kkm>> kkmNumbersMap = new HashMap<>();

            current = 1;
            Kkm kkm = new Kkm();

            for (String rec : dto.getKkt()) {
                matcher = patternKkmNumber.matcher(rec);
                if (matcher.find()) {
                    int num = Integer.parseInt(matcher.group());
                    if (num > current) {
                        kkmService.save(kkm);
                        kkm = new Kkm();
                        current++;
                    }
                    if (!kkmNumbersMap.containsKey(dto.getId())) {
                        kkmNumbersMap.put(dto.getId(), new HashMap<>());
                    }
                    kkmNumbersMap.get(dto.getId()).put(num, kkm);
                }
                if (rec.contains("ФН")) {
                    if (kkm.getFnNumber() != null) {
                        kkmService.save(kkm);
                        kkm = new Kkm();
                        current++;
                    }
                    matcher = patternFnNumber.matcher(rec);
                    String fnNumber = matcher.group();
                    kkm.setFnNumber(Long.getLong(fnNumber));

                    matcher = patternDate.matcher(rec);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy[yy]");

                    if (matcher.find()) {

                        String strDate = matcher.group();
                        try {
                            kkm.setFnEnd(sdf.parse(matcher.group()));
                        } catch (ParseException e) {
                            log.error("Не распознана дата окончания срока ФН ({})", strDate);
                        }

                    }
                }
            }

            org.setSourceId(String.valueOf(dto.getId()));
            org.setName(dto.getTitle());
            organizations.add(org);
        }

        List<String> organizationsIds = organizations.stream()
                .map(Organization::getSourceId)
                .collect(Collectors.toList());

        Map<String, List<String>> reqFilter = new HashMap<>();
        reqFilter.put("ENTITY_ID", organizationsIds);
        List<RequisiteDto> requisiteDtos = bitrix24.getRequisites(reqFilter, null);

        Map<String, Organization> orgsBySourceId = organizations.stream().collect(Collectors.toMap(Organization::getSourceId, org -> org));
        for (RequisiteDto req : requisiteDtos) {
            String id = req.getEntityId();
            orgsBySourceId.get(id).setInn(req.getInn());
        }
    }


    public String getCompanyUrl(int id) {
        return bitrix24.getCompanyUrl(id);
    }

}
