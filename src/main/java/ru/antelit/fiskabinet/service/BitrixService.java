package ru.antelit.fiskabinet.service;

import liquibase.exception.DateParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.antelit.fiskabinet.api.bitrix.Bitrix24;
import ru.antelit.fiskabinet.api.bitrix.TaskDto;
import ru.antelit.fiskabinet.api.bitrix.dto.CompanyDto;
import ru.antelit.fiskabinet.api.bitrix.dto.RequisiteDto;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.domain.Tradepoint;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.domain.Vendor;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
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

    private static final String PATTERN_FN_NUMBER = "[№]?(\\d{16})";
    private static final String PATTERN_KKM_NUMBER = "(^(\\d)[).]?)";
    private static final String PATTERN_DATE = "\\d{2}[.,]\\d{2}[.,]\\d{2,4}";
    private static final List<String> DATE_TIME_PATTERNS = asList("dd.MM.yy", "dd.MM.yyyy", "ddMMyyyy");

    private final Bitrix24 bitrix24;
    private final KkmService kkmService;
    private final ModelService modelService;
    private final OrgService orgService;
    private final TradepointService tradepointService;
    private final OfdService ofdService;
    private final VendorService vendorService;

    @Value("${bitrix24.responsible-id}")
    private Integer responsibleId;

    @Autowired
    public BitrixService(Bitrix24 bitrix24, KkmService kkmService, ModelService modelService, OfdService ofdService,
                         OrgService orgService, TradepointService tradepointService, VendorService vendorService) {
        this.bitrix24 = bitrix24;
        this.kkmService = kkmService;
        this.modelService = modelService;
        this.ofdService = ofdService;
        this.orgService = orgService;
        this.tradepointService = tradepointService;
        this.vendorService = vendorService;
    }

    public void createTask(UserInfo user, Kkm kkm) throws ExecutionException, InterruptedException {
        TaskDto task = new TaskDto();
        task.setResponsibleId(responsibleId);
        task.setTitle("Заявка на обслуживание");
        task.setDescription(
                String.format("Заявка на обслуживание от %s. %s зав. номер %s",
                        user.getShortName(), kkm.getKkmModel().getFullName(), kkm.getFnNumber()));
        bitrix24.createTask(task);
    }

    public String importOrganizationsData() {
        List<Organization> organizations = new ArrayList<>();

        Map<String, String> filter = new HashMap<>();
        filter.put(">UF_CRM_1524819776", "");
        List<String> select = asList(ID, TITLE, ASSIGNED_BY_ID, LOGIN_PASSWORD, KKT, SERIAL_NUMBERS, KABINETS,
                MAINTAIN_ADDRESS, MAINTAIN_ADDRESS_2, OFD, OFD2, OFD3);
        List<CompanyDto> companies = bitrix24.getOrganizations(filter, select);

        var vendorsList = vendorService.list();
        var vendorsNames = vendorsList.stream().map(Vendor::getName).collect(toList());

        var snPatterns = asList("(?u)[№]?(001\\d{11})", "(?u)[№]?(049\\d{7})", "(?u)[№]?(0\\d{15})", "(?u)[№]?(003\\d{11})");

        var modelList = modelService.list();
        var modelMap = modelList.stream().collect(toMap(m -> m.getName().toLowerCase(), m -> m));


        var ofdList = ofdService.list();
        var ofdMap = ofdList.stream()
                .collect(toMap(o -> o.getName().replaceAll("(?u)ОФД|ООО|\"", "").trim(), o -> o));
        var ofdPatterns = ofdMap.keySet();

        int unparsed = 0;
        CompanyDto lastLogged = new CompanyDto();
        StringBuilder report = new StringBuilder();

        for (CompanyDto dto : companies) {

            var org = importOrganizationInfo(dto);
            organizations.add(org);

            Pattern patternFnNumber = Pattern.compile(PATTERN_FN_NUMBER);
            Pattern patternKkmNumber = Pattern.compile(PATTERN_KKM_NUMBER);
            Pattern patternDate = Pattern.compile(PATTERN_DATE);

            Matcher matcher;

            Map<Integer, Map<Integer, Kkm>> kkmNumbersMap = new HashMap<>();

            Tradepoint defaultTradepoint;
            defaultTradepoint = tradepointService.getDefaultTradepoint(org);
            if (defaultTradepoint == null) {
                defaultTradepoint = tradepointService.createDefaultTradepoint(org);
                tradepointService.save(defaultTradepoint);
            }

            if (dto.getSerialNumbers() != null) {
                log.info("Организация {}", org.getName());
                int current = 1;

                log.info("  Новая ККМ");
                Kkm kkm = new Kkm();
                kkm.setInnerName("  Касса " + current);
                kkm.setTradepoint(defaultTradepoint);

                for (String rec : dto.getSerialNumbers()) {
                    log.info("  Парсим строку {}", rec);

                    matcher = patternKkmNumber.matcher(rec);
                    if (matcher.find()) {
                        int num = Integer.parseInt(matcher.group(2));
                        if (num > current) {
                            saveKkm(kkm);
                            kkm = new Kkm();
                            kkm.setInnerName("  Касса " + ++current);
                            kkm.setTradepoint(defaultTradepoint);

                            log.info("  Новая ККМ ({})", current);
                        }
                        rec = rec.replace(matcher.group(0), "").trim();
                        if (!kkmNumbersMap.containsKey(dto.getId())) {
                            kkmNumbersMap.put(dto.getId(), new HashMap<>());
                        }
                        kkmNumbersMap.get(dto.getId()).put(num, kkm);
                    }

                    // -- Модель --
                    for (String model : modelMap.keySet()) {
                        Pattern pattern = Pattern.compile(model, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
                        matcher = pattern.matcher(rec);
                        matcher.reset();
                        if (matcher.find()) {
                            log.info("  Найдена модель {}", model);
                            rec = rec.replace(matcher.group(), "");
                            kkm.setKkmModel(modelMap.get(model));
                        }
                    }

                    for (String snumPattern : snPatterns) {
                        Pattern pattern = Pattern.compile(snumPattern);
                        matcher = pattern.matcher(rec);
                        matcher.reset();
                        if (matcher.find()) {
                            String snum = matcher.group(1);
                            log.info("  Найден серийный номер {}", snum);
                            rec = rec.replace(matcher.group(0), "").trim();

                            Kkm savedKkm = kkmService.getBySerialNumber(snum);
                            if (savedKkm != null) {
                                kkm = savedKkm;
                                break;
                            }
                            kkm.setSerialNumber(snum);
                            break;
                        }
                    }

                    //Номер ФН
                    if (StringUtils.containsIgnoreCase(rec, "ФН")) {
                        matcher = patternFnNumber.matcher(rec);
                        matcher.reset();
                        if (matcher.find()) {
                            String fnNumber = matcher.group(1);
                            var saved = kkmService.getByFnNumber(fnNumber);
                            if (saved != null) {
                                kkm = saved;
                            } else {
                                kkm.setFnNumber(fnNumber);
                            }
                            log.info("  Найден номер ФН {}", fnNumber);
                            rec = rec.replaceAll("ФН|фн", "")
                                    .replace(matcher.group(0), "").trim();
                        }
                        matcher = patternDate.matcher(rec);
                        if (matcher.find()) {

                            String strDate = matcher.group();
                            log.info("  Найдена дата ФН {}", strDate);
                            rec = rec.replace(matcher.group(0), "").trim();
                            try {
                                LocalDate fnEnd = parseDate(strDate);
                                kkm.setFnEnd(fnEnd);
                                log.info("  Распарсена дата ФН {}", fnEnd);
                            } catch (ParseException e1) {
                                log.error(" Для организации {} не распознана дата окончания срока ФН ({})",
                                        org.getName(), strDate);
                            }
                        }
                    }

                    //-- ОФД --//
                    for (String ofd : ofdPatterns) {
                        Pattern pattern = Pattern.compile("(" + ofd + ")" + " (ОФД)?",
                                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
                        matcher.reset();
                        matcher = pattern.matcher(rec);
                        if (matcher.find()) {
                            log.info("  Найден офд {}", ofd);
                            kkm.setOfdProvider(ofdMap.get(ofd));
                            saveKkm(kkm);
                            rec = rec.replaceAll(matcher.group(0), "").trim();

                            matcher = patternDate.matcher(rec);
                            if (matcher.find()) {
                                String strDate = matcher.group();
                                log.info("  Найдена дата ОФД {}", strDate);
                                rec = rec.replace(matcher.group(0), "").trim();
                                try {
                                    LocalDate ofdDate = parseDate(strDate);
                                    kkm.setOfdSubEnd(ofdDate);
                                    log.info("  Распарсена дата ОФД {}", ofdDate);
                                } catch (ParseException e) {
                                    log.error(" Для организации {} не распознана дата окончания срока ОФД {}",
                                            org.getName(), strDate);
                                }
                            }
                            break;
                        }
                    }
                    saveKkm(kkm);

                    for (String vendor: vendorsNames) {
                       rec = rec.replaceAll("(?ui)" + vendor, "").trim();
                    }

                    StringJoiner joiner = new StringJoiner("|");
                    joiner.add("до|дл")
                            .add("г[.]?")
                            .add("(1\\.\\d ?м?(/(15|36))?)")
                            .add("(исп[. ]? ?|( ?(ин|ав)? ?\\d{2}-(\\d{1,2}м?)))")
                            .add("заводской номер")
                            .add("з/н")
                            .add("ккт")
                            .add("~|-|,")
                            .add("ФН")
                            .add("№*\\d?");
                        rec = rec.replaceAll(
                                "(?ui)" + joiner, "").trim();
                    if (rec.length() > 0) {
                        String msg = String.format("Не обработана строка %s", rec);
                        log.error(msg);
                        if (lastLogged != dto) {
                            lastLogged = dto;
                            report.append(dto.getTitle()).append("\n");
                        }
                        report.append(msg).append("\n");
                        unparsed++;
                    }
                }
            }
        }
        log.info("Необработано строк {}", unparsed);
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
            importRequisites(org, req);
            orgService.save(org);
        }
        return report.toString();
    }

    private Organization importOrganizationInfo(final CompanyDto company) {
        var org = orgService.findOrganizationBySourceId(String.valueOf(company.getId()));
        if (org == null) {
            org = new Organization();
            org.setName(company.getTitle());
            org.setSourceId(String.valueOf(company.getId()));
            orgService.save(org);
        }
        return org;
    }

    private void importRequisites(Organization org, final RequisiteDto requisiteDto) {
        String inn = requisiteDto.getInn();
        if (inn == null) {
            return;
        }
        if (org.getInn() != null) {
            if (!org.getInn().equals(requisiteDto.getInn())) {
                log.error("Для организации {} указаны разные ИНН {} и {}",
                        org.getName(), org.getInn(), requisiteDto.getInn());
                return;
            }
        }
        org.setInn(requisiteDto.getInn());
    }

    private LocalDate parseDate(String strDate) throws ParseException {
        LocalDate date = null;
        for (String pattern : DATE_TIME_PATTERNS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                date = LocalDate.parse(strDate, formatter);
            } catch (DateTimeParseException ignored) {
            }
            return date;
        }
        throw new DateParseException("Unable parse date from string " + strDate);
    }

    private void saveKkm(Kkm kkm) {
        if (kkm.getFnNumber() != null || kkm.getSerialNumber() != null) {
            kkmService.save(kkm);
        } else {
            log.error("    Не удалось сохранить кассу {}", kkm);
        }
    }

    public List<CompanyDto> findCompaniesByName(String query) {
        return bitrix24.findCompanyByName(query);
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

}
