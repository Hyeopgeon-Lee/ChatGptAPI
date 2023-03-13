package kopo.poly.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.ChatGptDTO;
import kopo.poly.service.IChatGptService;
import kopo.poly.util.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatGptService implements IChatGptService {

    @Value("${chat.gpt.url}")
    private String apiUrl; // Chat GPT API URL

    @Value("${chat.gpt.api.key}")
    private String apiKey; // OpenAI 홈페이지에서 발급받은 인증키

    @Override
    public ChatGptDTO getChatGptResult(ChatGptDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getChatGptResult Start!");

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json;utf-8");
        header.put("Authorization", "Bearer " + apiKey);

        pDTO.setModel("text-davinci-003");
        pDTO.setMax_tokens(2048);

        String param = new ObjectMapper().writeValueAsString(pDTO);

        log.info("param : " + param);

//        결과 예 : {"id":"cmpl-6tdNxSufA5SeTksxKLhqspBcqKExk","object":"text_completion","created":1678717457,"model":"text-davinci-003","choices":[{"text":"\n\n- 겨울 계절은
        // 결과는 choices 항목의 text 항목에 저장됨
        String json = NetworkUtil.post(apiUrl, header, param); // ChatGPT 응답 결과

        log.info("json : " + json);

        String result = ""; // ChatGPT 전체 답변

        // ChatGPT 응답 메시지를 Map 객체로 변환하기
        Map<String, Object> rMap = new ObjectMapper().readValue(json, HashMap.class);

        List<Map> rList = (List<Map>) rMap.get("choices");

        for (Map<String, Object> answer : rList) {
            result += answer.get("text");

        }

        log.info("result : " + result);

        // HTML 출력을 이쁘게 하기 위해 \n을 <br/>로 변경함
        pDTO.setResult(result.replaceAll("\n", "<br/>")); // Controller 값을 전달하기 위해 저장

        log.info(this.getClass().getName() + ".getChatGptResult Start!");

        return pDTO;
    }
}
