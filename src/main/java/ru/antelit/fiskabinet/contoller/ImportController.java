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
import ru.antelit.fiskabinet.api.bitrix.dto.RequisiteDto;
import ru.antelit.fiskabinet.domain.Organization;
import ru.antelit.fiskabinet.service.BitrixService;
import ru.antelit.fiskabinet.service.OrgService;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ImportController {

    private final BitrixService bitrixService;
    private final OrgService orgService;

    private final Map<String, CompanyDto> companyCache = new HashMap<>();
    private RequisiteDto selectedRequisite;

    @Autowired
    public ImportController(BitrixService bitrixService, OrgService orgService) {
        this.bitrixService = bitrixService;
        this.orgService = orgService;
    }

    @GetMapping("import")
    public String show() {
        return "import";
    }

    @PostMapping("import/save")
    public ResponseEntity<?> importData(@Param("id") String id, HttpServletResponse response) {
        var company = companyCache.get(id);
        var org = new Organization();
        org.setName(company.getTitle());
        org.setInn(selectedRequisite.getInn());
        org.setSourceId(company.getId());
        var savedId = orgService.save(org);
        response.setHeader("HX-Trigger", "saved");
        return ResponseEntity.ok(savedId);
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
        selectedRequisite = bitrixService.getRequisites(cmp);
        model.addAttribute("req", selectedRequisite);
        return "import::info";
    }
}