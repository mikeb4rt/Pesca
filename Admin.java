import java.io.*;
import java.util.Date;
import java.util.Scanner;

public class Admin {

    private final File users = new File("ResourcesPesca/usuaris.txt");
    private final File mediterranea = new File("ResourcesPesca/mediterrania.txt");
    private final File florida = new File("ResourcesPesca/florida.txt");
    private final File register = new File("ResourcesPesca/registres.txt");

    //CONSTRUCTOR
    public Admin() {
    }

    //Lee una columna de un archivo
    private String read(FileReader read, int column) throws IOException {
        //String donde se guarda la columna
        StringBuilder Fishname = new StringBuilder();
        //Caracter que se esta leyendo actualmente
        int actualCharacter = 0;
        //Contador de # para saber en que punto del fichero se encuentra
        int htgCounter = 0;
        //Mientras se hayan contado menos de column # y el fichero no haya acabado
        while (htgCounter < column && actualCharacter != -1) {
            //Cambia el caracter actual
            actualCharacter = read.read();
            //Si el caracter es distinto a #
            if ((char) actualCharacter != '#') {
                //Y se ha contado column - 1 #
                if (htgCounter == column - 1) {
                    //Añade el caracter actual a un string
                    Fishname.append((char) actualCharacter);
                }
            } else {
                //Si el caracter es un #
                htgCounter++;
            }
        }
        //Cerramos la conexion
        read.close();
        //Devolvemos el nombre del pez
        return Fishname.toString();
    }


    //Salta lineas de los archivos
    private FileReader readLine(File file, int line, int totalHtg) throws IOException {
        //Fichero a leer
        FileReader readFishery = new FileReader(file);
        //Caracter actual
        int actualCharacter = 0;
        //Contador de #
        int lineCounter = 0;
        //Mientras el numero de # sea menor a 2 * (line - 1) y el fichero no se haya acabado
        while (lineCounter < totalHtg * (line - 1) && actualCharacter != -1) {
            //Cambiamos el caracter
            actualCharacter = readFishery.read();
            //Si el caracter actual es # aumenta lineCounter
            if ((char) actualCharacter == '#') {
                lineCounter++;
            }
        }
        //Devuelve el lector
        return readFishery;
    }

    //Inserta usuarios en usuaris.txt
    private void insertUser() throws Exception {
        //Pregunta al usuario que introduzca un nombre
        String name = userInfo("Introduzca su nombre: ");
        //Si el usuario no esta registrado
        if (!checkExist(users, name)) {
            //Conexion para escribir en el fichero sin sobreescribirlo
            FileWriter user = new FileWriter(users, true);
            //Contenido que se escribira
            String userInfo = "#" +
                    name +
                    "#" +
                    "\n";
            //Escribe el users
            user.write(userInfo);
            //Cierra la conexion
            user.close();
            //Si esta registrado tira una excepcion
        } else {
            throw new Exception("Usuario ya registrado");
        }
    }

    //Borra usuarios de user.txt
    private void deleteUser(String name) throws Exception {
        //Comprueba si existe el usuario
        if (checkExist(users, name)) {
            //Informacion que no se borrara
            StringBuilder nonDeleted = new StringBuilder();
            //Linea del usuario a borrar
            int rowToDelete = checkLine(users, name);
            //Contador de lineas
            int line = 1;
            //Mientras no se acaben las lineas
            while (readLine(users, line, 2).read() != -1) {
                //Si la linea que se esta leyendo coincide con la que se ha de borrar
                if (line == rowToDelete) {
                    //Aumenta line
                    line++;
                }
                //Añade un usuario a nonDeleted
                nonDeleted.append("#").append(read(readLine(users, line, 2), 2)).append("#\n");
                //Aumenta line
                line++;
            }
            //Sobreescribe el fichero
            FileWriter users = new FileWriter("src/Pesca/ResourcesPesca/usuaris.txt");
            //Escribe en el fichero
            String sNonDeleted = nonDeleted.toString();
            sNonDeleted = sNonDeleted.substring(0, sNonDeleted.length() - 3);
            users.write(sNonDeleted);
            users.close();
            //Si no existe lanza una excepcion
        } else {
            throw new Exception("No existe ese usuario");
        }
    }

    //Desplega menu para pesca
    private void fishingMenu() throws IOException {
        //Comprueba si el usuario esta registrado
        String name = userInfo("Introduzca su nombre: ");
        if (checkExist(users, name)) {
            boolean fishery = false;
            //Mientras no se escriba una pesquera valida
            while (!fishery) {
                System.out.println("1.-Mediterranea\n2.-Florida\n");
                int option = Integer.parseInt(userInfo("Donde quiere pescar: "));
                //Llama al metodo fish pasando el usuario y el fichero de la pesquera correspondiente
                switch (option) {
                    case 1:
                        System.out.println("Va a percar en la mediterranea");
                        fish(mediterranea, name);
                        fishery = true;
                        break;
                    case 2:
                        System.out.println("Va a percar en florida");
                        fish(florida, name);
                        fishery = true;
                        break;
                    default:
                        System.out.println("Opcion no valida");
                        break;
                }
            }

        } else {
            System.out.println("No tiene licencia");
        }
    }

    //Añade una pesca al fichero registres.txt
    private void fish(File file, String name) throws IOException {
        //Escribimos en el fichero register sin sobreescribirlo
        FileWriter fishRegister = new FileWriter(register, true);
        //Numero random para determinar el pez
        double fished = Math.random();
        System.err.println(fished);
        //Contador de linea para obtener el pez
        int line = 1;
        //Bandera
        boolean baited = false;
        while (!baited) {
            //Cuando encuentra la linea con la probabilidad del pez sale de la bandera
            if (fished < Double.parseDouble(read(readLine(file, line, 5), 3))) {
                baited = true;
            } else {
                line++;
            }
        }
        Date actualDate = new Date();
        //Registramos El usuario que ha pescado, el nombre del pez, y de manera aleatoria entre el peso minimo y maximo del pez sacamos su peso
        fishRegister.write("#" + read(readLine(users, checkLine(users, name), 2), 2) + "#" +
                read(readLine(file, line, 5), 2) + "#" +
                (Math.random() * (Double.parseDouble(read(readLine(file, line, 5), 5))) + Double.parseDouble(read(readLine(file, line, 5), 4)) + "#" + actualDate + "#"
                        + "\n"));
        //Cierra writer
        fishRegister.close();
        System.err.println("#" + read(readLine(users, checkLine(users, name), 2), 2) + "#" +
                read(readLine(file, line, 5), 2) + "#" +
                (Math.random() * (Double.parseDouble(read(readLine(file, line, 5), 5))) + Double.parseDouble(read(readLine(file, line, 5), 4)) + "#"));
    }

    private void fishStadistics(File file) throws Exception {
        int line = 1;
        while (readLine(file, line, 5).read() != -1) {
            int lineR = 1;
            double maxWeigthFish = 0;
            String fish = read(readLine(file, line, 5), 2);
            while (readLine(register, lineR, 5).read() != -1) {
                if (fish.equals(read(readLine(register, lineR, 5), 3))) {
                    if (Double.parseDouble(read(readLine(register, lineR, 5), 4)) > maxWeigthFish) {
                        maxWeigthFish = Double.parseDouble(read(readLine(register, lineR, 5), 4));
                    }
                }
                lineR++;
            }
            if (maxWeigthFish > 0) {
                System.out.println(fish + " record de peso: " + maxWeigthFish);
            }
            line++;
        }
    }

    public void fishStadisticsPerUser(File file, String user) throws Exception {
        int line = 1;
        while (readLine(file, line, 5).read() != -1) {
            int lineR = 1;
            double maxWeigthFish = 0;
            String fish = read(readLine(file, line, 5), 2);
            while (readLine(register, lineR, 4).read() != -1) {
                if (user.equals(read(readLine(register, lineR, 5), 2))) {
                    if (fish.equals(read(readLine(register, lineR, 5), 3))) {
                        if (Double.parseDouble(read(readLine(register, lineR, 5), 4)) > maxWeigthFish) {
                            maxWeigthFish = Double.parseDouble(read(readLine(register, lineR, 5), 4));
                        }
                    }
                }
                lineR++;
            }
            if (maxWeigthFish > 0) {
                System.out.println(fish + " record de peso: " + maxWeigthFish);
            }
            line++;
        }
    }

    //Pide al usuario que introduzca datos
    private String userInfo(String mesage) {
        Scanner info = new Scanner(System.in);
        System.out.print(mesage);
        return info.nextLine();
    }

    //Comprueba si el usuario esta registrado
    private boolean checkExist(File file, String checked) throws IOException {
        //Linea para el fichero
        int line = 1;
        //Variable donde se almacenaran los nombres registrados
        String checking;
        //Mientras el fichero no se acabe
        while (readLine(file, line, 2).read() != -1) {
            //Lee el nombre de la linea actual
            checking = read(readLine(file, line, 2), 2);
            //Si el nombre coincide con el que se esta comprobando devuelve true
            if (checking.equals(checked)) {
                return true;
            }
            line++;
        }
        return false;
    }

    //Comprueba si el usuario esta registrado
    private int checkLine(File file, String checked) throws IOException {
        //Linea para el fichero
        int line = 1;
        //Variable donde se almacenaran los nombres registrados
        String checking;
        //Mientras el fichero no se acabe
        while (readLine(file, line, 2).read() != -1) {
            //Lee el nombre de la linea actual
            checking = read(readLine(file, line, 2), 2);
            //Si el nombre coincide con el que se esta comprobando la linea
            if (checking.equals(checked)) {
                return line;
            }
            line++;
        }
        return line + 1;
    }

    //Menu principal del juego
    public void menu() throws Exception {
        boolean menu = false;
        while (!menu) {
            System.out.println("""
                    **************************************************************
                    * Bienvenidos al programa de pesca Miguel Rodrigo Serna      *
                    * Menu Principal                                             *
                    **************************************************************
                    * 1.- Dar de alta usuario                                    *
                    * 2.- Dar de baja usuario                                    *
                    * 3.- Pescar en una pesquera                                 *
                    * 4.- Estadisticas por usuario                               *
                    * 5.- Estadisticas globales                                  *
                    * 6.- Salir                                                  *
                    **************************************************************
                    """);
            int option = Integer.parseInt(userInfo("Que desea hacer? "));
            switch (option) {
                case 1:
                    System.out.println("Dar de alta usuario");
                    insertUser();
                    break;
                case 2:
                    System.out.println("Dar de baja usuario");
                    String userDelete = userInfo("A que usuario quiere dar de baja? ");
                    deleteUser(userDelete);
                    break;
                case 3:
                    System.out.println("Pescar en una pesquera");
                    fishingMenu();
                    break;
                case 4:
                    System.out.println("Estadisticas por usuario");
                    String user = userInfo("De que usuario quiere saber las estadisticas? ");
                    fishStadisticsPerUser(mediterranea, user);
                    fishStadisticsPerUser(florida, user);
                    break;
                case 5:
                    System.out.println("Estadisticas globales");
                    fishStadistics(mediterranea);
                    fishStadistics(florida);
                    break;
                case 6:
                    System.out.println("Salir");
                    menu = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }
}