/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.empresa.utils; 
import java.util.Scanner; 
/**
 * @author Daniel Lévano Rodríguez
 */
public class Lectura { 
 private static Scanner teclado = new Scanner(System.in); 
 public String cadena(){ 
 return teclado.nextLine(); 
 } 
 public String cadenaMayuscula(){ 
 return cadena().toUpperCase(); 
 } 
 public String cadenaMinuscula(){ 
 return cadena().toLowerCase(); 
 } 
 public int entero(){ 
 return Integer.parseInt(cadena()); 
 } 
 public double decimal(){ 
 return Double.parseDouble(cadena()); 
 } 
 public char letra(){ 
 return cadena().charAt(0); 
 } 
 public void cerrar(){ 
 teclado.close(); 
 } 
} 