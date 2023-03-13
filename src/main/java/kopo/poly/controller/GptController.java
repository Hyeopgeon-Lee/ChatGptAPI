package kopo.poly.controller;

import kopo.poly.dto.Gpt35DTO;
import kopo.poly.dto.Gpt3DTO;
import kopo.poly.dto.MessageDTO;
import kopo.poly.service.IGptService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/gpt")
@RequiredArgsConstructor
@Controller
public class GptController {

    private final IGptService gptService;

    /**
     * GPT-3 모델 호출하여 결과받기
     */
    @PostMapping(value = "gpt3")
    @ResponseBody
    public Gpt3DTO gpt3(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".gpt3 Start!");

        String prompt = CmmUtil.nvl(request.getParameter("prompt"));

        log.info("prompt : " + prompt);

        Gpt3DTO pDTO = new Gpt3DTO();
        pDTO.setPrompt(prompt);

        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        Gpt3DTO rDTO = Optional.ofNullable(gptService.getGpt3(pDTO)).orElseGet(Gpt3DTO::new);

        log.info(this.getClass().getName() + ".gpt3 End!");

        return rDTO;
    }

    /**
     * GPT-3.5 모델 호출하여 결과받기
     */
    @PostMapping(value = "gpt35")
    @ResponseBody
    public Gpt35DTO gpt35(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".gpt35 Start!");

        String content = CmmUtil.nvl(request.getParameter("content"));

        log.info("content : " + content);

        MessageDTO pDTO = new MessageDTO();
        pDTO.setRole("user"); // 질문하는 사람은 user 정의
        pDTO.setContent(content);

        List<MessageDTO> pList = new LinkedList<>(); // GPT 3.5 파라미터 구조는 배열 구조로 메시지 전달
        pList.add(pDTO);

        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        Gpt35DTO rDTO = Optional.ofNullable(gptService.getGpt35(pList)).orElseGet(Gpt35DTO::new);

        rDTO.setMessage(content); // 웹에서 입력받은 질문을 Controller 결과로 추가하기, HTML 화면에 질문 출력을 위해서...
        log.info(this.getClass().getName() + ".gpt35 End!");

        return rDTO;
    }


}
