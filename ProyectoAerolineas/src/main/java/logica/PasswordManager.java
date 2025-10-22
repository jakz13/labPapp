package logica;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad para generar salt y hashear/verificar contraseñas usando SHA-256.
 * Proporciona métodos estáticos para uso en la capa de persistencia y lógica.
 */
public class PasswordManager {

    /** Longitud en bytes del salt aleatorio generado para cada contraseña. */
    private static final int SALT_LENGTH = 16;

    /**
     * Genera un salt aleatorio codificado en Base64.
     * @return salt en Base64 listo para almacenarse junto al hash.
     */
    public static String generarSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Calcula el hash SHA-256 de la contraseña usando el salt provisto.
     * @param password contraseña en texto plano
     * @param salt salt en Base64
     * @return hash en Base64
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            mDigest.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = mDigest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error al encriptar contraseña", e);
        }
    }

    /**
     * Verifica si la contraseña en texto plano coincide con el hash almacenado.
     * @param password contraseña en texto plano
     * @param salt salt en Base64 utilizado para hashear
     * @param hashedPassword hash en Base64 almacenado
     * @return true si coinciden, false en caso contrario
     */
    public static boolean verificarPassword(String password, String salt, String hashedPassword) {
        String nuevoHash = hashPassword(password, salt);
        return nuevoHash.equals(hashedPassword);
    }
}