package cz.tomas.alibi.common.controller;

import cz.tomas.alibi.common.domain.ItemCategory;
import cz.tomas.alibi.common.dtomapper.ItemDtoMapper;
import cz.tomas.alibi.common.exception.ApiExceptionHandler;
import cz.tomas.alibi.common.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ApiExceptionHandler.class, ItemDtoMapper.class})
class ItemControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ItemService itemService;

    @BeforeEach
    void items() {
        when(itemService.getItems(any(), any())).thenReturn(Page.empty());
    }

    @Test
    void itemCategoryFilterIsPassedToService() throws Exception {
        mvc.perform(get("/api/item").param("category", "TOOL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(0));

        verify(itemService).getItems(eq(ItemCategory.TOOL), any());
    }

    @Test
    void invalidItemCategoryReturnsMalformedRequestProblem() throws Exception {
        mvc.perform(get("/api/item").param("category", "NOT_A_CATEGORY"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Malformed request"));
    }
}
