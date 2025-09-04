package IntegracionBackFront.backfront.Config.Argon2;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Service;

@Service
public class Argon2Password {
    //Variables de inicialización del HASH Argon2ID
    private static final int ITERATIONS = 10;
    private static final int MEMORY = 32678;
    private static final int PARALLELISM = 2;

    //Instancia de la clase Argon
    private Argon2 objHashArgon = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    //Indentar las variables del hasheo
    public String EncryptPassword(String password){
        return objHashArgon.hash(ITERATIONS, MEMORY, PARALLELISM, password);
    }

    public boolean VerifyPassword(String hashDB, String password) {
        return objHashArgon.verify(hashDB, password);
    }
}
