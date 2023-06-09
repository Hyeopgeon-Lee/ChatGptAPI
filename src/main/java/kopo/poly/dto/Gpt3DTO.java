package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class Gpt3DTO {

    private String model; // 학습모델 : text-davinci-003
    private String prompt; // GPT 물어볼 질문
    private int max_tokens; // 결과를 받을 최대 토큰 수(비용문제때문에

    private String result; // GPT 대답

}

