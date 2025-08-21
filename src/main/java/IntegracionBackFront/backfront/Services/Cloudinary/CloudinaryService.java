package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    //Definimos COMO PRIMERA INSTANCIA (dependiendo) el tamaño de las imágenes en MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    //Definimos las extenciones QUE PERMITIMOS como subida de imagen (JPGE, PNG, etc.)
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png"};

    //Atributo CLOUDINARY
    private final Cloudinary cloudinary;

    //Constructor que permite la inyección de dependencias del Cloudinary
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    //Creación del método que cargará el cometido (imágenes, PDFS, etc.)
    //MultipartFile sirve para subir CUALQUIER ARCHIVO, subida de imágenes a la raíz de CLOUDINARY (Sin carpetas)
    public String uploadImageRoot(MultipartFile file) throws IOException {
        validateImage(file);
        Map<?, ?> updloadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "resource_type", "auto",
                "quality", "auto:good"
        ));
        //Retornamos la imágen subida a la RAÍZ de Cloudinary
        return (String) updloadResult.get("secure_url");
    }

    //Método que permie subir imágenes EN CARPETAS ESPECÍFICAS dentro de Cloudinary
    public String uploadImageFolder(MultipartFile file, String folder) throws IOException {
        validateImage(file);
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        //Cambio de nombre por SEGURIDAD, manteniendo la extensión y agregando variable "img_"
        String uniqueFileName = "img_" + UUID.randomUUID() + fileExtension;

        //Declaración de validaciones
        Map<String, Object> options = ObjectUtils.asMap(
                "folder", folder,       //Carpeta de destino de la imagen
                "public_id", uniqueFileName,    //Nombre único para el archivo
                "user_filename", false,         //Indicamos que NO se usará el nombre original
                "unique_filename", false,       //No generará nombre único (ya que se hace con el UUID)
                "overwrite", false,             //No se sobreescribirán archivos existentes
                "resource_type", "auto",
                "quality", "auto:good"
        );

        //Subida del archivo con MAP
        Map<?, ?> uploadResultImage = cloudinary.uploader().upload(file.getBytes(), options);
        return (String) uploadResultImage.get("secure_url");
    }

    //Creación del MÉTODO que validará la subida del archivo
    private void validateImage(MultipartFile file) {
        //Verificamos si el archivo está vacío
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        //Verificamos si el tamaño excede el límite permitidos
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El tamaño del archivo no debe ser mayor a 5MB");
        }

        //Verificamos la extensión del archivo
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("El nombre del archivo es inválido");
        }

        //Extraemos y validamos la extensión del archivo
        String validExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(ALLOWED_EXTENSIONS).contains(validExtension)) {
            throw new IllegalArgumentException("Solo se permiten archivos .jpg, .jpeg, .png");
        }

        //Verificamos que el tipo MIME sea una imágen
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imágen válida");
        }
    }
}
