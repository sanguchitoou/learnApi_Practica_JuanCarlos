package IntegracionBackFront.backfront.Services.Auth;

import IntegracionBackFront.backfront.Config.Argon2.Argon2Password;
import IntegracionBackFront.backfront.Entities.Users.UserEntity;
import IntegracionBackFront.backfront.Repositories.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    //Inicialización del Service, INYECTAOS el REPO del USER
    @Autowired
    private UserRepository repo;
    public  boolean Login (String correo, String contrasena){
        Argon2Password objHash = new Argon2Password();
        Optional<UserEntity> list = repo.findByCorreo(correo).stream().findFirst();
        if (list.isPresent()){
            UserEntity usuario = list.get();
            String nombreTipoUsuario = usuario.getTipoUsuario().getNombreTipo();
            System.out.println("Usuario ID encontrado: " + usuario.getId() + ", email: " + usuario.getCorreo() + ", rol: " + nombreTipoUsuario);
            //Obtener la clave del usuario que está en la base de datos
            //Es lo mismo que decir en resumen de esas tres lineas comentadas abajo hey
            return objHash.VerifyPassword(usuario.getContrasena(),contrasena);
//            String HashDB = usuario.getContrasena();
//            boolean verificado = objHash.VerifyPassword(HashDB, contrasena);
//            return  verificado;
        }
        return false;
    }
    public Optional<UserEntity> obtenerUsuario (String email){
        Optional<UserEntity> userOpt = repo. findByCorreo(email);
        return (userOpt != null) ? userOpt : null;
    }
}