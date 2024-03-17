package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.domain.ActivationCode;
import ru.antelit.fiskabinet.domain.dto.ActivationCodeDto;
import ru.antelit.fiskabinet.domain.dto.DtoConverter;
import ru.antelit.fiskabinet.service.ActivationCodeService;
import ru.antelit.fiskabinet.service.OfdService;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ActivationCodeController {

    private final OfdService ofdService;
    private final ActivationCodeService codeService;
    private final DtoConverter converter;

    @Autowired
    public ActivationCodeController(OfdService ofdService, ActivationCodeService codeService, DtoConverter converter) {
        this.ofdService = ofdService;
        this.codeService = codeService;
        this.converter = converter;
    }

    @GetMapping("/code")
    public String index(Model model) {
        model.addAttribute("ofdList", ofdService.list());
        return "code";
    }

    @PostMapping("/code/add")
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

    @GetMapping("/code/free")
    public String freeCodes(@RequestParam("providerId") Integer ofdId, Model model) {
        model.addAttribute("codes", codeService.getFreeCodesByProvider(ofdId));
        return "code::codes";
    }
}
