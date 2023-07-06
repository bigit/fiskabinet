package ru.antelit.fiskabinet.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;
import ru.antelit.fiskabinet.api.bitrix.TaskDto;
import ru.antelit.fiskabinet.api.bitrix.dto.CompanyDto;
import ru.antelit.fiskabinet.api.bitrix.dto.RequisiteDto;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.KkmModel;
import ru.antelit.fiskabinet.domain.OfdProvider;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.domain.Vendor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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
    private static final String PATTERN_KKM_NUMBER = "^(\\d)\\)";
    private static final String PATTERN_DATE = "\\d{2}\\.\\d{2}\\.\\d{2,4}";
    public static final String DATE_FORMAT_SHORT = "dd.MM.yy";
    public static final String DATE_FORMAT_LONG = "dd.MM.yyyy";

    @Autowired
    private Bitrix24 bitrix24;

    @Autowired
    private KkmService kkmService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private OfdService ofdService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private TradepointService tradepointService;

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

    public void importOrganizationsData() {
        List<Organization> organizations = new ArrayList<>();

        Map<String, String> filter = new HashMap<>();
        filter.put(">UF_CRM_1524819776", "");
        List<String> select = Arrays.asList(ID, TITLE, ASSIGNED_BY_ID, LOGIN_PASSWORD, KKT, SERIAL_NUMBERS, KABINETS,
                MAINTAIN_ADDRESS, MAINTAIN_ADDRESS_2, OFD, OFD2, OFD3);
        List<CompanyDto> companies = bitrix24.getOrganizations(filter, select);

        var vendorsList = vendorService.list();
        var vendorsNames = vendorsList.stream().map(Vendor::getName).collect(toList());
//        var snPatterns = vendorsList.stream().map(Vendor::getSerialNumberPattern).collect(Collectors.toList());

        var snPatterns = Arrays.asList("001\\d{11}","049\\d{7}","0\\d{15}");
        var modelList = modelService.list();
        var modelNames = modelList.stream()
                .map(KkmModel::getName)
                .collect(toList());

        var modelMap = modelList.stream().collect(toMap(m -> m.getName().toLowerCase(), m -> m));

        var ofdList = ofdService.list().stream()
                .map(OfdProvider::getName)
                .map(ofd -> ofd.replaceAll("ОФД|ООО|\"", ""))
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(toList());


        for (CompanyDto dto : companies) {
            var org = orgService.findOrganizationBySourceId(String.valueOf(dto.getId()));
            if (org == null) {
                org = new Organization();
                org.setName(dto.getTitle());
                org.setSourceId(String.valueOf(dto.getId()));
                orgService.save(org);
            }
            organizations.add(org);

            Pattern patternFnNumber = Pattern.compile(PATTERN_FN_NUMBER);
            Pattern patternKkmNumber = Pattern.compile(PATTERN_KKM_NUMBER);
            Pattern patternDate = Pattern.compile(PATTERN_DATE);
            SimpleDateFormat dateFormatShort = new SimpleDateFormat(DATE_FORMAT_SHORT);
            SimpleDateFormat dateFormatLong = new SimpleDateFormat(DATE_FORMAT_LONG);
            Matcher matcher;

            Map<Integer, Map<Integer, Kkm>> kkmNumbersMap = new HashMap<>();

            Tradepoint defaultTradepoint, savedTradepoint = null;
            if (org.getId() != null) {
                savedTradepoint = tradepointService.findTradepointByName(org, "Без торговой точки");
            }

            if (savedTradepoint != null) {
                defaultTradepoint = savedTradepoint;
            } else {
                defaultTradepoint = tradepointService.createDefaultTradepoint();
                defaultTradepoint.setOrganization(org);
            }

            current = 1;
            Kkm kkm = new Kkm();
            kkm.setInnerName("Касса " + current);
            kkm.setTradepoint(defaultTradepoint);
            tradepointService.save(defaultTradepoint);

            if (dto.getSerialNumbers() != null) {
                boolean parsed = false;
                for (String rec : dto.getSerialNumbers()) {

                    matcher = patternKkmNumber.matcher(rec);
                    if (matcher.find()) {
                        int num = Integer.parseInt(matcher.group(1));
                        if (num > current) {
                            kkmService.save(kkm);
                            kkm = new Kkm();
                            kkm.setInnerName("Касса " + ++current);
                            kkm.setTradepoint(defaultTradepoint);
                        }
                        if (!kkmNumbersMap.containsKey(dto.getId())) {
                            kkmNumbersMap.put(dto.getId(), new HashMap<>());
                        }
                        kkmNumbersMap.get(dto.getId()).put(num, kkm);
                    }

                    for (String model: modelNames) {
                        if (rec.toLowerCase().contains(model)){
                            kkm.setKkmModel(modelMap.get(model));
                            break;
                        }
                    }

                    for (String ptrn: snPatterns) {
                        Pattern pattern = Pattern.compile(ptrn);
                        matcher = pattern.matcher(rec);
                        matcher.reset();
                        if (matcher.find()) {
                            kkm.setSerialNumber(matcher.group());
                            break;
                        }
                    }

                    if (rec.contains("ФН")) {
                        if (kkm.getFnNumber() != null) {
                            kkmService.save(kkm);
                            kkm = new Kkm();
                            kkm.setInnerName("Касса " + ++current);
                            kkm.setTradepoint(defaultTradepoint);
                        }
                        matcher = patternFnNumber.matcher(rec);
                        if (matcher.find()) {
                            String fnNumber = matcher.group();
                            kkm.setFnNumber(Long.valueOf(fnNumber));
                            parsed = true;
                        }
                        matcher = patternDate.matcher(rec);
                        if (matcher.find()) {
                            parsed = true;
                            String strDate = matcher.group();
                            try {
                                kkm.setFnEnd(dateFormatShort.parse(strDate));
                            } catch (ParseException e) {
                                try {
                                    kkm.setFnEnd(dateFormatLong.parse(strDate));
                                } catch (ParseException e1) {
                                    log.error("Не распознана дата окончания срока ФН ({})", strDate);
                                }
                            }
                        }
                        kkmService.save(kkm);
                        if (!parsed) {
                            System.out.printf("Не обработана строка %s", rec);
                        }
                    }
                }
            }
        }

        List<String> organizationsIds = organizations.stream()
                .map(Organization::getSourceId)
                .collect(toList());

        Map<String, Object> reqFilter = new HashMap<>();
        reqFilter.put("ENTITY_ID", organizationsIds);
        List<RequisiteDto> requisiteDtos = bitrix24.getRequisites(reqFilter, null);

        Map<String, Organization> orgsBySourceId = organizations.stream().collect(toMap(Organization::getSourceId, org -> org));
        for (RequisiteDto req : requisiteDtos) {
            String id = req.getEntityId();
            var org = orgsBySourceId.get(id);
            if (org.getInn() != null) {
                continue;
            }
            if (req.getInn() != null) {
                org.setInn(req.getInn());
            } else {
                log.error("Не найден ИНН для {}", org.getName());
            }
            orgService.save(org);
        }
    }
    public String getCompanyUrl(int id) {
        return bitrix24.getCompanyUrl(id);
    }

}
