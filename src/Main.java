import utils.Pass;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] arg) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newCachedThreadPool();
        String password;

        // Nombre del archivo de registro
        String logFileName = "password_log.txt";

        System.out.println("\n-Ingrese Contraseña a validar con las siguientes características: " +
                "\n-8 Caracteres mínimo\n-15 Caracteres máximo" +
                "\n-Utilizar mayúsculas, minúsculas, números y caracteres especiales\n" +
                "-Escriba Salir para terminar la verificación. ");

        while (true) {
            password = scanner.nextLine();
            if (password.equalsIgnoreCase("Salir")) {
                break;
            }
            String finalPassword = password;

            executor.execute(() -> {
                boolean isValid = Pass.password(finalPassword);
                String result = isValid ? "válida" : "no válida";
                System.out.println("\nLa contraseña '" + finalPassword + "' es " + result + ".");

                // Escritura en el archivo de registro
                try (FileWriter writer = new FileWriter(logFileName, true)) {
                    writer.write("Contraseña: '" + finalPassword + "' - Resultado: " + result + "\n");
                } catch (IOException e) {
                    System.err.println("Error al escribir en el archivo de registro: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Verificación completada. Los resultados se han guardado en '" + logFileName + "'.");
    }
}
