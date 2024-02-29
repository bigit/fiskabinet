package ru.antelit.fiskabinet.contoller;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.antelit.fiskabinet.service.VendorService;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private ReportService reportService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private OfdService ofdService;
    @Autowired
    private MinioService minioService;

    @GetMapping("/application")
    public String index(Model model)
    {
//        Map<Integer, String> vendors = vendorService.getVendorsNames();
//        model.addAttribute("vendors", vendors);
        var ofdList = ofdService.list();
        model.addAttribute("ofdList", ofdList);
        var models = modelService.list().stream()
                .map(KkmModel::getFullName)
                .sorted()
                .collect(Collectors.toList());
        model.addAttribute("models", models);
        return "application";
    }

    @PostMapping("/application")
    public String create(RegFormDto regFormDto, Model model) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        File report =
                reportService.createRegistrationApplication(regFormDto);
        String filename = minioService.upload(report);
        model.addAttribute("download_url", host + "files/" + filename);
        return "application::report";
    }

    @GetMapping(value = "files/**", produces = "application/octet-stream")
    public ResponseEntity<?> get(HttpServletRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String filename = new AntPathMatcher().extractPathWithinPattern("files/**", request.getServletPath());
        InputStream stream = minioService.getFileStream(filename);
        if (stream == null) {
            return ResponseEntity.notFound().build();
        }
        String name = URLEncoder.encode(FILE_NAME, StandardCharsets.UTF_8)
                .replace("+", " ");
        byte[] fileBytes = IOUtils.toByteArray(minioService.getFileStream(filename));
        stream.close();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("filename=%s", name))
                .body(fileBytes);
    }
}
