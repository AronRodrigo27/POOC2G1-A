/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.empresa.vistacontrol; 
import java.util.ArrayList; 
import pe.edu.empresa.utils.*; 
 
/*
 * @author YWagnerCS
 */
public class Index { 
 private static Lectura leer = new Lectura(); 
 private static ArrayList<String> nombres = new ArrayList<>(); 
 private static ArrayList<Integer> edades = new ArrayList<>(); 
 public static void datos_de_instalacion(){ 
 nombres.add("Aron"); 
 edades.add(17); 
 
 nombres.add("Rodrigo"); 
 edades.add(16); 
 
 nombres.add("Magaño"); 
 edades.add(17); 
 } 
 public static void agregar(){ 
 System.out.println("Agregar Persona: "); 
 System.out.print("> Nombre: "); 
 nombres.add(leer.cadena()); 
 System.out.print(">Edad: "); 
 edades.add(leer.entero()); 
 } 
 public static void eliminar(){ 
 listar(); 
 System.out.print("Indique la posición de la persona a eliminar:"); 
 int posicion = leer.entero(); 
 posicion--; 
 nombres.remove(posicion); 
 edades.remove(posicion); 
 System.out.println("<<Registro eliminado>>"); 
 } 
 public static void editar(){ 
 listar(); 
 System.out.print("Indique la posición de la persona a editar:"); 
 int posicion = leer.entero(); 
 posicion--; 
 System.out.println("> Nombre actual: " + nombres.get(posicion)); 
 System.out.print("* Nuevo nombre:"); 
 String nuevonombre = leer.cadena(); 
 System.out.println("> Edad actual: " + edades.get(posicion)); 
 System.out.print("* Nueva edad:"); 
 int nuevaedad = leer.entero(); 
 nombres.set(posicion, nuevonombre); 
 edades.set(posicion, nuevaedad); 
 System.out.println("<<Registro editado correctamente>>"); 
 } 
 public static void listar(){ 
 System.out.println("Listado de Personas"); 
 System.out.println("*******************"); 
 int num=1; 
 System.out.println("Nro\tNOMBRE\tEDAD"); 
 for (int i = 0; i < nombres.size(); i++) { 
 System.out.println(num + "\t" + nombres.get(i)+ "\t" + edades.get(i)); 
 num++; 
 } 
 } 
 public static void buscar(){ 
 // Implementar el método buscar 
 // Considerar: agregar al menú de opciones 
 
 
 } 
 public static void salir(){ 
 System.out.println("Gracias por su visita"); 
 } 
 public static void error(){ 
 System.out.println("<Error: Opcion inválida>"); 
 } 
 public static void menu(){ 
 System.out.println("MENU PRINCIPAL"); 
 System.out.println("1. Agregar"); 
 System.out.println("2. Eliminar"); 
 System.out.println("3. Editar"); 
 System.out.println("4. Listar"); 
 System.out.println("5. Salir"); 
 System.out.print("Seleccione opcion [1-5]: "); 
 } 
 public static void inicio(){ 
 int opcion; 
 datos_de_instalacion(); 
 do { 
 menu(); 
 opcion = leer.entero(); 
 switch (opcion) { 
 case 1 -> agregar(); 
 case 2 -> eliminar(); 
 case 3 -> editar(); 
 case 4 -> listar(); 
 case 5 -> salir(); 
 default -> error(); 
 } 
 } while (opcion!=5); 
 } 
 public static void main(String[] args) { 
 inicio(); 
 } 
} 