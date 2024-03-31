package ru.antelit.fiskabinet.api.bitrix.enums;

public enum Scope {
    //	Бизнес-процессы
    BIZPROC("bizproc"),
    //  Календарь
    CALENDAR("calendar"),
    //Телефония (совершение звонков). В скоуп входят методы: voximplant.infocall.startwithsound voximplant.infocall.startwithtext
    CALL("call"),
    //Торговый каталог
    CATALOG("catalog"),
    //Контакт-центр
    CONTACT_CENTER("contact_center"),
    //CRM
    CRM("crm"),
    //Структура компании
    DEPARTMENT("department"),
    //Диск
    DISK("disk"),
    //Генератор документов
    DOCUMENT_GENERATOR("documentgenerator"),
    //Хранилище данных
    ENTITY("entity"),
    //Чат и уведомления
    IM("im"),
    //Создание и управление Чат-ботами
    IM_BOT("imbot"),
    //Открытые линии
    IM_OPEN_LINES("imopenlines"),
    //Интранет
    INTRANET("intranet"),
    //Сайты
    LANDING("landing"),
    //Списки
    LISTS("lists"),
    //Живая лента
    LOG("log"),
    //Почтовые сервисы
    MAIL_SERVICE("mailservice"),
    //Служба сообщений
    MESSAGE_SERVICE("messageservice"),
    //Мобильное приложение
    MOBILE("mobile"),
    //Платёжные системы
    PAY_SYSTEM("pay_system"),
    //Встраивание приложений
    PLACEMENT("placement"),
    //Pull&Push
    PULL("pull"),
    //Роботизация бизнеса
    RPA("rpa"),
    //Интернет-магазин
    SALE("sale"),
    //Рабочие группы
    SONET_GROUP("sonet_group"),
    //Задачи
    TASKS("tasks"),
    //Телефония
    TELEPHONY("telephony"),
    //Учет рабочего времени
    TIMEMAN("timeman"),
    //Пользователи. Версии:
    USER("user"),
    //- Пользователи (минимальный)
    USER_BRIEF("user_brief"),
    //- Пользователи (базовый)
    USER_BASIC("user_basic"),
    //Пользовательские поля пользователя
    USER_FIELDS("user.userfield"),
    //Работа с соглашениями
    USERCONSENT("userconsent"),
    //Доставки
    DELIVERY("delivery"),
    //Рейтинги
    RATING("rating"),
    //Смайлы
    SMILE("smile"),
    //Информационные блоки
    IBLOCK("iblock"),
    //Импорт отраслевых решений
    CONFIG_IMPORT("configuration.import"),
    //Продажи в чате
    SALESCENTER("salescenter"),
    //Кассы
    CASHBOX("cashbox"),
    //Форум
    FORUM("forum"),
    //Работа с репозитарием
    LANDING_CLOUD("landing_cloud"),
    //Служебный канал для мгновенных сообщений системы (подписка на информацию об обновлении всех элементов системы доступных пользователю)
    PULL_CHANNEL("pull_channel");

    private final String value;

    Scope(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
