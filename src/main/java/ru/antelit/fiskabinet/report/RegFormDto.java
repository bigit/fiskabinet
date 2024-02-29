package ru.antelit.fiskabinet.report;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Данные для формирования отчета о (пере)регистрации в налоговую
 * в формате xlsx
 *
 * @author R.Miller
 */
@Data
public class RegFormDto {
    private String ogrn; //TODO

    /* Наименование организации */
    private String name;

    /**
     * ИНН организации
     */
    public String inn;

    /**
     * Признак вида заявления
     * true - регистрация,
     * false - перерегистрация
     */
    private Boolean isRegistration;

    /**
     * Причина перерегистрации
     * 1 - смена адреса или места применения ккт
     * 2 - смена ОФД
     * 3 - изменение сведений о применении ккт в составе
     *      автоматического устройства
     * 4 - замена фискального накопителя
     * 5 - переход ккт из автономного режима в режим передачи данных в ОФД
     * 6 - переход ккт в автономный режим
     * 7 - изменение наименования пользоваеля
     * 8 - иные причины
     */

    private boolean changeAddressOrPlace;
    private boolean changeOfd;
    private boolean changeAutomatic;
    private boolean changeFn;
    private boolean changeOffAutonomous;
    private boolean changeOnAutonomous;
    private boolean changeUserName;
    private boolean changeOther;

    /**
     * ФИО заявителя
     */
    private String declarer;

    /**
     * Является ли заявитель пользователем
     *  true - пользователь
     *  false - представитель
     */
    private Boolean isUser = false;
    /**
     * Полное наименование модели. Например: Атол 30Ф
     */
    private String model;

    /**
     * Заводской номер ККТ
     */
    private String serialNumber;

    /**
     * Модель ФН
     */
    private String fnModel;

    /**
     * Заводской номер ФН
     */
    private String fnNumber;

    /**
     * Почтовый индекс
     */
    private String postIndex;

    /**
     * Район
     */
    private String county;

    /**
     * Город
     */
    private String city;

    /**
     * Населенный пункт
     */
    private String settlement;

    private String street;

    private String number;

    private String building;

    private String room;

    private String place;

    private boolean autonomous;
    private boolean lottery;
    private boolean betting;
    private boolean gambling;
    private boolean bankAgent;
    private boolean payAgent;
    private boolean vending;
    private boolean marking;
    private boolean internet;
    private boolean onSite;
    private boolean bso;
    private boolean excise;

    private Integer ofd;

    private boolean reportUnavailable;

    private String regNum;

    private String regDigest;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate regDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime regTime;

    private String closeNum;

    private String closeDigest;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closeTime;

}
