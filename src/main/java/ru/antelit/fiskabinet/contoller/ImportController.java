package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.api.bitrix.dto.CompanyDto;
import ru.antelit.fiskabinet.service.BitrixService;
import ru.antelit.fiskabinet.service.OrgService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Controller
public class ImportController {

    private final BitrixService bitrixService;

    private final Map<String, CompanyDto> companyCache = new HashMap<>();

    @Autowired
    public ImportController(BitrixService bitrixService, OrgService orgService) {
        this.bitrixService = bitrixService;
    }

    @GetMapping("import")
    public String show() {
        return "import";
    }

    @PostMapping("import")
    public ResponseEntity<?> importData(@Param("id") CompanyDto dto) {

    }

    @GetMapping("import/lookup")
    public String lookup(String query, Model model) {
        if (query == null || query.isBlank()) {
            model.addAttribute("companies", Collections.emptyList());
        } else {
            List<CompanyDto> companies = bitrixService.findCompaniesByName(query);
            companies.forEach(companyDto -> companyCache.putIfAbsent(companyDto.getId(), companyDto));
            bitrixService.setImportStatus(companies);
            model.addAttribute("companies", companies);
        }
        return "import::results";
    }

    @GetMapping("import/info")
    public String info(@RequestParam("bid") String bid, Model model) {
        var cmp = companyCache.get(bid);
        model.addAttribute("cmp",cmp);
        model.addAttribute("req", bitrixService.getRequisites(cmp));
        return "import::info";
    }
}