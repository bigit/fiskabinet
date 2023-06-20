package ru.antelit.fiskabinet.api.bitrix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import ru.antelit.fiskabinet.api.bitrix.dto.CompanyDto;
import ru.antelit.fiskabinet.api.bitrix.dto.RequisiteDto;
import ru.antelit.fiskabinet.api.bitrix.enums.Method;
import ru.antelit.fiskabinet.api.bitrix.enums.Scope;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static ru.antelit.fiskabinet.api.bitrix.enums.Entity.COMPANY;
import static ru.antelit.fiskabinet.api.bitrix.enums.Entity.REQUISITE;
import static ru.antelit.fiskabinet.api.bitrix.enums.Entity.TASK;

/**
 * Клиент для Bitrix24 REST API
 */
@Builder
@Getter
@AllArgsConstructor
public class Bitrix24 {

    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String ASSIGNED_BY_ID = "ASSIGNED_BY_ID";
    public static final String LOGIN_PASSWORD = "UF_CRM_1506422595";
    public static final String SERIAL_NUMBERS = "UF_CRM_1524819776";
    public static final String KABINETS = "UF_CRM_1565683449215";
    public static final String MAINTAIN_ADDRESS = "UF_CRM_1596523038359";
    public static final String MAINTAIN_ADDRESS_2 = "UF_CRM_1597121179";
    public static final String KABINET_URL = "UF_CRM_1687185821074";

    public static final String OFD = "UF_CRM_1506421566";
    public static final String OFD2 = "UF_CRM_1658205075471";
    public static final String OFD3 = "UF_CRM_1664526798211";
    public static final String KKT = "UF_CRM_1657547855021";
    public static final String COMPANY_URL = "/crm/company/details/%d/";
    public static final String HOST = ".bitrix24.";
    public static final String REST = "rest";


    private String path;

    @NonNull
    @Builder.ObtainVia(method = "forDomain")
    private final String domain;

    @Builder.Default
    @Builder.ObtainVia(method = "inCountry")
    private String country = "ru";

    @NonNull
    private final String userId;

    @NonNull
    private String apiKey;

    @Autowired
    private JerseyClient client;

    public Bitrix24(String path, @NonNull String userId) {
        this.domain = "";
        this.path = path;
        this.userId = userId;
    }

    public Integer createTask(TaskDto task) throws ExecutionException, InterruptedException, TimeoutException {
        BitrixRequest request = BitrixRequest.builder()
                .scope(Scope.TASKS)
                .entity(TASK)
                .method(Method.ADD)
                .build();

        Entity<TaskDto> entity = Entity.json(task);

        URI uri = buildUri(request);

        Invocation invocation = client
                .invocation(Link.fromUri(uri).build())
                .buildPost(entity);

        Future<Response> future = invocation.submit();
        Response response = future.get();
        return 0;
    }

    public List<CompanyDto> getOrganizations(Map<String, String> filters, List<String> select) {
        BitrixRequest request = BitrixRequest.builder()
                .scope(Scope.CRM)
                .entity(COMPANY)
                .method(Method.LIST)
                .filter(filters)
                .select(select)
                .build();
        return getList(request);
    }

    public List<RequisiteDto> getRequisites(Map<String, List<String>> filters, List<String> select) {
        if (filters == null) {
            filters = new HashMap<>();
        }
        filters.put("RQ_ENTITY_ID", Collections.singletonList("4"));
        BitrixRequest request = BitrixRequest.builder()
                .scope(Scope.CRM)
                .entity(REQUISITE)
                .method(Method.LIST)
                .filter(filters)
                .select(select)
                .build();
        return getList(request);
    }

    public <T> List<T> getList(BitrixRequest request) {
        List<T> list = new ArrayList<>();
        ListResponse<T> listResponse;
        do {
            URI uri = buildUri(request);
            Invocation invocation = client.invocation(Link.fromUri(uri).build()).buildPost(Entity.json(request));
            try {
                var response = invocation.submit().get();
                listResponse = response.readEntity(new GenericType<>() {});
                list.addAll(listResponse.getResult());
                request.setStart(listResponse.getNext());
            } catch (Exception e) {
                e.printStackTrace();
                return list;
            }
        } while (listResponse.getNext() != null);
        System.out.println("Count" + list.size());
        return list;
    }


    private URI buildUri(BitrixRequest request) {
        UriBuilder builder = new JerseyUriBuilder();

        if (path == null) {
            builder.scheme("https")
                    .host(domain + HOST + country)
                    .path(REST)
                    .path(userId)
                    .path(apiKey);
            path = builder.toTemplate();
        } else {
            builder.path(path);
        }

        builder.path(request.buildRequest());

        return builder.build();
    }

    public String getCompanyUrl(int id) {
        return "https://" + domain + HOST + country + String.format(COMPANY_URL, id);
    }
}

