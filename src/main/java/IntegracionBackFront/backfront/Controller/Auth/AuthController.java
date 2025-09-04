package IntegracionBackFront.backfront.Controller.Auth;

import IntegracionBackFront.backfront.Entities.Users.UserEntity;
import IntegracionBackFront.backfront.Models.DTO.Users.UserDTO;
import IntegracionBackFront.backfront.Services.Auth.AuthService;
import IntegracionBackFront.backfront.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    //Iniciamos inyectando el service
    private AuthService objAuthService;
    //Inyectamos el JWT
    private JWTUtils objJWTToken;

    //Método POST
    @PostMapping("/login")
    private ResponseEntity<String> login(@Valid @RequestBody UserDTO userDTO, HttpServletRequest response){
        //Validación de datos repetidos
        if (userDTO.getCorreo() == null || userDTO.getCorreo().isBlank() ||
                userDTO.getContrasena() == null || userDTO.getContrasena().isBlank()) {
            return ResponseEntity.status(401).body("Error: Credenciales incompletas");
        }
        if(objAuthService.Login(userDTO.getCorreo(), userDTO.getContrasena())){
            return ResponseEntity.ok("Inicio de sesión exitoso");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
    private void addTokenCookie(HttpServletResponse response, String correo) {
        // Obtener el usuario completo de la base de datos
        Optional<UserEntity> userOpt = objAuthService.obtenerUsuario(correo);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            String token = objJWTToken.create(
                    String.valueOf(user.getId()),
                    user.getCorreo(),
                    user.getTipoUsuario().getNombreTipo() // ← Usar el nombre real del tipo
            );

            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
    }
}
