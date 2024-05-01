package mediaSoft.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mediaSoft.storage.controller.dto.GoodsRequestDto;
import mediaSoft.storage.service.search.criteria.BigDecimalSearchCriteria;
import mediaSoft.storage.service.search.criteria.SearchCriteria;
import mediaSoft.storage.service.search.criteria.StringSearchCriteria;
import mediaSoft.storage.service.search.enums.OperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class GoodsSearchControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  public void searchGoods_Success_ShouldReturnListOfGoods() throws Exception {

    GoodsRequestDto dto1 = createRequestGood1();
    GoodsRequestDto dto2 = createRequestGood2();
    GoodsRequestDto dto3 = createRequestGood3();
    GoodsRequestDto dto4 = createRequestGood4();

    mockMvc.perform(post("/goods")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto1))
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    mockMvc.perform(post("/goods")
            .content(objectMapper.writeValueAsString(dto2))
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    mockMvc.perform(post("/goods")
            .content(objectMapper.writeValueAsString(dto3))
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    mockMvc.perform(post("/goods")
            .content(objectMapper.writeValueAsString(dto4))
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(MockMvcResultMatchers.status().isCreated());

    SearchCriteria<String> nameCriteria = StringSearchCriteria.builder()
            .field("quantity")
            .value("30")
            .operation(OperationType.LESS_THAN_OR_EQ)
            .build();

    SearchCriteria<BigDecimal> priceCriteria = BigDecimalSearchCriteria.builder()
            .field("price")
            .value(BigDecimal.valueOf(20))
            .operation(OperationType.GREATER_THAN_OR_EQ)
            .build();

    List<SearchCriteria<?>> searchCriteriaList = Arrays.asList(nameCriteria, priceCriteria);

    mockMvc.perform(get("/goods/search")
                    .param("page", "0")
                    .param("size", "10")
                    .content(objectMapper.writeValueAsString(searchCriteriaList))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())

            .andExpect(jsonPath("$", hasSize(2)))

            .andExpect(jsonPath("$[0].name").value("Good2"))
            .andExpect(jsonPath("$[0].article").value("Article2"))
            .andExpect(jsonPath("$[0].quantity").value("20"))

            .andExpect(jsonPath("$[1].name").value("Good3"))
            .andExpect(jsonPath("$[1].article").value("Article3"))
            .andExpect(jsonPath("$[1].quantity").value("30"));
  }

  private GoodsRequestDto createRequestGood1() {
    return GoodsRequestDto.builder()
            .name("Good1")
            .article("Article1")
            .description("Description1")
            .goodCategory("Category1")
            .price(BigDecimal.TEN)
            .quantity(10)
            .build();
  }

  private GoodsRequestDto createRequestGood2() {
    return GoodsRequestDto.builder()
            .name("Good2")
            .article("Article2")
            .description("Description2")
            .goodCategory("Category2")
            .price(BigDecimal.valueOf(20))
            .quantity(20)
            .build();
  }

  private GoodsRequestDto createRequestGood3() {
    return GoodsRequestDto.builder()
            .name("Good3")
            .article("Article3")
            .description("Description3")
            .goodCategory("Category3")
            .price(BigDecimal.valueOf(30))
            .quantity(30)
            .build();
  }

  private GoodsRequestDto createRequestGood4() {
    return GoodsRequestDto.builder()
            .name("Good4")
            .article("Article4")
            .description("Description4")
            .goodCategory("Category4")
            .price(BigDecimal.valueOf(40))
            .quantity(40)
            .build();
  }
}