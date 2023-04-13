package ru.antelit.fiskabinet.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.antelit.fiskabinet.domain.Kkm;
import ru.antelit.fiskabinet.domain.KkmModel;
import ru.antelit.fiskabinet.domain.Vendor;
import ru.antelit.fiskabinet.service.KkmService;
import ru.antelit.fiskabinet.service.VendorService;
import ru.antelit.fiskabinet.service.ModelService;

import java.util.List;

@Controller
public class KkmController {

    @Autowired
    private KkmService kkmService;
    @Autowired
    private VendorService vendorService;
    @Autowired
    private ModelService modelService;

    @GetMapping("/kkm/{id}")
    public String index(@PathVariable(value = "id", required = false) Integer id, Model model) {
        if (id == null) {
            model.addAttribute("kkm", new Kkm());
        } else {
            Kkm kkm = kkmService.get(id);
            model.addAttribute("kkm", kkm);
        }

        List<Vendor> vendors = vendorService.list();
        model.addAttribute("vendors", vendors);

        return "kkm";
    }

    @PostMapping("/kkm/save")
    public String save(Kkm kkm) {
        Kkm saved = kkmService.save(kkm);
        return "redirect:/kkm/" + saved.getId();
    }

    @GetMapping("/models")
    public String getModels(@RequestParam("id") Integer vendorId, Model model) {
        List<KkmModel> models =  modelService.getModelsByVendorId(vendorId);
        model.addAttribute("models", models);
        return "kkm::#model_list";
    }
}
