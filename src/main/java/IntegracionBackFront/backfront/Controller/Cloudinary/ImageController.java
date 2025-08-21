package IntegracionBackFront.backfront.Controller.Cloudinary;

import IntegracionBackFront.backfront.Services.Cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.util.Map;

@RestController
@RequestMapping("/api/image")
@Validated
public class ImageController {
    //Inyectamos servicio de Cloudinary
    @Autowired
    private final CloudinaryService objCloudService;

    //Constructor de CloudinaryService
    public ImageController(CloudinaryService objCloudService) {
        this.objCloudService = objCloudService;
    }

    //Método que permite CARGAR LA IMÁGEN (POST) con respuesta a la entidad
    @PostMapping("/postImage")
    public ResponseEntity<?> postImage(@RequestParam("image") MultipartFile file){
        try{
            //Llamamos al SERVICIO para subir la imagen y obtenemos la URL de la misma
            String imageUrl = objCloudService.uploadImage(file);
            //Se debe de llamar SI O SI esta variable URL, a la hora de llamar al FRONTEND
            return ResponseEntity.ok(Map.of(
                    "message", "Imagen subida exitosamente",
                    "url", imageUrl
            ));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al subir la imágen, error: " + e);
        }
    }
}
