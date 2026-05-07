package web_prak.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommonErrorController implements ErrorController {


    @RequestMapping("/error")

    public String handleError(@RequestParam(name = "msg", required = false) String msg, HttpServletRequest request, Model model) {
       Integer costMin;
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object error = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if(msg != null && msg != "")
        {
            model.addAttribute("error", "Ошибка");
            model.addAttribute("message", msg);
        }
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("status", statusCode);
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", "Страница не найдена");
                model.addAttribute("message", "Запрашиваемая страница не существует");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "Внутренняя ошибка сервера");
                model.addAttribute("message", "Пожалуйста, попробуйте позже");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", "Доступ запрещён");
                model.addAttribute("message", "У вас недостаточно прав");
            } else {
                model.addAttribute("error", "Ошибка");
                model.addAttribute("message", message != null ? message : "Неизвестная ошибка");
            }
        }

        return "error";
    }
}