package mmazurkiewicz.controllers;

import mmazurkiewicz.commands.RecipeCommand;
import mmazurkiewicz.services.ImageService;
import mmazurkiewicz.services.RecipesService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    @Mock
    ImageService imageService;

    @Mock
    RecipesService recipesService;

    ImageController imageController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

        imageController = new ImageController(imageService, recipesService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void getImageForm() throws Exception{
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        when(recipesService.findCommandById(anyString())).thenReturn(Mono.just(command));

        //when
        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipesService, times(1)).findCommandById(anyString());
    }

    @Test
    public void handleImagePost() throws Exception{
        MockMultipartFile multipartFile = new MockMultipartFile("imageFile", "testing.txt", "text/plain",
                "Spring framework".getBytes());

        when(imageService.saveImageFile(anyString(),any())).thenReturn(Mono.empty());

        mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/1/show"));

        verify(imageService, times(1)).saveImageFile(anyString(), any());
    }

    @Test
    public void renderImageFromDatabase() throws Exception{
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId("1");

        String string = "fake text";
        Byte[] bytesBoxed = new Byte[string.getBytes().length];

        int iterator = 0;

        for (byte primitiveByte : string.getBytes()){
            bytesBoxed[iterator++] = primitiveByte;
        }

        command.setImage(bytesBoxed);

        when(recipesService.findCommandById(anyString())).thenReturn(Mono.just(command));

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeImage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseBytes = response.getContentAsByteArray();

        assertEquals(string.getBytes().length, responseBytes.length);
    }

/*    @Test
    public void testImageNumberFormatException() throws Exception{
        mockMvc.perform(get("/recipe/ii/recipeImage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }*/
}
