import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class PalabrasDiferentes extends SimpleFileVisitor<Path> {
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        FileReader fl = null;
        BufferedReader in = null;
        String name = file.toAbsolutePath().toString();
        if( name.toLowerCase().endsWith(".txt")) {

            fl = new FileReader(name);
            in = new BufferedReader(fl);
            int contadorLineas = 0;
            int wordCount = 0;
            int numberCount = 0;
            String linea = null;

            String delimiters = "\\s+|,\\s*|\\.\\s*|\\;\\s*|\\:\\s*|\\!\\s*|\\¡\\s*|\\¿\\s*|\\?\\s*|\\-\\s*"
                    + "|\\[\\s*|\\]\\s*|\\(\\s*|\\)\\s*|\\\"\\s*|\\_\\s*|\\%\\s*|\\+\\s*|\\/\\s*|\\#\\s*|\\$\\s*";

            ArrayList<String> list = new ArrayList<String>();

            while ( (linea = in.readLine() )!= null) {
                contadorLineas++;
                if (linea.trim().length() == 0) {
                    continue; // la linea esta vacia, continuar
                }
                // separar las palabras en cada linea
                String words[] = linea.split( delimiters );
                wordCount += words.length;
                for (String theWord : words) {

                    theWord = theWord.toLowerCase().trim();

                    boolean isNumeric = true;

                    // verificar si el token es un numero
                    try {
                        Double num = Double.parseDouble(theWord);
                    } catch (NumberFormatException e) {
                        isNumeric = false;
                    }

                    // Si el token es un numero, pasar al siguiente
                    if( isNumeric ) {
                        numberCount++;
                        continue;
                    }

                    // si la palabra no esta en la lista, agregar a la lista
                    if ( !list.contains(theWord) ) {
                        list.add( theWord );
                    }
                }

            }
            in.close();
            fl.close();

            // Mostrar total de palabras diferentes

            System.out.printf("%-50s Total de palabras: %4d    Palabras diferentes: %3d   Lineas: %3d%n ", name, wordCount, list.size(), contadorLineas);
            System.out.println(list + "\n");
        }
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.printf("No se puede procesar:%30s%n", file.toString()) ;
        return super.visitFileFailed(file, exc);
    }

    public static void main(String[] args) throws IOException {

        // /Users/rnavarro/datos
        if (args.length < 1) {
            System.exit(2);
        }

        // iniciar en este directorio
        Path startingDir = Paths.get(args[0]);

        // clase para procesar los archivos
        PalabrasDiferentes palabras = new PalabrasDiferentes();

        // iniciar el recorrido de los archivos
        Files.walkFileTree(startingDir, palabras);

    }
}