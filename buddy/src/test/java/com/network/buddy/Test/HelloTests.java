package com.network.buddy.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.network.buddy.HelloService;
import com.network.buddy.HeloWorld;
import com.network.buddy.service.JwtService;
import com.network.buddy.service.UserService;

@WebMvcTest(HeloWorld.class)
@AutoConfigureMockMvc(addFilters = false)
public class HelloTests {

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HelloService helloService;

    @Test
    void shouldReturnHelloWorld() throws Exception {
        // Arrange
        when(helloService.sayHello()).thenReturn("Hello World!");

        // Act + Assert
        mockMvc.perform(get("/hello")).andExpect(status().isOk()).andExpect(content().string("Hello World!"));
    }

}
