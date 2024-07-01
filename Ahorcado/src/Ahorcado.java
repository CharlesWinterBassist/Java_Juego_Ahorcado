import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Ahorcado {
    public static void main(String[] args) throws Exception {
        
        Scanner teclado = new Scanner(System.in) ;
        boolean entradaValida = true ;
        final String palabraSecreta = generarPalabraSecreta() ;
        final int maxIntentos = (int)(palabraSecreta.length() * 0.8) ;
        char[] arrayPalabra = generarPartida(palabraSecreta) ;
        boolean[] arrayDescubierto = new boolean[arrayPalabra.length] ;
        int intentos = 0 ;
        boolean adivinada = false ;
        String letraIntroducida ;

        System.out.println("-BIENVENIDO AL JUEGO DEL AHORCADO-\n");

        do {
            imprimirSituacion(arrayPalabra, arrayDescubierto) ;

            do {
                System.out.printf("Fallos restantes: %d\n", maxIntentos - intentos);
                System.out.print("Introduzca una letra: ");
                letraIntroducida = teclado.nextLine().toLowerCase() ;
                entradaValida = comprobarEntrada(letraIntroducida) ;
            } while (!entradaValida);

            if (palabraSecreta.contains(letraIntroducida)) {
                for (int i = 0; i < arrayPalabra.length; i++) {
                    if(arrayPalabra[i] == letraIntroducida.charAt(0)) {
                        arrayDescubierto[i] = true ;
                    }
                }
            } else {
                intentos++ ;
                System.out.printf("La letra \"%s\" no aparece en la palabra.\n\n", letraIntroducida);
            }
            
            adivinada = comprobarVictoria(arrayDescubierto) ;

        } while (!adivinada && intentos < maxIntentos) ;

        System.out.println("\n");
        imprimirSituacion(arrayPalabra, arrayDescubierto);
        System.out.println("");

        if (adivinada) {
            System.out.println("¡Enhorabuena, has ganado!\n");
        } else {
            System.out.printf("Solución: %s\n", palabraSecreta);
            System.out.println("Lástima, quizás la próxima vez.\n");
        }

        teclado.close() ;

        System.out.println("-FIN DEL PROGRAMA-");

    }



    private static String generarPalabraSecreta() {
        
        String palabraSecreta = null ;
        int numLineas = 0 ;

        try (BufferedReader br = new BufferedReader(new FileReader("./Ahorcado/Palabras_Secretas.txt")) ) {
            while (br.readLine() != null) {
                numLineas++ ;
            }
        } catch (IOException ex) {
            System.out.println("Error durante la generación de la palabra secreta.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader("./Ahorcado/Palabras_Secretas.txt")) ) {
            int numAleatorio = (int)(Math.random() * numLineas) ;
            for (int i = 0; i < numAleatorio; i++) {
                br.readLine();
            }
            palabraSecreta = br.readLine() ;
        } catch (IOException ex) {
            System.out.println("Error durante la generación de la palabra secreta.");
        }

        palabraSecreta = normalizar(palabraSecreta) ;

        return palabraSecreta ;
    }



    private static String normalizar(String palabra) {

        // Normaliza el texto para descomponer caracteres acentuados:
        String palabraNormalizada = Normalizer.normalize(palabra, Normalizer.Form.NFD);
        
        // Expresión regular para eliminar las marcas diacríticas:
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        
        // Reemplaza las marcas diacríticas por una cadena vacía:
        palabraNormalizada = pattern.matcher(palabraNormalizada).replaceAll("");

        return palabraNormalizada ;
    }



    private static char[] generarPartida(String palabraSecreta) {

        char[] arrayPalabra = new char[palabraSecreta.length()] ;

        for (int i = 0; i < arrayPalabra.length; i++) {
            arrayPalabra[i] = palabraSecreta.charAt(i) ;
        }

        return arrayPalabra ;
    }



    private static boolean comprobarEntrada(String letraIntroducida) {

        boolean entradaValida = true ;

        if(letraIntroducida.length() > 1) {
            System.out.println("Solamente debes introducir una letra.\n");
            entradaValida = false ;
        }

        if(letraIntroducida.charAt(0) < 'a' || letraIntroducida.charAt(0) > 'z') {
            System.out.println("Solamente se admiten letras, sin acentos.\n");
            entradaValida = false ;
        }

        return entradaValida ;
    }


    
    private static void imprimirSituacion(char[] arrayPalabra, boolean[] arrayDescubierto) {
        String salida = "" ;

        for (int i = 0; i < arrayPalabra.length; i++) {

            if(arrayDescubierto[i]) {
                salida += arrayPalabra[i] + " " ;
            } else {
                salida += "_ " ;
            }
        }

        salida = salida.substring(0, salida.length()-1) ;
        salida += "\n" ;

        System.out.println(salida);
    }



    private static boolean comprobarVictoria(boolean[] arrayDescubierto) {
        boolean victoria = true ;

        for (int i = 0; i < arrayDescubierto.length; i++) {
            if (arrayDescubierto[i] == false) {
                victoria = false ;
            }
        }

        return victoria ;
    }



}
