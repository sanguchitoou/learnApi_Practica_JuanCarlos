package IntegracionBackFront.backfront.Controller.Categories;

import IntegracionBackFront.backfront.Entities.Categories.CategoryEntity;
import IntegracionBackFront.backfront.Models.DTO.Categories.CategoryDTO;
import IntegracionBackFront.backfront.Services.Categories.CategoryService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
@CrossOrigin(origins = "*")
public class CategoryController {

    //Inyectamos la clase SERVICE
    @Autowired
    private CategoryService service;

    //GET
    @GetMapping("/getDataCategories")
    private ResponseEntity<Page<CategoryDTO>> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        //Parte 1. Se evalúa cuantos registros desea por página el usuario
        //Se tiene en cuenta que como máximo habrá 50 registros por página
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }

        //Parte 2. Se invoca al método getAllCategories el cual se encuentra en el service
        //Guardamos los datos en el objeto category
        //Si no hay datos en CATEGORY, será null
        Page<CategoryDTO> category = service.getAllCategories(page, size);
        if(category == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay categorías registradas"
            ));
        }

        return ResponseEntity.ok(category);
    }

    //POST


    //PUT


    //DELETE

}
