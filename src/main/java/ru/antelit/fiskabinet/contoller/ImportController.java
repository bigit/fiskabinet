package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.antelit.fiskabinet.api.bitrix.dto.CompanyDto;
import ru.antelit.fiskabinet.service.BitrixService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Controller
public class ImportController {

    @Autowired
    private BitrixService bitrixService;

    private volatile Map<Integer, CompanyDto> companyCache = new HashMap<>();

    @GetMapping("import")
    public String show() {
        return "import";
    }

    @PostMapping("import")
    public String importData(Model model) {
        String report = bitrixService.importOrganizationsData();
        model.addAttribute("report", report);
        return "import::report";
    }

    @GetMapping("import/lookup")
    public String lookup(String query, Model model) {
        List<CompanyDto> companies = bitrixService.findCompaniesByName(query);
        companies.forEach(companyDto -> companyCache.putIfAbsent(companyDto.getId(), companyDto));

        Map<Integer, String> results = companies.stream()
                .collect(toMap(CompanyDto::getId, CompanyDto::getTitle));
        model.addAttribute("results", results);
        return "import::search-results";
    }

    @GetMapping("import/info")
    public String info(Integer query, Model model) {
        model.addAttribute("org", companyCache.get(query));
        return "import::info";
    }
}