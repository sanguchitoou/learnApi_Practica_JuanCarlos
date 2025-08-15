package IntegracionBackFront.backfront.Config.Cloudinary;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    //VARIABLES PARA UTILIZAR CLOUD
    private String cloudName;
    private String apiKey;
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary(){
        //Cargamos TODAS LA VARIABLES del archivo .env
        Dotenv loadDOTenv = Dotenv.load();

        //Almacenar configuración en un formato MAP
        Map<String, String> saveConfig = new HashMap<>();

        //Obtenemos las credenciales desde las variables de entorno del ARCHIVO .env
        saveConfig.put("cloud_name", loadDOTenv.get("CLOUDINARY_CLOUD_NAME"));
        saveConfig.put("api_key", loadDOTenv.get("CLOUDINARY_API_KEY"));
        saveConfig.put("api_secret", loadDOTenv.get("CLOUDINARY_API_SECRET"));

        //Retornamos una instancia de cloudinary con la configuración cargada
        return new Cloudinary(saveConfig);
    }
}
