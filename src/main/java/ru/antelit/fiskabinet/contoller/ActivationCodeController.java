package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.ofd.ActivationCode;
import ru.antelit.fiskabinet.ofd.CodeStatus;
import ru.antelit.fiskabinet.domain.UserInfo;
import ru.antelit.fiskabinet.domain.dto.ActivationCodeDto;
import ru.antelit.fiskabinet.domain.dto.DtoConverter;
import ru.antelit.fiskabinet.service.ActivationCodeService;
import ru.antelit.fiskabinet.service.OfdService;
import ru.antelit.fiskabinet.service.OrgService;
import ru.antelit.fiskabinet.utils.SecurityUtils;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
public class ActivationCodeController {

    private final OfdService ofdService;
    private final ActivationCodeService codeService;
    private final OrgService orgService;
    private final DtoConverter converter;
    private final SecurityUtils securityUtils;

    @Autowired
    public ActivationCodeController(OfdService ofdService, ActivationCodeService codeService, OrgService orgService,
                                    DtoConverter converter, SecurityUtils securityUtils) {
        this.ofdService = ofdService;
        this.codeService = codeService;
        this.orgService = orgService;
        this.converter = converter;
        this.securityUtils = securityUtils;
    }

    @GetMapping("code")
    public String index(Model model) {
        model.addAttribute("ofdList", ofdService.list());
        return "code";
    }

    @GetMapping("code/list")
    public String list(@RequestParam("providerId") Integer ofdId, Model model) {
        model.addAttribute("codes", codeService.getCodesByProvider(ofdId)
                .stream()
                .sorted(Comparator.comparing(ActivationCode::getId))
                .collect(Collectors.toList()));
        return "code::codes";
    }

    @GetMapping("code/modal")
    public String showModal(@RequestParam("codeId") Long codeId, Model model, HttpServletResponse response) {
        var code = codeService.getById(codeId);
        model.addAttribute("code", code);
        if (code.getStatus() == CodeStatus.NEW) {
            model.addAttribute("orgs", orgService.list());
        }
        return "include :: use_code_modal";

    }
    @PostMapping("code/add")
    public String addCode(ActivationCodeDto dto, Model model, HttpServletResponse response) {
        ActivationCode code = converter.convert(dto);
        ActivationCode existCode = codeService.getByValue(code.getValue());
        if (existCode != null) {
            model.addAttribute("class", "text-danger");
            model.addAttribute("result", "Код уже существует");
        } else {
            codeService.addActivationCode(code);
            model.addAttribute("class", "text-success");
            model.addAttribute("result", "Успешно");
            response.setHeader("HX-Trigger", "update");
        }
        return "code::result";
    }

    @PostMapping("code/apply")
    public ResponseEntity<?> applyCode(@RequestParam("code_id") Long id, @RequestParam("org_id") Integer orgId,
                                       HttpServletResponse response) {
        var code = codeService.getById(id);
        UserInfo user;

        if (code == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Указанный код не найден в системе");
        }
        if (code.getStatus() == CodeStatus.ERROR || code.getStatus() == CodeStatus.USED) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Указанный код не может быть использован");
        }
        user = securityUtils.getCurrentUser();
        code.setUserInfo(user);
        code.setStatus(CodeStatus.USED);
        code.setOrganization(orgService.get(orgId));
        codeService.save(code);
        response.setHeader("HX-Trigger", "update");
        return ResponseEntity.ok().build();
    }

    @PostMapping("code/reserve")
    public ResponseEntity<?> reserveCode(@RequestParam("code_id") String codeId,
                                         @RequestParam(value = "org_id", required = false) Integer orgId,
                                         HttpServletResponse response) {
        var code = codeService.getById(Long.parseLong(codeId));
        if (code == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Указанный код не найден в системе");
        }
        if (code.getStatus() != CodeStatus.NEW) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Указанный код не может быть зарезервирован");
        }
        if (orgId != null) {
            var org = orgService.get(orgId);
            code.setOrganization(org);
        }
        UserInfo user = securityUtils.getCurrentUser();
        code.setUserInfo(user);
        code.setStatus(CodeStatus.RESERVED);
        codeService.save(code);
        response.setHeader("HX-Trigger", "update");
        return ResponseEntity.ok().build();
    }

    @PostMapping("code/release")
    public ResponseEntity<?> releaseCode(@RequestParam("code_id") String codeId, HttpServletResponse response) {
        var code = codeService.getById(Long.parseLong(codeId));
        if (code == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Указанный код не найден");
        }
        if (code.getStatus() != CodeStatus.RESERVED) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Указанный код не зарезервирован");
        }
        code.setStatus(CodeStatus.NEW);
        code.setOrganization(null);
        code.setUserInfo(null);
        codeService.save(code);
        response.setHeader("HX-Trigger", "update");
        return ResponseEntity.ok().build();
    }

}
