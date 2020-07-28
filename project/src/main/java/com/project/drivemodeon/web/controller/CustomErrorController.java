package com.project.drivemodeon.web.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public ModelAndView handleError(HttpServletResponse response) {
        int errStatus = response.getStatus();

        if(errStatus == 403 || errStatus == 200){
            //Redirect when page is forbidden or ok
            return new ModelAndView("redirect:/feed");
        }

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errCode", errStatus);
        modelAndView.addObject("error", getErrorText(errStatus));
        return modelAndView;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private String getErrorText(int status) {
        String text = "";
        switch (status) {
            case 500:
                text = "It seems that we have a problem!";
                break;
            case 404:
                text = "Ooops... We did not find your search!";
                break;
            default:
                text = "There is a error!";
                break;
        }
        return text;
    }
}
