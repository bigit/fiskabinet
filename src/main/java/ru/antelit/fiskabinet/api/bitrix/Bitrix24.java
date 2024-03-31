package ru.antelit.fiskabinet.api.bitrix;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import ru.antelit.fiskabinet.api.bitrix.enums.Method;
import ru.antelit.fiskabinet.api.bitrix.enums.Scope;
import ru.antelit.fiskabinet.api.bitrix.json.ObjectMapperProvider;
import ru.antelit.fiskabinet.api.bitrix.model.BitrixRequest;
import ru.antelit.fiskabinet.api.bitrix.model.CompanyDto;
import ru.antelit.fiskabinet.api.bitrix.model.ListResponse;
import ru.antelit.fiskabinet.api.bitrix.model.RequisiteDto;
import ru.antelit.fiskabinet.api.bitrix.model.TaskDto;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import static ru.antelit.fiskabinet.api.bitrix.enums.Entity.COMPANY;
import static ru.antelit.fiskabinet.api.bitrix.enums.Entity.REQUISITE;
import static ru.antelit.fiskabinet.api.bitrix.enums.Entity.TASK;
import static ru.antelit.fiskabinet.api.bitrix.enums.Method.LIST;
import static ru.antelit.fiskabinet.api.bitrix.enums.Scope.CRM;
import static ru.antelit.fiskabinet.api.bitrix.model.BitrixConstants.COMPANY_URL;
import static ru.antelit.fiskabinet.api.bitrix.model.BitrixConstants.ENTITY_TYPE_COMPANY;
import static ru.antelit.fiskabinet.api.bitrix.model.BitrixConstants.ENTITY_TYPE_ID;
import static ru.antelit.fiskabinet.api.bitrix.model.BitrixConstants.HOST;
import static ru.antelit.fiskabinet.api.bitrix.model.BitrixConstants.HTTPS;
import static ru.antelit.fiskabinet.api.bitrix.model.BitrixConstants.REST;

/**
 * Клиент для Bitrix24 REST API
 */
@Builder
@Getter
@Slf4j
@AllArgsConstructor
public class Bitrix24 {

    public static final String LOGIN_PASSWORD = "UF_CRM_1506422595";
    public static final String SERIAL_NUMBERS = "UF_CRM_1524819776";
    public static final String CABINETS = "UF_CRM_1565683449215";
    public static final String MAINTAIN_ADDRESS = "UF_CRM_1596523038359";
    public static final String MAINTAIN_ADDRESS_2 = "UF_CRM_1597121179";
    public static final String CABINET_URL = "UF_CRM_1687185821074";

    public static final String OFD = "UF_CRM_1506421566";
    public static final String OFD2 = "UF_CRM_1658205075471";
    public static final String OFD3 = "UF_CRM_1664526798211";
    public static final String KKT = "UF_CRM_1657547855021";

    @Getter(AccessLevel.PRIVATE)
    private String path;

    @NonNull
    @Builder.ObtainVia(method = "forDomain")
    private final String domain;

    @Builder.ObtainVia(method = "inCountry")
    private final String country;

    @NonNull
    private final String userId;

    @NonNull
    @Getter(AccessLevel.PRIVATE)
    private final String apiKey;

    @Builder.Default
    @Builder.ObtainVia(method = "client")
    private JerseyClient client = buildDefaultClient();

    private static JerseyClient buildDefaultClient() {
        ClientBuilder builder = JerseyClientBuilder.newBuilder();

        ClientConfig config = new ClientConfig();
        config.register(ObjectMapperProvider.class);
        builder.withConfig(config);

        builder.scheduledExecutorService(Executors.newSingleThreadScheduledExecutor());
        return (JerseyClient) builder.build();
    }

    public Bitrix24(String path, @NonNull String userId, @NonNull String apiKey, String country) {
        this.apiKey = apiKey;
        this.domain = "";
        this.path = path;
        this.userId = userId;
        this.country = country;
    }

    public TaskDto createTask(TaskDto task) throws ExecutionException, InterruptedException {
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
        return response.readEntity(TaskDto.class);
    }

    public List<CompanyDto> findCompanyByName(String query) {
        HashMap<String, String> filter = new HashMap<>();
        filter.put("%TITLE", query);
        return getOrganizations(filter, null);
    }

    public List<RequisiteDto> findRequisitesByCompanyName(String query) {
        HashMap<String, String> filter = new HashMap<>();
        filter.put("%RQ_COMPANY_FULL_NAME", query);
        return getRequisites(filter, null);
    }

    @SuppressWarnings("unchecked")
    private List<CompanyDto> getOrganizations(Map<String, String> filters, List<String> select) {
        BitrixRequest request = BitrixRequest.builder()
                .scope(CRM)
                .entity(COMPANY)
                .method(LIST)
                .filter(filters)
                .select(select)
                .build();
        return getList(request, response ->
                (ListResponse<CompanyDto>) response.readEntity(GenericType.forInstance(
                        new GenericEntity<ListResponse<CompanyDto>>(new ListResponse<>()) {
                        })));
    }

    @SuppressWarnings("unchecked")
    public List<RequisiteDto> getRequisites(Map<String, String> filters, List<String> select) {
        if (filters == null) {
            filters = new HashMap<>();
        }
        filters.put(ENTITY_TYPE_ID, ENTITY_TYPE_COMPANY);
        BitrixRequest request = BitrixRequest.builder()
                .scope(CRM)
                .entity(REQUISITE)
                .method(LIST)
                .filter(filters)
                .select(select)
                .build();
        return getList(request, response ->
                (ListResponse<RequisiteDto>) response.readEntity(GenericType.forInstance(
                        new GenericEntity<ListResponse<RequisiteDto>>(new ListResponse<>()) {
                        })));
    }

    private <T> List<T> getList(BitrixRequest request, Function<Response, ListResponse<T>> deserializer) {
        List<T> list = new ArrayList<>();
        ListResponse<T> listResponse;
        do {
            URI uri = buildUri(request);
            Invocation invocation = client.invocation(Link.fromUri(uri).build()).buildPost(Entity.json(request));
            try {
                var response = invocation.submit().get();
                listResponse = deserializer.apply(response);
                list.addAll(listResponse.getResult());
                request.setStart(listResponse.getNext());
            } catch (Exception e) {
                log.error("Unable retrieve data. {}", e.getMessage());
                return list;
            }
        } while (listResponse.getNext() != null);
        return list;
    }

    private URI buildUri(BitrixRequest request) {
        UriBuilder builder = new JerseyUriBuilder();
        if (path == null) {
            builder.scheme(HTTPS)
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

    public String getCompanyUrl(String id) {
        return "https://" + domain + HOST + country + String.format(COMPANY_URL, id);
    }

}

