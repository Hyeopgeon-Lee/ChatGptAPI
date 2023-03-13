package kopo.poly.controller;

import kopo.poly.dto.ChatGptDTO;
import kopo.poly.service.IChatGptService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/gpt")
@RequiredArgsConstructor
@Controller
public class ChatGptController {

    private final IChatGptService chatGptService;

    /**
     * 채팅방 전체 리스트가져오기
     */
    @PostMapping(value = "gptResult")
    @ResponseBody
    public ChatGptDTO gptResult(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".gptResult Start!");

        String prompt = CmmUtil.nvl(request.getParameter("prompt"));

        log.info("prompt : " + prompt);

        ChatGptDTO pDTO = new ChatGptDTO();
        pDTO.setPrompt(prompt);

        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        ChatGptDTO rDTO = Optional.ofNullable(chatGptService.getChatGptResult(pDTO))
                .orElseGet(ChatGptDTO::new);

        log.info(this.getClass().getName() + ".gptResult End!");

        return rDTO;
    }


}
