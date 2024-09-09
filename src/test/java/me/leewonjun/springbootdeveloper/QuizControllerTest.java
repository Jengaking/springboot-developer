package me.leewonjun.springbootdeveloper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @DisplayName("quiz(): GET /quiz?code=1 이면 응답 코드는 201, 응답 본문은 Create!를 리턴한다.")
    @Test
    public void getQuiz1() throws Exception {
        String url = "/quiz";
        final ResultActions result = mockMvc.perform(get(url).param("code", "1"));
        // result의 메소드로 matcher를 전달하여 검증한다.
        // status().isCreated()는 matcher이다. Predicate이나 Comparator, Collector 처럼 전달하여 사용 : 전략 패턴.
        result.andExpect(status().isCreated()).andExpect(MockMvcResultMatchers.content().string("Created!"));
    }

    @DisplayName("quiz(): GET /quiz?code=2 이면 응답 코드는 400, 응답 본문은 Bad Request!를 리턴한다.")
    @Test
    public void getQuiz2() throws Exception {
        String url = "/quiz";
        final ResultActions result = mockMvc.perform(get(url).param("code", "2"));

        result.andExpect(status().isBadRequest()).andExpect(MockMvcResultMatchers.content().string("Bad Request!"));
    }

    @DisplayName("quiz(): POST /quiz?code=1 이면 응답 코드는 403, 응답 본문은 Forbidden!를 리턴한다.")
    @Test
    public void postQuiz1() throws Exception {
        // given
        final String url = "/quiz";
        Code code = new Code(1);

        // when
        // code를 직렬화 -> 뭐.. 컨트롤러에선 알아서 Deserialize 하지 않을까?
        final ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(code)));

        // then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("Forbidden!"));
    }

    @DisplayName("quiz(): POST /quiz?code=13 이면 응답 코드는 200, 응답 본문은 OK!를 리턴한다.")
    @Test
    public void qostQuiz13() throws Exception {
        final String url = "/quiz";
        Code code = new Code(13);

        final ResultActions result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(code)));

        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("OK!"));
    }
}