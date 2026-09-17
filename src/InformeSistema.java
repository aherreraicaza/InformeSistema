// InformeSistema.java
// Antuan Herrera Icaza - DAM2
// PSP: radiografia del sistema: procesadores, memoria, sistema y propiedades.

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class InformeSistema {

    private static final String RAYA = "==================================================";

    public static void main(String[] args) {
        line("PROCESADORES");
        System.out.println("Disponibles JVM: " + Runtime.getRuntime().availableProcessors());
        System.out.println("(son hilos lógicos: con SMT no coinciden con los núcleos físicos)");

        Runtime r = Runtime.getRuntime();
        r.gc();
        long usadoAntes = r.totalMemory() - r.freeMemory();

        line("MEMORIA · ANTES");
        reportMemoria(r);

        long[] reservado = new long[8 * 1024 * 1024];
        // guardo la referencia y la uso despues para que el GC no la libere antes de medir
        long usadoDespues = r.totalMemory() - r.freeMemory();

        line("MEMORIA · DESPUÉS DE RESERVAR 64 MIB");
        reportMemoria(r);
        System.out.println("Incremento en uso: " + ((usadoDespues - usadoAntes) >> 20) + " MiB");
        System.out.println("(el array sigue en memoria: reservado[0] = " + reservado[0] + ")");

        line("SISTEMA");
        System.out.println("os.name: " + System.getProperty("os.name"));
        System.out.println("file.separator: \"" + File.separator + "\"");
        System.out.println("Ruta construida con las propiedades:");
        System.out.println(System.getProperty("user.home")
                + File.separator + "psp" + File.separator + "informe.txt");

        String[] prefijos = args.length > 0 ? args : new String[]{"os.", "user.", "java.version"};
        line("PROPIEDADES QUE EMPIEZAN POR " + String.join(", ", prefijos));
        Properties props = System.getProperties();
        List<String> claves = new ArrayList<>();
        for (Object k : props.keySet()) {
            String clave = k.toString();
            for (String pre : prefijos) {
                if (clave.startsWith(pre)) {
                    claves.add(clave);
                    break;
                }
            }
        }
        Collections.sort(claves);
        for (String clave : claves) {
            System.out.println(clave + " = " + props.getProperty(clave));
        }

        line("PROCESO EN ESPERA");
        System.out.println("Buscame desde otra terminal con:");
        System.out.println("ps -ef | grep InformeSistema");
        System.out.print("Pulsa INTRO para terminar... ");
        // me quedo esperando para dar tiempo a localizar el proceso desde otra terminal
        new Scanner(System.in).nextLine();
        System.out.println("Fin del programa.");
    }

    private static void reportMemoria(Runtime r) {
        long total = r.totalMemory();
        long libre = r.freeMemory();
        long usado = total - libre;
        System.out.println("Total reservada: " + (total >> 20) + " MiB");
        System.out.println("Libre: " + (libre >> 20) + " MiB");
        System.out.println("En uso: " + (usado >> 20) + " MiB ("
                + Math.round(usado * 100.0 / total) + " % de la total)");
        System.out.println("Máxima (-Xmx): " + (r.maxMemory() >> 20) + " MiB");
    }

    private static void line(String titulo) {
        System.out.println();
        System.out.println(titulo);
        System.out.println(RAYA);
    }
}