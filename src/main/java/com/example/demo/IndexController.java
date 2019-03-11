package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Value("${vcap.application.name:localMachine}")
    private String applicationName;

    @Value("${vcap.application.space_name:localSpace}")
    private String spaceName;

    @Value("${vcap.application.instance_id:localInstanceId}")
    private String instanceId;

    @Value("${vcap.application.instance_index:localInstanceIndex}")
    private String instanceIndex;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Hello world !!!");
        model.addAttribute("applicationName", applicationName);
        model.addAttribute("spaceName", spaceName);
        model.addAttribute("instanceId", instanceId);
        model.addAttribute("instanceIndex", instanceIndex);

        return "index";
    }

}


