package one.microproject.scheduler.tests;

import one.microproject.scheduler.dto.TaskInfo;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SchedulerTests {

    @Autowired
    private WebTestClient webClient;

    private static String accessToken = "";

    @Test
    @Order(0)
    public void testGetTypes() {
        EntityExchangeResult<TaskInfo[]> taskInfosExchange = webClient.get().uri("/services/tasks/types")
                //.header("Authorization", "Bearer: " + accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskInfo[].class).returnResult();
        TaskInfo[] taskInfos = taskInfosExchange.getResponseBody();
        assertEquals(1, taskInfos.length);
    }


}
