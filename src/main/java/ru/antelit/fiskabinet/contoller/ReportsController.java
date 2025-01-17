package ru.antelit.fiskabinet.contoller;

import io.minio.errors.MinioException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.antelit.fiskabinet.domain.KkmModel;
import ru.antelit.fiskabinet.report.RegFormDto;
import ru.antelit.fiskabinet.service.MinioService;
import ru.antelit.fiskabinet.service.ModelService;
import ru.antelit.fiskabinet.service.OfdService;
import ru.antelit.fiskabinet.service.ReportService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;

@Controller
public class ReportsController {

    public static final String FILE_NAME = "Заявление на регистрацию.xlsx";

    @Value("${deploy.host}")
    private String host;

    private final ReportService reportService;
    private final ModelService modelService;
    private final OfdService ofdService;
    private final MinioService minioService;

    public ReportsController(ReportService reportService, ModelService modelService, OfdService ofdService,
                             MinioService minioService) {
        this.reportService = reportService;
        this.modelService = modelService;
        this.ofdService = ofdService;
        this.minioService = minioService;
    }

    @GetMapping("/application")
    public String index(Model model) {
        var ofdList = ofdService.list();
        var models = modelService.list().stream()
                .map(KkmModel::getFullName)
                .sorted()
                .collect(Collectors.toList());
        model.addAttribute("ofdList", ofdList);
        model.addAttribute("models", models);
        return "application";
    }

    @PostMapping("/application")
    public String create(RegFormDto regFormDto, Model model) throws MinioException, IOException,
            NoSuchAlgorithmException, InvalidKeyException {
        File report = reportService.createRegistrationApplication(regFormDto);
        String filename = minioService.upload(report);
        model.addAttribute("download_url", host + "files/" + filename);
        return "application::report";
    }

    @GetMapping(value = "files/**", produces = "application/octet-stream")
    public ResponseEntity<?> get(HttpServletRequest request) throws Exception {
        String filename = new AntPathMatcher().extractPathWithinPattern("files/**", request.getServletPath());
        InputStream stream = minioService.getFileStream(filename);
        String name;
        if (stream == null) {
            return ResponseEntity.notFound().build();
        }
        name = URLEncoder.encode(FILE_NAME, StandardCharsets.UTF_8)
                .replace("+", " ");
        byte[] fileBytes = IOUtils.toByteArray(minioService.getFileStream(filename));
        stream.close();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("filename=%s", name))
                .body(fileBytes);
    }
}
