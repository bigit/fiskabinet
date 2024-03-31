package ru.antelit.fiskabinet.api.bitrix.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BitrixConstants {

    public static final String ENTITY_TYPE_ID = "ENTITY_TYPE_ID";
    public static final String ENTITY_TYPE_COMPANY = "4";
    public static final String COMPANY_URL = "/crm/company/details/%s/";
    public static final String HOST = ".bitrix24.";
    public static final String REST = "rest";
    public static final String HTTPS = "https";
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String ASSIGNED_BY_ID = "ASSIGNED_BY_ID";
}
