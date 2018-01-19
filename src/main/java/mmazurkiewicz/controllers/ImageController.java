package mmazurkiewicz.controllers;

import mmazurkiewicz.commands.RecipeCommand;
import mmazurkiewicz.services.ImageService;
import mmazurkiewicz.services.RecipesService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipesService recipesService;

    public ImageController(ImageService imageService, RecipesService recipesService) {
        this.imageService = imageService;
        this.recipesService = recipesService;
    }

    @GetMapping("recipe/{recipeId}/image")
    public String showUploadForm(@PathVariable String recipeId, Model model){
        model.addAttribute("recipe", recipesService.findCommandById(recipeId));

        return "recipe/imageUploadForm";
    }

    @PostMapping("recipe/{recipeId}/image")
    public String handleImagePost(@PathVariable String recipeId, @RequestParam("imageFile") MultipartFile file){
        imageService.saveImageFile(recipeId, file);

        return "redirect:/recipe/" + recipeId + "/show";
    }

    @GetMapping("recipe/{recipeId}/recipeImage")
    public void renderImageFromDatabase(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipesService.findCommandById(recipeId).block();

        if (recipeCommand.getImage() != null) {
            byte[] byteArray = new byte[recipeCommand.getImage().length];

            int iterator = 0;

            for (Byte wrappedByte : recipeCommand.getImage()) {
                byteArray[iterator++] = wrappedByte;
            }

            response.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(byteArray);
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }
}
