package com.usco.esteban.agenda_procesos.app;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Locale;
//import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AgendaProcesosApplication implements CommandLineRunner
{
	/* CommandLineRunner es una interfaz para el metodo run()*/
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(AgendaProcesosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		
		/*acá se genera las contraseñas de ejemplo */
		//String password = "12345678,.";
		//String password1 = "123456";
		
		/*for(int i =0; i<2; i++)
		{
			String bcryptPassword = passwordEncoder.encode(password);
			System.out.println(bcryptPassword);
		}*/
		
		
//		String bcryptPassword1 = passwordEncoder.encode(password);
//		System.out.println("la clave es: " + bcryptPassword1);
		
		Locale locale = Locale.getDefault();
		TimeZone timeZone = TimeZone.getDefault();
		
		Calendar fechaActual = Calendar.getInstance(timeZone, locale);
		
		Calendar fechaFinal = Calendar.getInstance(timeZone, locale);
		fechaFinal.set(2020, 10, 16);
		fechaFinal.add(Calendar.DAY_OF_YEAR, 336);
		System.out.println("LA FECHA FINAL MAS 336 DIAS ES: " + fechaFinal.getTime());
		
//		System.out.println("La localidad es: "+ Locale.getDefault());
//		System.out.println("El time zone es: "+ TimeZone.getDefault());
		
//		String [] timeZones = TimeZone.getAvailableIDs();
//	    System.out.println("Available Timezones "+Arrays.toString(timeZones));
		
		//Calendar fechaInicial = detalle.getFechaInicial();
		/*int dia = 19;
		int mes = 6;
		int año = 2020;
		
		fechaFinal.set(año, mes, dia);
		fechaFinal.set(Calendar.YEAR, 2020);
		fechaFinal.set(Calendar.MONTH, Calendar.JULY);
		fechaFinal.set(Calendar.DAY_OF_MONTH, 19);*/
		
		fechaActual.set(Calendar.HOUR, 0);
		fechaActual.set(Calendar.HOUR_OF_DAY, 0);
		fechaActual.set(Calendar.MINUTE, 0);
		fechaActual.set(Calendar.SECOND, 0);
		
		fechaFinal.set(Calendar.HOUR, 0);
		fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
		fechaFinal.set(Calendar.MINUTE, 0);
		fechaFinal.set(Calendar.SECOND, 0);
		
		
		
		//long fechaIncialMS = fechaInicial.getTimeInMillis();
		long fechaFinalMS = fechaFinal.getTimeInMillis();
		long fechaActualMS = fechaActual.getTimeInMillis();
		
		int dias = (int) ((Math.abs(fechaFinalMS - fechaActualMS)) / (1000 * 60 * 60* 24));
		
		System.out.println("los dias desde el inicio de la app son: " + dias);
		
//		if(dias == 30)
//		{
//			System.out.println("dias igual a 30");
//		}
//		
//		String nombre1 = "notificacion demandado";
//		String nombre2 = "notificacion demandado";
//		
//		if(nombre1.equals("notificacion demandado"))
//		{
//			System.out.println("las dos son iguales");
//		}
//		
//		fechaFinal.add(Calendar.DAY_OF_YEAR, 365);
//		System.out.println("la fecha mas 365 con DAY OF YEAR es: " + fechaFinal.getTime());
//		
//		fechaFinal.add(Calendar.DAY_OF_MONTH, 365);
//		System.out.println("la fecha mas 365 con DAY OF MONTH es: " + fechaFinal.getTime());
	}

}
