package kopo.poly.service;

import kopo.poly.dto.ChatGptDTO;

public interface IChatGptService {

    ChatGptDTO getChatGptResult(ChatGptDTO pDTO) throws Exception;

}

