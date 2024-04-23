package ru.antelit.fiskabinet.service;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.jxls.area.XlsArea;
import org.jxls.command.EachCommand;
import org.jxls.common.AreaRef;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.cellshift.InnerCellShiftStrategy;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import ru.antelit.fiskabinet.domain.OfdProvider;
import ru.antelit.fiskabinet.report.RegFormDto;
import ru.antelit.fiskabinet.report.StepCellRefGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ReportService {

    private static final char MARK_X = 'X';
    private static final String EXCL = "!";
    public static final String PAGE_1 = "стр.1";
    private static final String PAGE_3 = "стр.3_Разд.1";
    private static final String PAGE_4 = "стр.4_Разд.1";
    private static final String PAGE_5 = "стр.5_Разд.2";
    public static final String PAGE_6 = "стр.6_Разд.2";
    public static final String PAGE_9 = "стр.9_Разд.3";
    public static final String PAGE_10 = "стр.10_Разд.4";

    @Autowired
    private OfdService ofdService;
    @Autowired
    private DateTimeFormatter timeReportFormatter;
    @Autowired
    private DateTimeFormatter dateReportFormatter;

    @Async
    public File createRegistrationApplication(RegFormDto regFormDto) throws IllegalArgumentException {

        File templateFile;
        FileOutputStream fos;
        FileInputStream fis;
        File outputFile = new File("/tmp/tmp.xls");
        Context context = new Context();

        context.putVar("inn", prepareString(regFormDto.getInn(), 12, 12));
        context.putVar("orgName", prepareString(regFormDto.getName(), 40, 120));
        context.putVar("isRegistration", regFormDto.getIsRegistration() ? 1 : 2);

        context.putVar("changeAddressPlace",    regFormDto.isChangeAddressOrPlace() ? MARK_X : " ");
        context.putVar("changeOfd",             regFormDto.isChangeOfd() ? MARK_X : " ");
        context.putVar("changeAutomatic",       regFormDto.isChangeAutomatic() ? MARK_X : " ");
        context.putVar("changeFn",              regFormDto.isChangeFn() ? MARK_X : " ");
        context.putVar("changeOffAutonomous",   regFormDto.isChangeOffAutonomous() ? MARK_X : " ");
        context.putVar("changeOnAutonomous",    regFormDto.isChangeOnAutonomous() ? MARK_X : " ");
        context.putVar("changeUserName",        regFormDto.isChangeUserName() ? MARK_X : " ");
        context.putVar("changeOther",           regFormDto.isChangeOther() ? MARK_X : " ");

        context.putVar("isUser",        regFormDto.getIsUser() ? 1 : 2);
        context.putVar("declarerName",  prepareString(regFormDto.getDeclarer(), 20, 60));
        context.putVar("modelName",     prepareString(regFormDto.getModel(), 20, 120));
        context.putVar("serialNumber",  prepareString(regFormDto.getSerialNumber(), 40, 40));
        context.putVar("fnModel",       prepareString(regFormDto.getFnModel(), 20,120));
        context.putVar("fnNumber",      prepareString(regFormDto.getFnNumber(), 20));
        context.putVar("postIndex",     prepareString(regFormDto.getPostIndex(), 6));
        context.putVar("county",        prepareString(regFormDto.getCounty(), 30));
        context.putVar("city",          prepareString(regFormDto.getCity(), 30));
        context.putVar("settlement",    prepareString(regFormDto.getSettlement(), 30));
        context.putVar("street",        prepareString(regFormDto.getStreet(), 30));
        context.putVar("number",        prepareString(regFormDto.getNumber(), 8));
        context.putVar("building",      prepareString(regFormDto.getBuilding(), 8));
        context.putVar("room",          prepareString(regFormDto.getRoom(), 8));
        context.putVar("place",         prepareString(regFormDto.getPlace(), 20,80));
        context.putVar("autonomous",    regFormDto.isAutonomous() ? 1 : 2);
        context.putVar("lottery",       regFormDto.isLottery() ? 1 : 2);
        context.putVar("betting",       regFormDto.isBetting() ? 1 : 2);
        context.putVar("gambling",      regFormDto.isGambling() ? 1 : 2);
        context.putVar("bankAgent",     regFormDto.isBankAgent() ? 1 : 2);
        context.putVar("payAgent",      regFormDto.isPayAgent() ? 1 : 2);
        context.putVar("vending",       regFormDto.isVending() ? 1 : 2);
        context.putVar("marking",       regFormDto.isMarking() ? 1 : 2);
        context.putVar("internet",      regFormDto.isInternet() ? 1 : 2);
        context.putVar("onsite",        regFormDto.isOnSite() ? 1 : 2);
        context.putVar("bso",           regFormDto.isBso() ? 1 : 2);
        context.putVar("excise",        regFormDto.isExcise() ? 1 : 2);

        OfdProvider ofd = ofdService.get(regFormDto.getOfd());
        context.putVar("ofdName", prepareString(ofd.getName(), 20, 80));
        context.putVar("ofdInn", prepareString(ofd.getInn(), 12));

        context.putVar("reportUnavailable", regFormDto.isReportUnavailable() ? 1 : 2);
        context.putVar("regNum", prepareString(regFormDto.getRegNum(), 8));
        context.putVar("regDigest", prepareString(regFormDto.getRegDigest(), 10));
        context.putVar("dr", regFormDto.getRegDate().format(dateReportFormatter).toCharArray());
        context.putVar("tr", regFormDto.getRegTime().format(timeReportFormatter).toCharArray());

        context.putVar("closeNum", prepareString(regFormDto.getCloseNum(), 8));
        context.putVar("closeDigest", prepareString(regFormDto.getCloseDigest(), 10));
        context.putVar("dc", regFormDto.getCloseDate().format(dateReportFormatter).toCharArray());
        context.putVar("tc", regFormDto.getCloseTime().format(timeReportFormatter).toCharArray());

        try {
            templateFile = ResourceUtils.getFile("classpath:report/reg_form.xlsx");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            fos = new FileOutputStream(outputFile);
            fis = new FileInputStream(templateFile);

            JxlsHelper jxlsHelper = JxlsHelper.getInstance();
            Transformer transformer = jxlsHelper.createTransformer(fis, fos);

            //TODO Проверить работоспособноть параметров
//            ((PoiTransformer) transformer).setIgnoreColumnProps(false);
//            ((PoiTransformer) transformer).setIgnoreRowProps(false);

            XlsArea areaPage1 = new XlsArea(new AreaRef(PAGE_1 + EXCL + "A1:DO40"), transformer);

            //ИНН
            AreaRef arInn = new AreaRef(PAGE_1 + EXCL + "AN4:BU4");
            XlsArea areaInn = new XlsArea(arInn, transformer);
            EachCommand innCommand = new EachCommand("i", "inn", areaInn,
                    new StepCellRefGenerator(PAGE_1, 3, 13, 12));
            areaPage1.addCommand(arInn, innCommand);

            //Наименование организации
            AreaRef arOrgName = new AreaRef(PAGE_1 + EXCL + "A17:DN17");
            XlsArea areaOrgName = new XlsArea(arOrgName, transformer);
            EachCommand orgNameCommand = new EachCommand("letter", "orgName", areaOrgName,
                    new StepCellRefGenerator(PAGE_1, 16, 0, 40));
            areaPage1.addCommand(arOrgName, orgNameCommand);

            areaPage1.applyAt(new CellRef(PAGE_1 + EXCL + "A1"), context);

            //ФИО заявителя
            AreaRef arRootDeclarer = new AreaRef(PAGE_1 + EXCL + "A35:BI40");
            XlsArea areaRootDeclarer = new XlsArea(arRootDeclarer, transformer);
            AreaRef afDeclarer = new AreaRef(PAGE_1 + EXCL + "A36:BF40");
            XlsArea areaDeclarer = new XlsArea(afDeclarer, transformer);
            areaDeclarer.setCellShiftStrategy(new InnerCellShiftStrategy());
            EachCommand declarantCommand = new EachCommand("letter", "declarerName", areaDeclarer,
                    new StepCellRefGenerator(PAGE_1, 35, 0, 20));
            areaRootDeclarer.addCommand(afDeclarer, declarantCommand);
            areaRootDeclarer.applyAt(new CellRef(PAGE_1 + EXCL + "A35"), context);

            //// Стр. 3 Раздел 1
            //
            XlsArea areaPage3_1 = new XlsArea(PAGE_3 + EXCL + "BC13:DJ33", transformer);
            areaPage3_1.setCellShiftStrategy(new InnerCellShiftStrategy());

            //Модель ККТ
            AreaRef arModel = new AreaRef(PAGE_3 + EXCL + "BC13:DH15");
            XlsArea areaModel = new XlsArea(arModel, transformer);
            EachCommand cmdModel = new EachCommand("l", "modelName", areaModel,
                    new StepCellRefGenerator(PAGE_3, 12, 18, 20));
            areaModel.setCellShiftStrategy(new InnerCellShiftStrategy());
            areaPage3_1.addCommand(arModel, cmdModel);

            //Заводской номер
            AreaRef arSerialNum = new AreaRef(PAGE_3 + EXCL + "BC17:DH17");
            XlsArea areaSerialNum = new XlsArea(arSerialNum, transformer);
            EachCommand cmdSerialNum = new EachCommand("d", "serialNumber", areaSerialNum,
                    new StepCellRefGenerator(PAGE_3, 16, 18, 20));
            areaPage3_1.addCommand(arSerialNum, cmdSerialNum);

            //Модель ФН
            AreaRef arFnModel = new AreaRef(PAGE_3 + EXCL + "BC21:DH31");
            XlsArea areaFnModel = new XlsArea(arFnModel, transformer);
            EachCommand cmdFnModel = new EachCommand("l", "fnModel", areaFnModel,
                    new StepCellRefGenerator(PAGE_3, 20, 18, 20));
            areaPage3_1.addCommand(arFnModel, cmdFnModel);

            //Номер ФН
            AreaRef arFnNumber = new AreaRef(PAGE_3 + EXCL + "BC33:DH33");
            XlsArea areaFnNumber = new XlsArea(arFnNumber, transformer);
            EachCommand cmdFnNumber = new EachCommand("d", "fnNumber", areaFnNumber,
                    new StepCellRefGenerator(PAGE_3, 32, 18, 20));
            areaPage3_1.addCommand(arFnNumber, cmdFnNumber);

            areaPage3_1.applyAt(new CellRef(PAGE_3 + EXCL + "BC13"), context);

            //Индекс
            AreaRef arRootPostIndex = new AreaRef(PAGE_3 + EXCL + "AD37:AW39");
            XlsArea areaRootPostIndex = new XlsArea(arRootPostIndex, transformer);
            AreaRef arPostIndex = new AreaRef(PAGE_3 + EXCL + "AE38:AT38");
            XlsArea areaPostIndex = new XlsArea(arPostIndex, transformer);
            EachCommand cmdPostIndex = new EachCommand("d", "postIndex", areaPostIndex,
                    new StepCellRefGenerator(PAGE_3, 37, 10, 6));
            areaRootPostIndex.addCommand(arPostIndex, cmdPostIndex);
            areaRootPostIndex.applyAt(new CellRef(PAGE_3 + EXCL + "AD37"), context);

           //Район, Город, Населенный пункт
            AreaRef arRootCity  = new AreaRef(PAGE_3 + EXCL + "AC39:DQ45");
            XlsArea areaRootCity = new XlsArea(arRootCity, transformer);
            areaRootCity.setCellShiftStrategy(new InnerCellShiftStrategy());

            AreaRef arCounty = new AreaRef(PAGE_3 + EXCL + "AE40:DN40");
            XlsArea areaCounty = new XlsArea(arCounty, transformer);
            EachCommand cmdCounty = new EachCommand("l", "county", areaCounty,
                    new StepCellRefGenerator(PAGE_3, 39, 10, 30));
            areaRootCity.addCommand(arCounty, cmdCounty);

            AreaRef arCity = new AreaRef(PAGE_3 + EXCL + "AE42:DN42");
            XlsArea areaCity = new XlsArea(arCity, transformer);
            EachCommand cmdCity = new EachCommand("l", "city", areaCity,
                    new StepCellRefGenerator(PAGE_3, 41, 10, 30));
            areaRootCity.addCommand(arCity, cmdCity);

            AreaRef arSettlement = new AreaRef(PAGE_3 + EXCL + "AE44:DN44");
            XlsArea areaSettlement = new XlsArea(arSettlement, transformer);
            areaSettlement.setCellShiftStrategy(new InnerCellShiftStrategy());
            EachCommand cmdSettlement = new EachCommand("l", "settlement", areaSettlement,
                    new StepCellRefGenerator(PAGE_3, 43, 10, 30));
            areaRootCity.addCommand(arSettlement, cmdSettlement);

            areaRootCity.applyAt(new CellRef(PAGE_3 + EXCL + "AC39"), context );

            //// Стр. 4 Раздел 1
            //
            AreaRef arAddressStreet = new AreaRef(PAGE_4 + EXCL + "AD8:DQ16");
            XlsArea areaAddressStreet = new XlsArea(arAddressStreet, transformer);
            areaAddressStreet.setCellShiftStrategy(new InnerCellShiftStrategy());

            AreaRef arStreet = new AreaRef(PAGE_4 + EXCL + "AE9:DN9");
            XlsArea areaStreet = new XlsArea(arStreet, transformer);
            EachCommand cmdStreet = new EachCommand("l", "street", areaStreet,
                    new StepCellRefGenerator(PAGE_4, 8, 10, 30));
            areaAddressStreet.addCommand(arStreet, cmdStreet);

            AreaRef arNumber = new AreaRef(PAGE_4 + EXCL + "AE11:AZ11");
            XlsArea areaNumber = new XlsArea(arNumber, transformer);
            EachCommand cmdNumber = new EachCommand("d", "number", areaNumber,
                    new StepCellRefGenerator(PAGE_4, 10, 10, 8));
            areaAddressStreet.addCommand(arNumber, cmdNumber);

            AreaRef arBuilding = new AreaRef(PAGE_4 + EXCL + "AE13:AZ13");
            XlsArea areaBuilding = new XlsArea(arBuilding, transformer);
            EachCommand cmdBuilding = new EachCommand("d", "building", areaBuilding,
                    new StepCellRefGenerator(PAGE_4, 12, 10, 8));
            areaAddressStreet.addCommand(arBuilding, cmdBuilding);

            AreaRef arRoom = new AreaRef(PAGE_4 + EXCL + "AE15:AZ15");
            XlsArea areaRoom = new XlsArea(arRoom, transformer);
            EachCommand cmdRoom = new EachCommand("d", "room", areaRoom,
                    new StepCellRefGenerator(PAGE_4, 14, 10, 8));
            areaAddressStreet.addCommand(arRoom, cmdRoom);
            areaAddressStreet.applyAt(new CellRef(PAGE_4 + EXCL + "AD8"), context);

            AreaRef arRootPlace = new AreaRef(PAGE_4 + EXCL + "AZ18:DE27");
            XlsArea areaRootPlace = new XlsArea(arRootPlace, transformer);
            AreaRef arPlace = new AreaRef(PAGE_4 + EXCL + "AZ18:DE24");
            XlsArea areaPlace = new XlsArea(arPlace, transformer);
            EachCommand cmdPlace = new EachCommand("l", "place", areaPlace,
                    new StepCellRefGenerator(PAGE_4, 17, 17, 20));
            areaRootPlace.addCommand(arPlace, cmdPlace);
            areaRootPlace.applyAt(new CellRef(PAGE_4 + EXCL + "AZ18"), context);

            AreaRef arAttr1 = new AreaRef(PAGE_5 + EXCL + "BF11:BF42");
            XlsArea areaAttr1 = new XlsArea(arAttr1, transformer);
            areaAttr1.applyAt(new CellRef(PAGE_5 + EXCL + "BF11"), context);

            AreaRef arAttr2 = new AreaRef(PAGE_6 + EXCL + "BF10:BF24");
            XlsArea areaAttr2 = new XlsArea(arAttr2, transformer);
            areaAttr2.applyAt(new CellRef(PAGE_6 + EXCL + "BF10"), context);

            AreaRef arOfd = new AreaRef(PAGE_9 + EXCL + "BC12:DJ21");
            XlsArea areaOfd = new XlsArea(arOfd, transformer);

            AreaRef arOfdName = new AreaRef(PAGE_9 + EXCL + "BC12:DH18");
            XlsArea areaOfdName = new XlsArea(arOfdName, transformer);
            EachCommand ofdNameCommand = new EachCommand( "l", "ofdName", areaOfdName,
                    new StepCellRefGenerator(PAGE_9, 11, 17, 20));
            areaOfd.addCommand(arOfdName, ofdNameCommand);

            AreaRef arOfdInn = new AreaRef(PAGE_9 + EXCL + "BC21:CJ21");
            XlsArea areaOfdInn = new XlsArea(arOfdInn, transformer);
            EachCommand ofdInnCommand = new EachCommand("d", "ofdInn", areaOfdInn,
                    new StepCellRefGenerator(PAGE_9, 20, 17, 12));
            areaOfd.addCommand(arOfdInn, ofdInnCommand);
            areaOfd.applyAt(new CellRef(PAGE_9 + EXCL + "BC21"), context);

            //Стр.10_Разд.4
            AreaRef arReport = new AreaRef(PAGE_10 + EXCL + "BD11:CJ45");
            XlsArea areaReport = new XlsArea(arReport, transformer);
            AreaRef arRegNum = new AreaRef(PAGE_10 + EXCL + "BD18:BY18");
            XlsArea areaRegNum = new XlsArea(arRegNum, transformer);
            EachCommand regNumCommand = new EachCommand("d", "regNum", areaRegNum,
                    new StepCellRefGenerator(PAGE_10, 17, 18, 8, 1));
            areaReport.addCommand(arRegNum, regNumCommand);

            AreaRef arRegDigest = new AreaRef(PAGE_10 + EXCL + "BD29:CE29");
            XlsArea areaRegDigest = new XlsArea(arRegDigest, transformer);
            EachCommand regDigestCommand = new EachCommand("d", "regDigest", areaRegDigest,
                    new StepCellRefGenerator(PAGE_10, 28, 18, 10, 1));
            areaReport.addCommand(arRegDigest, regDigestCommand);

            AreaRef arCloseNum = new AreaRef(PAGE_10 + EXCL + "BD34:BY34");
            XlsArea areaCloseNum = new XlsArea(arCloseNum, transformer);
            EachCommand closeNumCommand = new EachCommand("d", "closeNum", areaCloseNum,
                    new StepCellRefGenerator(PAGE_10, 33, 18, 8, 1));
            areaReport.addCommand(arCloseNum, closeNumCommand);

            AreaRef arCloseDigest = new AreaRef(PAGE_10 + EXCL + "BD45:CE45");
            XlsArea areaCloseDigest = new XlsArea(arCloseDigest, transformer);
            EachCommand closeDigestCommand = new EachCommand("d", "closeDigest", areaCloseDigest,
                    new StepCellRefGenerator(PAGE_10, 44, 18, 10, 1));
            areaReport.addCommand(arCloseDigest, closeDigestCommand);
            areaReport.applyAt(new CellRef(PAGE_10 + EXCL + "BD11"), context);

            transformer.write();
            fos.flush();
            fos.close();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        return outputFile;
    }

    private List<Character> prepareString(String str, int length) {
        return prepareString(str, length, length);
    }

    private List<Character> prepareString(String str, int rowLength, int length) {
        if (str == null) {
            return Collections.nCopies(length, ' ');
        }

        List<String> words = List.of(str.split(" "));
        StringBuilder result = new StringBuilder();
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if (line.length() + word.length() > rowLength) {
                result.append(StringUtils.rightPad(line.toString(),  rowLength, ' '));
                line = new StringBuilder();
            }
            line.append(word);

            if (line.length() + 1 > rowLength) {
                result.append(line);
                line = new StringBuilder();
            } else {
                line.append(" ");
            }

            if (words.indexOf(word) == words.size() - 1 && result.length() < length) {
                result.append(line);
            }
        }
        return StringUtils.rightPad(result.toString(), length, " ")
                .toUpperCase()
                .chars()
                .mapToObj(e -> (char) e)
                .collect(Collectors.toList());
    }
}

