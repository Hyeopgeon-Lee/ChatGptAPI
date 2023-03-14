package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.Gpt35DTO;
import kopo.poly.dto.Gpt3DTO;
import kopo.poly.dto.MessageDTO;
import kopo.poly.service.IGptService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GptService implements IGptService {

    @Value("${chat.gpt.v3.url}")
    private String gpt3Api; // GPT-3 API URL

    @Value("${chat.gpt.v35.url}")
    private String gpt35Api; // GPT-3.5 API URL

    @Value("${chat.gpt.api.key}")
    private String apiKey; // OpenAI 홈페이지에서 발급받은 인증키

    @Override
    public Gpt3DTO getGpt3(Gpt3DTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getGpt3 Start!");

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;utf-8");
        header.put("Authorization", "Bearer " + apiKey);

        pDTO.setModel("text-davinci-003");
        pDTO.setMax_tokens(2048);

        String param = new ObjectMapper().writeValueAsString(pDTO);

        log.info("param : " + param);

//        결과 예 : {"id":"cmpl-6tdNxSufA5SeTksxKLhqspBcqKExk","object":"text_completion","created":1678717457,"model":"text-davinci-003","choices":[{"text":"\n\n- 겨울 계절은
        // 결과는 choices 항목의 text 항목에 저장됨
        String json = NetworkUtil.post(gpt3Api, header, param); // GPT 응답 결과

        log.info("json : " + json);

        String result = ""; // GPT 전체 답변

        // ChatGPT 응답 메시지를 Map 객체로 변환하기
        Map<String, Object> rMap = new ObjectMapper().readValue(json, HashMap.class);

        List<Map> rList = (List<Map>) rMap.get("choices");

        for (Map<String, Object> answer : rList) {
            result += CmmUtil.nvl((String) answer.get("text"));

        }

        log.info("result : " + result);

        // HTML 출력을 이쁘게 하기 위해 \n을 <br/>로 변경함
        pDTO.setResult(result.replaceAll("\n", "<br/>")); // Controller 값을 전달하기 위해 저장

        log.info(this.getClass().getName() + ".getGpt3 Start!");

        return pDTO;
    }

    @Override
    public Gpt35DTO getGpt35(List<MessageDTO> pList) throws Exception {

        log.info(this.getClass().getName() + ".getGpt35 Start!");

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;utf-8");
        header.put("Authorization", "Bearer " + apiKey);

        Gpt35DTO dto = new Gpt35DTO();
        dto.setModel("gpt-3.5-turbo"); // GPT-3.5 모델 정의
        dto.setMessages(pList); // 질문 및 role 정의

        String param = new ObjectMapper().writeValueAsString(dto); // GPT 3.5 전송할 파마미터를 JSON 구조로 만들기

        log.info("param : " + param);

        // "choices":[{"message":{"role":"assistant","content":"\n\n안녕하세요. 저는 AI 어시스턴트입니다. 무엇을 도와드릴까요?"}
        // ,"finish_reason":"stop","index":0}]}
        // 결과는 choices 항목의 text 항목에 저장됨
        String json = NetworkUtil.post(gpt35Api, header, param); // GPT에 응답 결과

        log.info("json : " + json);

        String result = ""; // GPT 3.5 전체 답변

        // ChatGPT 응답 메시지를 Map 객체로 변환하기
        Map<String, Object> rMap = new ObjectMapper().readValue(json, HashMap.class);

        List<Map> choices = (List<Map>) rMap.get("choices");

        for (Map<String, Object> answer : choices) {
            Map<String, Object> message = (Map<String, Object>) answer.get("message");

            result += CmmUtil.nvl((String) message.get("content")); // 결과

        }

        log.info("result : " + result);

        // HTML 출력을 이쁘게 하기 위해 \n을 <br/>로 변경함
        dto.setResult(result.replaceAll("\n", "<br/>")); // Controller 값을 전달하기 위해 저장

        log.info(this.getClass().getName() + ".getGpt35 Start!");

        return dto;
    }

}
